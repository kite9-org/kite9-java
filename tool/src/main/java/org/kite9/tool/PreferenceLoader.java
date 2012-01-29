package org.kite9.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import org.kite9.framework.common.RepositoryHelp;

public class PreferenceLoader {
	
	/**
	 * Creates a default preferences file that the user can edit
	 */
	public static void createDefaultPreferences(String fileName) throws IOException {
		File out = new File(fileName);
		
		if (out.exists()) {
			throw new IOException("Preferences file: "+out.toString()+" already exists");
		}
		
		InputStream defaults = PreferenceLoader.class.getResourceAsStream("/default.properties"); 
		OutputStream os = new FileOutputStream(out);
		
		RepositoryHelp.streamCopy(defaults, os, true);
	}

	public static Properties getPreferences(String fileName) throws IOException {

		File out = getPreferencesFileFromUserHome(fileName);
		if (out == null)
			out = getPreferencesFileFromLocalPath(fileName);

		if (out == null) {
			out = getPreferencesFileFromClasspath(fileName);
		}

		if (out == null)
			return null;

		Properties p = new Properties();
		p.load(new FileInputStream(out));

		return p;
	}

	private static File getPreferencesFileFromLocalPath(String name) {
		try {
			File f = new File(name);
			if (f.exists())
				return f;
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	private static File getPreferencesFileFromClasspath(String name) {
		try {
			URL u = PreferenceLoader.class.getResource("/" + name);
			File f = new File(u.getFile());
			if (f.exists())
				return f;
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	private static File getPreferencesFileFromUserHome(String name) {
		try {
			String home = System.getProperty("user.home");
			File f = new File(home, name);
			if (f.exists())
				return f;
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

}
