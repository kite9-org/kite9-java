package org.kite9.framework.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.kite9.framework.common.RepositoryHelp;

public class BasicFileRepository implements Repository<File> {

	public InputStream retrieve(String subjectId, String name, String type)
			throws IOException {
		return retrieve(getAddress(subjectId, name, type, false));
	}

	public OutputStream store(String subjectId, String name, String type)
			throws FileNotFoundException {
		return store(getAddress(subjectId, name, type, true));
	}

	
	public InputStream retrieve(File f)
			throws IOException {
		if (!f.exists()) {
			throw new IOException("File does not exist: " + f);
		}

		return new FileInputStream(f);
	}

	public OutputStream store(File f) throws FileNotFoundException {
		return new FileOutputStream(f);
	}

	public File getAddress(String id, String name, String extension,
			boolean createDir) {
		return RepositoryHelp.prepareFileName(id, name == null ? null : name
				+ "." + extension, getBaseDir(), createDir);
	}

	protected String getBaseDir() {
		return baseDir;
	}

	private String baseDir = "target/kite9-repo";

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public void clear(String subjectId, final String name) throws IOException {
		File f = getAddress(subjectId, null, null, false);
		File[] matches = f.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name1) {
				return (name1.startsWith(name));
			}

		});

		if (matches != null)
			for (File file : matches) {
				file.delete();
			}

	}

}
