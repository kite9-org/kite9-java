package org.kite9.tool;

import java.util.List;

import org.kite9.tool.listener.BuildListener;
import org.kite9.tool.scanner.Scanner;

/**
 * Basic runner which can be configured by Spring.
 * 
 */
public class BasicKite9Runner extends AbstractKite9Runner {

	List<Scanner> scanners;
	List<BuildListener> listeners;

	public List<BuildListener> getListeners() {
		return listeners;
	}

	@Override
	protected List<Scanner> getScanners() {
		return scanners;
	}

	public void setScanners(List<Scanner> scanners) {
		this.scanners = scanners;
	}

	public void setListeners(List<BuildListener> listeners) {
		this.listeners = listeners;
	}

}
