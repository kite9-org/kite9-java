package org.kite9.tool.scanner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.server.WorkItem;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * Uses spring class patterns to figure out which classes to build design items
 * from.
 * 
 * @author moffatr
 * 
 */
public class SpringClassScanner extends AbstractClassScanner implements Scanner {

    protected String resolveBasePackage(String basePackage) {
	return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    public List<WorkItem> getItems() {
	try {
	    List<WorkItem> out = new ArrayList<WorkItem>(300);
	    MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
	    String[] paths = getContext().getClassPath().split(File.pathSeparator);
	    for (int i = 0; i < paths.length; i++) {
		FileSystemResourceLoader loader = new FileSystemResourceLoader();
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(loader);

		String packageSearchPath = (paths[i].startsWith("/") ? "file:/" : "" ) + paths[i] + "/" + ((basePackage.length() > 0) ? basePackage.replace(".", "/") + "/" : "") + pattern;
		Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

		int resourceCount = 0;

		for (Resource r : resources) {
		    resourceCount++;
		    MetadataReader reader = metadataReaderFactory.getMetadataReader(r);
		    ClassMetadata cm = reader.getClassMetadata();
		    String name = cm.getClassName();
		    List<WorkItem> local = getWorkItems(name);
		    out.addAll(local);
		}

		getContext().getLogger().send("Scanned " + resourceCount +" resources matching \""+packageSearchPath+"\"");

	    }

	    return out;
	} catch (IOException e) {
	    throw new Kite9ProcessingException("Could not create work items", e);
	}

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
