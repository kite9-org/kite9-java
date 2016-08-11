package org.kite9.diagram.adl;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.style.DiagramElement;

/**
 * DiagramElement to contain a label an edge, container or diagram.
 * 
 */
public interface Label extends IdentifiableDiagramElement, CompositionalDiagramElement {
	
	public boolean hasContent();
	
	public RectangleRenderingInformation getRenderingInformation();
	
	public DiagramElement getOwner();
	
}
