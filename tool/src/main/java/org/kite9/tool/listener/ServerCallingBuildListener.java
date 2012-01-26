package org.kite9.tool.listener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.common.RepositoryHelp;
import org.kite9.framework.repository.Repository;
import org.kite9.framework.serialization.XMLHelper;
import org.kite9.framework.server.ItemServer;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.AbstractContextualizable;

public class ServerCallingBuildListener extends AbstractContextualizable implements BuildListener {

	protected ItemServer server;
	
	int successful;
	int failed;
	int total;

	public boolean canProcess(WorkItem designItem) {
		return true;
	}

	public void finished() {
	    getContext().getLogger().send("Server processed "+successful+" successful calls and "+failed+" failures out of "+total);
	}

	public Repository getRepository() {
		return getContext().getRepository();
	}

	private void processItem(WorkItem i) {
		total++;

		try {
			getRepository().clear(i.getSubjectId(), i.getName());
			ZipInputStream zis = server.serve(i);
			ZipEntry next;

			while ((next = zis.getNextEntry()) != null) {
				String name = next.getName();
				int lastDot = name.lastIndexOf(".");
				int firstSlash = name.indexOf("/");
				String id = name.substring(0, firstSlash);
				String np = name.substring(firstSlash + 1, lastDot);
				String ext = name.substring(lastDot + 1);

				OutputStream fos = getRepository().store(id, np, ext);
				RepositoryHelp.streamCopy(zis, fos, true);

				if (ext.equals("exception")) {
					InputStream is = getRepository().retrieve(id, np, ext);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					RepositoryHelp.streamCopy(is, baos, true);
					String problemText = baos.toString();
					// an exception was thrown - throw it up.
					throw new Kite9ProcessingException("An exception occurred in processing the work item:  "+i.getSubjectId()+" "+i.getName()+"\n"+problemText);
				}
				
				successful++;
			}
		} catch (IOException e) {
			failed ++;
			throw new Kite9ProcessingException("Could not process server response: " + e);
		} finally {
			try {
				OutputStream fos = getRepository().store(i.getSubjectId(), i.getName(), "xml");
				XMLHelper xh = new XMLHelper();
				String xml = xh.toXML(i);
				OutputStreamWriter w = new OutputStreamWriter(fos);
				w.write(xml);
				w.close();
			} catch (IOException e) {
				throw new Kite9ProcessingException("Could not store request XML in local repository: " + e);
			}
		}
	}

	public void process(WorkItem designItem) throws Exception {
		try {
			processItem(designItem);
		} catch (Exception e) {
			getContext().getLogger().error("Could not process work item: "+designItem.getSubjectId()+" "+designItem.getName(), e);
		}
	}

	public ItemServer getServer() {
		return server;
	}

	public void setServer(ItemServer server) {
		this.server = server;
	}

}
