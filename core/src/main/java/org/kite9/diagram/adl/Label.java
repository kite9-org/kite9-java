package org.kite9.diagram.adl;

import org.kite9.diagram.position.RectangleRenderingInformation;

/**
 * DiagramElement to contain a label an edge, container or diagram.
 * 
 */
public interface Label extends DiagramElement {
	
	public boolean hasContent();
	
	public RectangleRenderingInformation getRenderingInformation();
		
}
