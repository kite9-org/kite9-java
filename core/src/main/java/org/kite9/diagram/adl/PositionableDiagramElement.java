package org.kite9.diagram.adl;

import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.xml.XMLElement;


public interface PositionableDiagramElement extends XMLElement {

	public RenderingInformation getRenderingInformation();
	
	public void setRenderingInformation(RenderingInformation ri);
	
	public HintMap getPositioningHints();
	
	public void setPositioningHints(HintMap hints);
}
