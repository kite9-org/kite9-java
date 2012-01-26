package org.kite9.tool.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.IdentifiableDiagramElement;
import org.kite9.diagram.visitors.DiagramElementVisitor;
import org.kite9.diagram.visitors.VisitorAction;
import org.kite9.framework.common.RepositoryHelp;
import org.kite9.framework.server.WorkItem;

/**
 * This class helps process the javadoc files, adding links to the diagrams.
 * 
 * @author moffatr
 * 
 */
public class Kite9DiagramJavadocListener extends AbstractJavadocModifyingListener {

	private static final String PROJECT_JAVA_PACKAGE = "project_java_package:";
	private static final String PROJECT_JAVA_CLASS = "project_java_class:";

	protected void processClassLevelDoc(Class<?> cl, File htmlResource, OutputStreamWriter os, Set<WorkItem> mods2)
			throws IOException {

		for (WorkItem workItem : mods2) {
			referenceInternalDiagram(cl, htmlResource, os, workItem);
		}

	}

	protected void referenceDiagram(Class<?> cl, File htmlResource, OutputStreamWriter os, WorkItem workItem) {
		try {
			os.write("<a href=\"" + convertToURL(cl.getName(), workItem.getSubjectId(), null, ".html") + "#"
					+ workItem.getName() + "\" title=\"Review the javadoc for this page\">");
			os.write("This class is referenced in the diagram <em>" + workItem.getName() + "</em>.");
			os.write("</a><br />\n");
		} catch (IOException e) {
			getContext().getLogger().error("Could not change javadoc page: "+workItem.getSubjectId()+" "+workItem.getName(), e);
			return;
		}
	}

	protected void referenceInternalDiagram(Class<?> cl, File htmlResource, OutputStreamWriter os, WorkItem workItem) {
		try {
			os.write("<a href=\"" + convertToURL(cl.getName(), workItem.getSubjectId(), null, ".html") + "#"
					+ workItem.getName() + "\" title=\"Review the javadoc for this page\">");
			os.write("This class has diagram <em>" + workItem.getName() + "</em>.");
			os.write("</a><br />\n");
		} catch (IOException e) {
			getContext().getLogger().error("Could not change javadoc page: "+workItem.getSubjectId()+" "+workItem.getName(), e);
			return;
		}
	}

	public String prepareCaption(WorkItem wi) {
		StringBuffer out = new StringBuffer();
		out.append(Character.toUpperCase(wi.getName().charAt(0)));
		for (int i = 1; i < wi.getName().length(); i++) {
			char c0 = wi.getName().charAt(i - 1);
			char c1 = wi.getName().charAt(i);
			if (Character.isLowerCase(c0) && Character.isUpperCase(c1)) {
				out.append(" ");
			}
			out.append(c1);
		}

		return out.toString();
	}

	protected void inlineDiagram(Class<?> cl, File htmlResource, OutputStreamWriter os, WorkItem workItem) {
		// first, copy in the diagram
		File packageDir = htmlResource.getParentFile();
		String pageName = htmlResource.getName();
		String dirName = pageName.substring(0, pageName.lastIndexOf("."));
		File classDir = new File(packageDir, dirName);
		boolean ok = classDir.exists() || classDir.mkdirs();
		if (!ok) {
			getContext().getLogger().error("Did not create directory: " + classDir);
			return;
		}
		
		String localName = dirName + "/" + workItem.getName() + ".png";
		File imageResource = new File(classDir, workItem.getName() + ".png");

		InputStreamReader map = null; 
		
		try {
			InputStream source = getContext().getRepository().retrieve(workItem.getSubjectId(), workItem.getName(), "png");
			OutputStream dest = new FileOutputStream(imageResource);
			getContext().getLogger().send("Copying: "+source+" to "+imageResource);
			RepositoryHelp.streamCopy(source, dest, true);
			map = new InputStreamReader(getContext().getRepository().retrieve(workItem.getSubjectId(),
					workItem.getName(), "map"));

		} catch (IOException e) {
			getContext().getLogger().error("Could not find image: "+workItem.getSubjectId()+" "+workItem.getName(), e);
			return;
		}
		String caption = prepareCaption(workItem);

		// next, write the html for the diagram
		try {
			os.write("<div class=\"kite9image\" style=\"text-align: center\">\n");
			os.write("<a name=\"" + workItem.getName() + "\" />\n");
			
			if (map != null) {
				os.write("<map name=\"" + workItem.getName() + "\">\n");
				processMap(map, os, cl, null);

				map.close();
				os.write("</map>\n");
			}
			os.write("<img src=\"" + localName + "\""+(map==null ? "" : " usemap=\"#" + workItem.getName()) + "\" border=\"0\" alt=\"" + caption
					+ "\"/>");
			os.write("<br />\n" + caption + "\n</div>\n");
		} catch (IOException e) {
			getContext().getLogger().error("Could not change javadoc page: "+workItem.getSubjectId()+" "+workItem.getName(), e);
			return;
		}
	}

	public static void processMap(Reader map, Writer os, Class<?> cl, String prefix) throws IOException {
		BufferedReader br = new BufferedReader(map);
		boolean hasNext = true;
		while (hasNext) {
			String line = br.readLine();

			if (line == null) {
				hasNext = false;
			} else {
				// get the url
				int startPos = line.indexOf("id=\"") + 4;

				int endPos = line.indexOf("\"", startPos);
				String id = line.substring(startPos, endPos);
				int classEndPos = id.indexOf("/");
				String method = "";
				if (classEndPos > -1) {
					method = id.substring(classEndPos + 1);
				} else {
					classEndPos = id.length();
				}

				if (id.startsWith(PROJECT_JAVA_CLASS)) {
					String classSubstring = id.substring(PROJECT_JAVA_CLASS.length(), classEndPos);
					line = line.substring(0, startPos-4) + "href=\""
							+ convertToURL(cl == null ? null : cl.getName(), classSubstring, prefix, ".html"
									+ (method.length() > 0 ? "#" + method : "")) +"\" target=\"_parent\""+ line.substring(endPos+1);
				} else if (id.startsWith(PROJECT_JAVA_PACKAGE)) {
					line = line.substring(0, startPos -4) + "href=\""
							+ convertToURL(cl == null ? null : cl.getName(), id.substring(PROJECT_JAVA_PACKAGE
									.length()), prefix, "/package-summary.html") + "\" target=\"_parent\""+line.substring(endPos+1);
				}

				os.write(line);
				os.write("\n");
			}
		}
	}

	@Override
	protected void processMethodLevelDoc(Class<?> cl, File htmlResource, String method, OutputStreamWriter os,
			Set<WorkItem> mods2) throws IOException {
		for (WorkItem workItem : mods2) {
			if (workItem.getDesignItem() instanceof Diagram) {
				if (workItem.getSubjectId().equals(cl.getName())) {
					if (workItem.getName().equals(method)) {
						inlineDiagram(cl, htmlResource, os, workItem);
					}
				}
			}
		}

	}

	@Override
	protected void processReferences(final WorkItem designItem) {
		if (designItem.getDesignItem() instanceof Diagram) {
			DiagramElementVisitor dev = new DiagramElementVisitor();
			dev.visit((Diagram) designItem.getDesignItem(), new VisitorAction() {
				public void visit(DiagramElement de) {
					if (de instanceof IdentifiableDiagramElement) {
						IdentifiableDiagramElement rde = (IdentifiableDiagramElement) de;
						if (rde.getID().startsWith(PROJECT_JAVA_CLASS)) {
							createProjectJavaClassReference(designItem, rde);
						}
					}
				}

				private void createProjectJavaClassReference(final WorkItem designItem, IdentifiableDiagramElement rde) {
					String className = rde.getID().substring(PROJECT_JAVA_CLASS.length());
					int slashIndex = className.indexOf("/");
					if (slashIndex > -1) {
						className = className.substring(0, slashIndex);
					}
					try {
						Class<?> to = getContext().getUserClassLoader().loadClass(className);
						addReferenceToClass(designItem, to);
					} catch (Exception e) {
						getContext().getLogger().error("Could not find class: " + className + " for " + rde.getID());
					}
				}
			});
		}
	}

}
