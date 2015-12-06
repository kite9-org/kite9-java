package org.kite9.framework.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.kite9.framework.common.Kite9ProcessingException;

/**
 * Handles serving of a set of work items and returning a zip stream.
 * 
 * @author moffatr
 * 
 */
public abstract class AbstractLocalServer implements ItemServer {

	public ZipInputStream serve(WorkItem item) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		serve(item, os);

		return new ZipInputStream(new ByteArrayInputStream(os.toByteArray()));
	}

	public void serve(WorkItem workItem, OutputStream os) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(os);

		try {
			processItem(workItem, zos);
		} catch (Kite9ProcessingException e) {
			ZipEntry ze = new ZipEntry(workItem.getSubjectId() + "/" + workItem.getName() + ".exception");
			zos.putNextEntry(ze);
			PrintStream ps = new PrintStream(zos, true);
			e.printStackTrace(ps);
		} finally {
			zos.close();
		}

	}

	/**
	 * Adds any files to the zip output stream that the request might need.
	 * @throws Kite9ProcessingException an exception to be returned to the client
	 * @throws IOException for any problem writing the stream
	 */
	protected abstract void processItem(WorkItem workItem, ZipOutputStream zos) throws IOException, Kite9ProcessingException;

	/**
	 * Call this method for each item to add to the zip stream.
	 */
	public void beginZipEntry(WorkItem workItem, String fileExtension, ZipOutputStream zos) throws IOException {
		ZipEntry ze = new ZipEntry(fileExtension);
		zos.putNextEntry(ze);
	}
}
