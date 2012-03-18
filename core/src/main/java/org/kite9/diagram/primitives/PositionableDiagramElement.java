package org.kite9.diagram.primitives;

import org.kite9.diagram.position.RenderingInformation;


public interface PositionableDiagramElement extends DiagramElement {

	public RenderingInformation getRenderingInformation();
	
	public void setRenderingInformation(RenderingInformation ri);
}
