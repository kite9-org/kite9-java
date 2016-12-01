package org.kite9.diagram.adl;

/**
 * DiagramElement to contain a label an edge, container or diagram.
 * 
 */
public interface Label extends DiagramElement, Rectangular {
	
	public boolean hasContent();
		
	@Deprecated
	public String getText();		// this won't be here later
	
	public boolean isConnectionLabel();
}
