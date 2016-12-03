package org.kite9.diagram.adl;

/**
 * DiagramElement to contain a label for an edge, container or diagram.
 */
public interface Label extends DiagramElement {
	
	public boolean hasContent();
	
	public boolean isConnectionLabel();
}
