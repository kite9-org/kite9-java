package org.kite9.diagram.primitives;

import org.kite9.diagram.position.RectangleRenderingInformation;

/**
 * DiagramElement to contain a label an edge, container or diagram.
 * 
 */
public interface Label extends IdentifiableDiagramElement, CompositionalDiagramElement {
	
	public boolean hasContent();
	
	public RectangleRenderingInformation getRenderingInformation();
	
	public DiagramElement getOwner();
	
}
