package org.kite9.tool;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.kite9.framework.classloading.OverrideJarClassLoader;

public class Main {

	public static final String TOOL_CLASS = "org.kite9.tool.Tool";

	public static void main(String args[]) {
		try {
			Main main = new Main();
			URL[] out = main.getLibNames();
			ClassLoader classLoader = setupClassLoader(out, TOOL_CLASS, hasArg("-Xcl", args));
			invokeGo(args, classLoader, TOOL_CLASS);
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e).getTargetException();
			}
			e.printStackTrace();
			System.out
					.print("USAGE: \n java -jar <this jar file> [-p <properties file name>] [argName=value] [-Xcl] ...\n");
		}
	}

	private static boolean hasArg(String string, String args[]) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(string)) {
				return true;
			}
		}

		return false;
	}

	private static void invokeGo(String[] args, ClassLoader classLoader, String goClassName)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {
		Class<?> c = classLoader.loadClass(goClassName);
		Object instance = c.newInstance();
		Method toCall = c.getMethod("go", new Class<?>[] { String[].class });
		toCall.invoke(instance, (Object) args);
	}

	private static ClassLoader setupClassLoader(URL[] out, String goClassName, boolean logging) throws IOException {
		ClassLoader parent = Main.class.getClassLoader();
		ClassLoader classLoader;
		if ((out != null) && (out.length > 0)) {
			System.out.println("logging: "+logging);
			classLoader = new OverrideJarClassLoader(out, parent, goClassName, logging);
		} else {
			classLoader = parent;
		}
		Thread.currentThread().setContextClassLoader(classLoader);
		return classLoader;
	}

	public URL[] getLibNames() throws IOException {
		ClassLoader loader = getClass().getClassLoader();

		for (Enumeration<URL> manifests = loader.getResources("META-INF/MANIFEST.MF"); manifests.hasMoreElements();) {
			URL manifestURL = manifests.nextElement();

			if ((manifestURL.getPath().contains("kite9")) && manifestURL.toString().startsWith("jar:")) {
				String basePath = manifestURL.toString();
				basePath = basePath.substring(0, basePath.indexOf("!"));
				basePath = basePath.substring(4); // remove jar:
				URL baseURL = new URL(basePath);

				List<URL> out = new ArrayList<URL>();

				ZipInputStream zis = new ZipInputStream(baseURL.openStream());
				ZipEntry ze = zis.getNextEntry();
				while (ze != null) {
					String name = ze.getName();
					if (name.startsWith("lib/") && name.endsWith(".jar")) {
						out.add(new URL("jar:" + basePath + "!/" + name));
					}
					ze = zis.getNextEntry();
				}

				zis.close();

				return (URL[]) out.toArray(new URL[out.size()]);
			}
		}

		return null; // must be running outside jar
	}

}
