package org.kite9.diagram.adl;

/**
 * DiagramElement to contain a label an edge, container or diagram.
 * 
 */
public interface Label extends DiagramElement {
	
	public boolean hasContent();
	
	/**
	 * Allows you to say where the label connects, but shouldn't be concern of D.e.
	 * @param o
	 */
	@Deprecated
	public void setParent(Object o);
		
}
