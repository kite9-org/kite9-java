package org.kite9.diagram.primitives;

import org.kite9.diagram.adl.XMLElement;
import org.kite9.diagram.position.RenderingInformation;


public interface PositionableDiagramElement extends XMLElement {

	public RenderingInformation getRenderingInformation();
	
	public void setRenderingInformation(RenderingInformation ri);
	
	public HintMap getPositioningHints();
	
	public void setPositioningHints(HintMap hints);
}
