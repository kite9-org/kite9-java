package org.kite9.tool.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kite9.framework.common.ClassHelp;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.AbstractContextualizable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * This class modifies a javadoc file by inserting extra text within the
 * javadocs. This allows diagrams (or anything else) to augment the javadoc
 * files.
 * 
 * @author moffatr
 * 
 */
public abstract class AbstractJavadocModifyingListener extends AbstractContextualizable implements BuildListener, InitializingBean {

	private Map<Class<?>, Set<WorkItem>> modifications = new HashMap<Class<?>, Set<WorkItem>>(1000);

	/**
	 * This gets hold of a resource for a specific class.
	 */
	public Resource getJavadocHtmlForClass(Class<?> c) {
		String rootDir = getDocRoot();
		String path = createPathForClass(c);
		File f = new File(rootDir + "/" + path);
		if (f.exists()) {
			return new FileSystemResource(f);
		} else {
			getContext().getLogger().send("Could not find javadoc for: " + c + " at " + f);
			return null;
		}
	}

	String docRoot = "target/docs/api";
	boolean processing = true;

	private String getDocRoot() {
		return docRoot;
	}

	public String createPathForClass(Class<?> c) {
		String className = c.getName();
		return className.replace(".", "/").replace("$", ".") + ".html";
	}

	public void afterPropertiesSet() {
		checkJavadocsExist();
		if (!processing) {
			getContext().getLogger().send("Not augmenting javadocs - index.html not present at " + docRoot);
		}
	}

	private void checkJavadocsExist() {
		File f = new File(getDocRoot());
		if (!f.isDirectory()) {
			processing = false;
		}

		File f2 = new File(getDocRoot() + "/index.html");
		if (!f2.exists()) {
			processing = false;
		}
	}

	public boolean canProcess(WorkItem designItem) {
		return processing;
	}

	protected String classDocPattern() {
		return "<!-- ======== START OF CLASS DATA ======== -->.*?<(HR|hr)>.*?</(pre|PRE)>";
	}

	protected String methodDocPattern() {
		return "<(A|a) (NAME|name)=\"([a-zA-Z_0-9]*?)\\(.*?\\)\">.*?<(pre|PRE)>.*?</(pre|PRE)>";
	}

	protected void processClass(Class<?> cl, Set<WorkItem> mods) throws IOException {
		Resource r = getJavadocHtmlForClass(cl);
		if (r==null) {
			return;
		}
		Reader is = new InputStreamReader(r.getInputStream());

		// read the lot into memory
		StringBuffer input = new StringBuffer(100000);
		char[] buffer = new char[1000];
		boolean hasMore = true;

		while (hasMore) {
			int amt = is.read(buffer);
			if (amt > 0) {
				input.append(buffer, 0, amt);
			}
			hasMore = amt > 0;
		}

		is.close();

		OutputStreamWriter os = null;
		int cursor = 0;

		try {
			os = new OutputStreamWriter(new FileOutputStream(r.getFile()));

			String classDocPattern = classDocPattern();
			Matcher m = Pattern.compile(classDocPattern, Pattern.DOTALL).matcher(input);
			boolean found = m.find();

			if (found) {
				int at = m.end();
				os.write(input.substring(cursor, at));
				processClassLevelDoc(cl, r.getFile(), os, mods);
				cursor = at;
			} else {
				throw new Kite9ProcessingException("Could not find string: \"" + classDocPattern + "\" in "
						+ r.getFilename());
			}

			// ok, next up, match any methods
			String methodDocPattern = methodDocPattern();
			m = Pattern.compile(methodDocPattern, Pattern.DOTALL).matcher(input);

			found = m.find(cursor);

			while (found) {
				int at = m.end();
				String toInsert = input.substring(cursor, at);
				os.write(toInsert);
				processMethodLevelDoc(cl, r.getFile(), m.group(3), os, mods);
				cursor = at;
				found = m.find(cursor);
			}

		} finally {
			try {
				if (cursor < input.length()) {
					os.write(input.substring(cursor, input.length()));
				}
			} finally {
				os.close();
			}
		}
	}

	/**
	 * Override this to include any of your own processing for the class-heading
	 * level javadoc.
	 * 
	 * @throws IOException
	 */
	protected abstract void processClassLevelDoc(Class<?> cl, File htmlResource, OutputStreamWriter os,
			Set<WorkItem> mods2) throws IOException;

	protected abstract void processMethodLevelDoc(Class<?> cl, File htmlResource, String method, OutputStreamWriter os,
			Set<WorkItem> mods2) throws IOException;

	public void process(WorkItem designItem) throws ClassNotFoundException {
		try {
			String classPart = designItem.getID().substring(0, designItem.getID().lastIndexOf("."));
			Class<?> cl = ClassHelp.loadClass(classPart, getContext().getUserClassLoader());
			addReferenceToClass(designItem, cl);
			processReferences(designItem);
		} catch (Throwable e) {
			// if this happens, chances are we can't find the class for the design item.
			getContext().getLogger().send("Couldn't find class for: "+designItem.getID());
		}

	}

	/**
	 * Add functionality here to parse the designItem, and attach references to
	 * it within the javadoc.
	 * 
	 * @param designItem
	 */
	protected abstract void processReferences(WorkItem designItem);

	protected void addReferenceToClass(WorkItem designItem, Class<?> cl) {
		Set<WorkItem> mods = modifications.get(cl);
		if (mods == null) {
			mods = new HashSet<WorkItem>(10);
			modifications.put(cl, mods);
		}

		mods.add(designItem);
	}

	public void finished() {
		// ok, now it's time to make the changes
		for (Entry<Class<?>, Set<WorkItem>> mod : modifications.entrySet()) {
			try {
				processClass(mod.getKey(), mod.getValue());
			} catch (IOException e) {
				getContext().getLogger().error("Could not process javadoc changes for " + mod.getKey(), e);
			}
		}
	}

	/**
	 * Returns the relative url of the classname's javadoc page
	 * 
	 * @param url
	 * @return
	 */
	public static String convertToURL(String fileSource, String fileDest, String prefix, String suffix) {
	    String[] fromParts = (fileSource==null) ? new String[0] :  fileSource.split("\\.");
	    StringBuffer out = new StringBuffer(100);
	    if (prefix!=null)
		out.append(prefix);
	    
	    String[] toParts = fileDest.split("\\.");

		int matched = 0;
		while ((matched < fromParts.length - 1) && (matched < toParts.length - 1)
				&& (fromParts[matched].equals(toParts[matched]))) {
			matched++;
		}

		for (int i = matched; i < fromParts.length - 1; i++) {
			out.append("../");
		}

		for (int i = matched; i < toParts.length; i++) {
			out.append(toParts[i].replace("$", "."));
			if (i < toParts.length - 1) {
				out.append("/");
			}
		}

		out.append(suffix);
		return out.toString();
	}

	public void setDocRoot(String docRoot) {
		this.docRoot = docRoot;
	}
}
