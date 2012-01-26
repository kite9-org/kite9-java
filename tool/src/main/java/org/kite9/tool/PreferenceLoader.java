package org.kite9.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PreferenceLoader {

	public static Properties getPreferences(String fileName) throws FileNotFoundException, IOException {

		File out = getPreferencesFileFromUserHome(fileName);
		if (out == null)
			out = getPreferencesFileFromLocalPath(fileName);

		if (out == null) {
			out = getPreferencesFileFromClasspath(fileName);
		}

		if (out == null)
			throw new FileNotFoundException("Could not find default preferences: " + fileName);

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
