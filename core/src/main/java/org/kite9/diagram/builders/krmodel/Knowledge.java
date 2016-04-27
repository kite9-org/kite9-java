package org.kite9.diagram.builders.krmodel;

import org.kite9.diagram.builders.id.HasAddress;

/**
 * Indicates that the knowledge element has a human-readable label.
 * 
 * @author robmoffat
 *
 */
public interface Knowledge extends HasAddress {

	public String getLabel();
	
}
