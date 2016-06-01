package org.kite9.diagram.adl;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractIdentifiableDiagramElement;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.DiagramElement;
import org.w3c.dom.Node;

public class CompositionalShape extends AbstractIdentifiableDiagramElement implements CompositionalDiagramElement {

	private static final long serialVersionUID = 5343674853338333434L;
	
	public CompositionalShape(String id, ADLDocument doc) {
		super(id, "comp-shape", doc);
	}
	
	public CompositionalShape() {
		this.tagName = "comp-shape";
	}

	public int compareTo(DiagramElement arg0) {
		return 0;
	}

	public RenderingInformation getRenderingInformation() {
		if (renderingInformation == null) {
			renderingInformation = new RectangleRenderingInformation();
		}
		
		return renderingInformation;
	}
	

	public void setRenderingInformation(RenderingInformation ri) {
		this.renderingInformation = ri;
	}

	@Override
	protected Node newNode() {
		return new CompositionalShape();
	}

}
