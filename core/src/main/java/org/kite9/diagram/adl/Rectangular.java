package org.kite9.diagram.adl;

import org.kite9.diagram.position.RectangleRenderingInformation;

/**
 * Marker interface for diagram elements which consume a rectangular space, and therefore
 * return {@link RectangleRenderingInformation}.
 * 
 * @author robmoffat
 *
 */
public interface Rectangular extends DiagramElement {

	public RectangleRenderingInformation getRenderingInformation();
	
}
