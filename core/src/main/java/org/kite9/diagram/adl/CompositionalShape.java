package org.kite9.diagram.adl;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractIdentifiableDiagramElement;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.style.StyledDiagramElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("comp-shape")
public class CompositionalShape extends AbstractIdentifiableDiagramElement implements CompositionalDiagramElement, StyledDiagramElement {

	private static final long serialVersionUID = 5343674853338333434L;
	@XStreamOmitField
	Object parent;
	@XStreamAsAttribute
	String shape;
	
	public CompositionalShape(String id, String shape) {
		super(id);
		this.shape = shape;
	}
	
	public CompositionalShape(String shape) {
		super(createID());
	}

	public CompositionalShape() {
	}

	public int compareTo(DiagramElement arg0) {
		return 0;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object de) {
		this.parent = de;
	}

	public String getShapeName() {
		return this.shape;
	}

	public void setShapeName(String name) {
		this.shape = name;
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

}
