package org.kite9.diagram.adl;

import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.style.DiagramElement;


public interface PositionableDiagramElement extends DiagramElement {

	public RenderingInformation getRenderingInformation();
	
	public void setRenderingInformation(RenderingInformation ri);
	
	public HintMap getPositioningHints();
	
	public void setPositioningHints(HintMap hints);
}
