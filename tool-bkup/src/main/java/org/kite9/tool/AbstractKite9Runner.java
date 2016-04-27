package org.kite9.tool;

import java.util.ArrayList;
import java.util.List;

import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.AbstractContextualizable;
import org.kite9.tool.listener.BuildListener;
import org.kite9.tool.scanner.Scanner;

/**
 * Todo: add exception processing code
 * @author moffatr
 * 
 */
public abstract class AbstractKite9Runner extends AbstractContextualizable implements Kite9Runner {

	public AbstractKite9Runner() {
		super();
	}

	protected abstract List<Scanner> getScanners();

	protected abstract List<BuildListener> getListeners();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kite9.framework.runner.Kite9Runner#process()
	 */
	public void process() {

		List<WorkItem> allItems = new ArrayList<WorkItem>(1000);

		for (Scanner s : getScanners()) {
			List<WorkItem> items = s.getItems();
			allItems.addAll(items);
		}

		for (WorkItem item : allItems) {

			for (BuildListener buildListener : getListeners()) {
				if (buildListener.canProcess(item)) {
					try {
						buildListener.process(item);
					} catch (Exception e) {
						getContext().getLogger().error(
								"Error processing design item: " + item.getID() + " with listener "
										+ buildListener.getClass().getName(), e);
					}
				}
			}
		}

		for (BuildListener buildListener : getListeners()) {
			buildListener.finished();
		}
	}

}