package org.kite9.tool.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.adl.Diagram;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.serialization.XMLHelper;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.AbstractContextualizable;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class XMLFileScanner extends AbstractContextualizable implements Scanner {

	private XMLHelper helper = new XMLHelper();

	public List<WorkItem> getItems() {
		FileSystemResourceLoader loader = new FileSystemResourceLoader();
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(
				loader);
		try {
			String packageSearchPath = baseFolder + "/" + pattern;
			Resource[] resources = resourcePatternResolver
					.getResources(packageSearchPath);
			List<WorkItem> out = new ArrayList<WorkItem>(resources.length * 2);

			for (Resource r : resources) {
				getContext().getLogger().send("Scanning: " + r);
				Object o = helper.fromXML(r.getInputStream());

				if (o instanceof Diagram) {
					out.add((Diagram) o);
				} else {
					getContext().getLogger().error(
							"Cannot process: " + o.toString());
				}
			}

			return out;

		} catch (IOException e) {
			throw new Kite9ProcessingException("Could not retrieve classes: ",
					e);
		}
	}

	private String baseFolder = "";
	private String pattern = "**/*.adl";

	public String getBaseFolder() {
		return baseFolder;
	}

	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
