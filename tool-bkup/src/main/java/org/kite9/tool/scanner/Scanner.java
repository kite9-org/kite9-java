package org.kite9.tool.scanner;

import java.util.List;

import org.kite9.framework.server.WorkItem;

/**
 * A scanner can scan a project to produce a set of work items to process.
 * 
 * @author moffatr
 * 
 */
public interface Scanner {

    public List<WorkItem> getItems();
}
