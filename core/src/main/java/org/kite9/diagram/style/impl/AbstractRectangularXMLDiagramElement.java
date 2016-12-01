package org.kite9.diagram.style.impl;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RectangleRenderingInformationImpl;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.xml.StyledXMLElement;

public abstract class AbstractRectangularXMLDiagramElement extends AbstractXMLDiagramElement {

	private RectangleRenderingInformation ri;

	public AbstractRectangularXMLDiagramElement(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public RectangleRenderingInformation getRenderingInformation() {
		if (ri == null) {
			ri = new RectangleRenderingInformationImpl();
		}
		
		return ri;
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.ri = (RectangleRenderingInformation) ri;
	}

}