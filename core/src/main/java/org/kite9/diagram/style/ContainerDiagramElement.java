package org.kite9.diagram.style;

import java.util.List;

import org.kite9.diagram.adl.Contained;
import org.kite9.diagram.adl.Container;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RectangleRenderingInformationImpl;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.xml.StyledXMLElement;
import org.kite9.diagram.xml.XMLElement;

public class ContainerDiagramElement extends AbstractXMLDiagramElement implements Container {
	
	private List<Contained> contents;
	
	@Override
	public List<Contained> getContents() {
		return contents;
	}
	
	public ContainerDiagramElement(StyledXMLElement el) {
		super(el);

		for (XMLElement xmlElement : el) {
			DiagramElement de = xmlElement.getDiagramElement();			
			if (de instanceof Contained) {
				contents.add((Contained) de);
			}
		}
	}


	@Override
	public Layout getLayoutDirection() {
		return null;
	}

	@Override
	public Label getLabel() {
		return null;
	}


	private RectangleRenderingInformation ri;
	
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
