package org.kite9.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.ZipOutputStream;

import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.alias.PropertyAliaser;
import org.kite9.framework.common.HelpMethods;
import org.kite9.framework.common.RepositoryHelp;
import org.kite9.framework.model.ProjectModel;
import org.kite9.framework.repository.BasicFileRepository;
import org.kite9.framework.repository.Repository;
import org.kite9.framework.server.AbstractLocalServer;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.Kite9Context;
import org.kite9.tool.context.Kite9ContextImpl;
import org.kite9.tool.listener.ServerCallingBuildListener;
import org.kite9.tool.model.SpringProjectModelFactory;
import org.kite9.tool.scanner.BasicClassScanner;
import org.kite9.tool.scanner.DefaultingClassScanner;
import org.kite9.tool.scanner.MockBuildListener;
import org.kite9.tool.scanner.XMLFileScanner;

public class AbstractRunnerTest extends HelpMethods {

	protected static final String TARGET_DOCS = "target/functional-test/docs";

	public void invokeRunner(String args) throws Exception {

		Main.main(new String[] { "-p", "kite9-test.properties",
				"scanner.base-package=" + this.getClass().getPackage().getName(),
				"listener.main.class=" + MockBuildListener.class.getName() });
	}

	static class StreamGobbler extends Thread {
		InputStream is;
		String type;
		StringBuffer created = new StringBuffer(1000);

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					created.append(type + ">" + line);
			} catch (IOException ioe) {
				System.out.println(created.toString());
				ioe.printStackTrace();
			}
		}
	}

	public int exec(String cmd) {
		StreamGobbler errorGobbler = null;
		StreamGobbler outputGobbler = null;

		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);

			// any error message?
			errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERR");

			// any output?
			outputGobbler = new StreamGobbler(proc.getInputStream(), "OUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
			return exitVal;
		} catch (Throwable t) {
			return -1;
		}
	}

	public void generateJavadocs() throws Exception {
		String cp = System.getProperty("java.class.path");
		String home = System.getProperty("java.home");

		String[] javadocargs = { "-d", TARGET_DOCS, "-classpath", cp, "-sourcepath", ("src/test/java"),
				this.getClass().getPackage().getName() };

		StringBuffer args = new StringBuffer();
		for (String string : javadocargs) {
			args.append(string);
			args.append(" ");
		}

		String cmd = home + "/../bin/javadoc.exe ";
		int res = exec(cmd + args.toString());
		if (res == -1) {
			res = exec("javadoc " + args.toString());
		}

		if (res == -1) {
			throw new Exception("Could not generate javadocs with either 'javadoc' or '" + cmd + "'");
		}

	}

	protected Kite9Context ctx = createContext();

	protected static Kite9Context createContext() {
		Aliaser a = new PropertyAliaser();
		Repository<File> r = new BasicFileRepository();
		Kite9ContextImpl out = new Kite9ContextImpl(a, r);
		out.setClassPath("target/classes" + File.pathSeparator + "target/test-classes");
		out.setProjectId("35");
		out.setSecretKey("PFB9T1V97AK96WFK");
		out.afterPropertiesSet();
		return out;
	}
	
	public static ProjectModel createModel() throws IOException {
		Kite9Context ctx = createContext();
		SpringProjectModelFactory fact = new SpringProjectModelFactory();
		fact.setContext(ctx);
		fact.setBasePackage(AbstractRunnerTest.class.getPackage().getName());
		
		return fact.createProjectModel();
	    }

	protected BasicClassScanner createLocalClassScanner(ProjectModel pm) {
		DefaultingClassScanner scs = new DefaultingClassScanner();
		scs.setContext(ctx);
		scs.setProjectModel(pm);
		scs.setBasePackage(this.getClass().getPackage().getName());
		return scs;
	}

	protected XMLFileScanner createLocalFileScanner() {
		XMLFileScanner xfs = new XMLFileScanner();
		xfs.setContext(ctx);
		String folder = this.getClass().getPackage().getName();
		folder = folder.replace(".", "/");
		folder = "src/test/resources/"+folder;
		xfs.setBaseFolder(folder);
		return xfs;
	}

	
	protected ServerCallingBuildListener createServerCallingListener() {
		ServerCallingBuildListener scbl = new ServerCallingBuildListener();
		scbl.setContext(ctx);
		scbl.setServer(new AbstractLocalServer() {
			@Override
			protected void processItem(WorkItem workItem, ZipOutputStream zos) throws IOException {
				// just returns 'canned' responses from the test artifacts
				// directory
				File png = getFileFromWorkItem(workItem, "png");
				if (png.exists()) {
					beginZipEntry(workItem, "png", zos);
					RepositoryHelp.streamCopy(new FileInputStream(png), zos, false);
				}

				File map = getFileFromWorkItem(workItem, "map");
				if (map.exists()) {
					beginZipEntry(workItem, "map", zos);
					RepositoryHelp.streamCopy(new FileInputStream(map), zos, false);
				}

			}
		});
		return scbl;
	}

	protected File getFileFromWorkItem(WorkItem wi, String ext) throws FileNotFoundException {
		String id = wi.getSubjectId().toLowerCase();
		String name = wi.getName();
		String full = "/" + id.replace(".", "/") + "/" + name + "." + ext;
		URL found = AbstractRunnerTest.class.getResource(full);
		if (found == null) {
			throw new FileNotFoundException("Could not find: " + full);
		}
		String file = found.getFile();
		return new File(file);
	}
}
