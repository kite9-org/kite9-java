package org.kite9.tool.model;

import java.io.File;
import java.io.IOException;

import org.kite9.framework.model.ProjectModel;
import org.kite9.framework.model.ProjectModelImpl;
import org.kite9.tool.context.AbstractContextualizable;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * This class uses spring & asm functionality to scan the contents of the
 * project to build the project model.
 * 
 * 
 * @author moffatr
 * 
 */
public class SpringProjectModelFactory extends AbstractContextualizable {

	public ProjectModel createProjectModel() throws IOException {
		int fileCount = 0;
		ClassFileModelBuilder cfmb = new ClassFileModelBuilder();
		String[] paths = getContext().getClassPath().split(File.pathSeparator);
		for (int i = 0; i < paths.length; i++) {
			FileSystemResourceLoader loader = new FileSystemResourceLoader();
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(loader);

			String packageSearchPath =(paths[i].startsWith("/") ? "file:/" : "" ) + paths[i] + "/" + ((basePackage.length() > 0) ? basePackage.replace(".", "/") + "/" : "") + pattern;
			getContext().getLogger().send("Searching: "+packageSearchPath);
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

			for (Resource resource : resources) {
				fileCount++;
				cfmb.visit(resource);
			}

		}

		ProjectModelImpl model = cfmb.getModel();
		
		getContext().getLogger().send(
				"Created project model from \"" + getContext().getClassPath() + "\" with " + fileCount + " files and "
						+ model.getClassCount() + " classes");

		return model;

	}

	
	
	private String basePackage = "";
	private String pattern = "**/*.class";

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}


}
