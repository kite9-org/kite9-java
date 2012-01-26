package org.kite9.framework.classloading;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.kite9.framework.common.RepositoryHelp;

/**
 * Handles loading of classes from a nested jar files. Handles a special
 * 'override class' which will be loaded only using this class loader.
 * 
 * 
 * @author robmoffat
 * 
 */
public class OverrideJarClassLoader extends ClassLoader {

    String overrideClass;
    boolean logging;

    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
	Class<?> clazz = findLoadedClass(name);
	if (clazz != null) {
	    return clazz;
	}

	if (!overrideClass.equals(name)) {
	    try {
		clazz = getSystemClassLoader().loadClass(name);
		if (clazz != null) {
		    if (resolve)
			resolveClass(clazz);
		    log("Returning (System ClassLoader) " + clazz + " with class loader: "
			    + clazz.getClassLoader());
		    return (clazz);
		}
	    } catch (ClassNotFoundException e) {
	    }

	    ClassLoader loader = getParent();
	    if (loader != null) {
		try {
		    clazz = loader.loadClass(name);
		    if (clazz != null) {
			if (resolve)
			    resolveClass(clazz);
			log("Returning (Parent ClassLoader) " + clazz + " with class loader: "
				+ clazz.getClassLoader());
			return (clazz);
		    }
		} catch (ClassNotFoundException e) {
		}
	    }
	}

	String file = convertToFile(name);
	if (memoryMappedFiles.containsKey(file)) {
	    clazz = findClass(name);
	    if (clazz != null) {
		if (resolve)
		    resolveClass(clazz);
		log("Returning (OverrideJarClassLoader) " + clazz + " with class loader: "
			+ clazz.getClassLoader());
		return clazz;
	    }

	}

	throw new ClassNotFoundException(name);
    }

    URL[] urls;

    Map<String, byte[]> memoryMappedFiles = new HashMap<String, byte[]>();
    Map<String, Integer> startPosition = new HashMap<String, Integer>();
    Map<String, Integer> lengths = new HashMap<String, Integer>();

    public OverrideJarClassLoader(URL[] urls, ClassLoader parent, String overrideClass, boolean log) throws IOException {
	super(parent);
	this.urls = urls;
	init();
	this.overrideClass = overrideClass;
	this.logging = log;
    }

    protected void init() throws IOException {
	for (int i = 0; i < urls.length; i++) {
	    Set<String> files = new HashSet<String>(500);

	    URLConnection c = urls[i].openConnection();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(20000);

	    ZipInputStream zis = new ZipInputStream(c.getInputStream());
	    ZipEntry ze = zis.getNextEntry();
	    while (ze != null) {
		int start = baos.size();
		startPosition.put(ze.getName(), start);
		RepositoryHelp.streamCopy(zis, baos, false);
		int end = baos.size();
		lengths.put(ze.getName(), end - start);
		files.add(ze.getName());
		ze = zis.getNextEntry();
	    }

	    zis.close();
	    baos.close();

	    byte[] theFile = baos.toByteArray();

	    for (String name : files) {
		memoryMappedFiles.put(name, theFile);
	    }

	    log("mapped file: " + urls[i]);
	}
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
	try {
	    log("Finding: "+name);
	    String path = convertToFile(name);
	    byte[] theFile = memoryMappedFiles.get(path);
	    Integer start = startPosition.get(path);
	    Integer length = lengths.get(path);

	    if (theFile == null) {
		throw new ClassNotFoundException("Could not load class, not found in any jar: " + path);
	    }

	    Class<?> out = defineClass(name, theFile, start, length);
	    return out;

	} catch (ClassFormatError e) {
	    throw new ClassNotFoundException("Could not load class: ", e);
	}

    }

    private String convertToFile(String name) {
	return name.replace('.', '/').concat(".class");
    }

    public boolean isLogging() {
	return logging;
    }

    public void setLogging(boolean logging) {
	this.logging = logging;
    }

    public void log(String l) {
	if (logging) {
	    System.err.println("OJCL: "+l);
	}
    }
}
