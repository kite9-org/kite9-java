package org.kite9.tool.listener;


import org.kite9.framework.server.WorkItem;


/**
 * A Listener for build events, which are generated design items.
 */
public interface BuildListener {
	   
	public void process(WorkItem designItem) throws Exception;
	
	public boolean canProcess(WorkItem designItem);
	
	public void finished();
}