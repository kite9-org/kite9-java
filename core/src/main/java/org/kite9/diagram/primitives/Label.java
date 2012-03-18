package org.kite9.diagram.primitives;


/**
 * DiagramElement to contain a label an edge, container or diagram.
 * 
 */
public interface Label extends IdentifiableDiagramElement, CompositionalDiagramElement {
	
	public boolean hasContent();
	
}
