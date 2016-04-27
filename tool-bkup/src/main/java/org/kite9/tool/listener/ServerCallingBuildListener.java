package org.kite9.tool.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.common.RepositoryHelp;
import org.kite9.framework.repository.Repository;
import org.kite9.framework.serialization.XMLHelper;
import org.kite9.framework.server.Format;
import org.kite9.framework.server.ItemServer;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.AbstractContextualizable;

public class ServerCallingBuildListener extends AbstractContextualizable implements BuildListener {

	protected ItemServer server;
	
	int successful;
	int failed;

	public boolean canProcess(WorkItem designItem) {
		return true;
	}

	public void finished() {
	    getContext().getLogger().send("Server processed "+successful+" successful calls and "+failed+" failures out of "+(failed+successful));
	}

	public Repository<?> getRepository() {
		return getContext().getRepository();
	}

	private void processItem(WorkItem i) {
		try {
			getRepository().clear(i.getID());
			processItem(i, Format.PNG);
			processItem(i, Format.MAP);

		} catch (IOException e) {
			failed ++;
			throw new Kite9ProcessingException("Could not process server response: ",e);
		} finally {
			try {
				OutputStream fos = getRepository().store(i.getID(), "xml");
				XMLHelper xh = new XMLHelper();
				String xml = xh.toXML(i);
				OutputStreamWriter w = new OutputStreamWriter(fos);
				w.write(xml);
				w.close();
			} catch (IOException e) {
				throw new Kite9ProcessingException("Could not store request XML in local repository: ", e);
			}
		}
	}

	protected void processItem(WorkItem i, Format f) throws IOException {
		InputStream is = server.serve(i, getContext().getSecretKey(), getContext().getUserId(), f);
		OutputStream fos = getRepository().store(i.getID(), f.name());
		RepositoryHelp.streamCopy(is, fos, true);
	}

	public void process(WorkItem designItem) throws Exception {
		try {
			processItem(designItem);
		} catch (Exception e) {
			getContext().getLogger().error("Could not process work item: "+designItem.getID(), e);
		}
	}

	public ItemServer getServer() {
		return server;
	}

	public void setServer(ItemServer server) {
		this.server = server;
	}

}
