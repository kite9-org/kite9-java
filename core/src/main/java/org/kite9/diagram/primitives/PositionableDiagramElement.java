package org.kite9.diagram.primitives;

import org.kite9.diagram.position.RenderingInformation;


public interface PositionableDiagramElement extends XMLDiagramElement {

	public RenderingInformation getRenderingInformation();
	
	public void setRenderingInformation(RenderingInformation ri);
	
	public HintMap getPositioningHints();
	
	public void setPositioningHints(HintMap hints);
}
