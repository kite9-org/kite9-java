package org.kite9.diagram.adl;

import org.apache.batik.css.engine.value.Value;
import org.kite9.diagram.position.RenderingInformation;

public class LinkTerminator implements DiagramElement {

	public LinkTerminator() {
		super();
	}

	public LinkTerminator(String shape) {
		this.shapeName = shape;
	}
	
	private String shapeName;

	@Override
	public String getShapeName() {
		return shapeName;
	}

	@Override
	public int compareTo(DiagramElement o) {
		return 0;
	}

	@Override
	public DiagramElement getParent() {
		return null;
	}

	@Override
	public String getID() {
		return null;
	}

	@Override
	public RenderingInformation getRenderingInformation() {
		return null;
	}

	@Override
	public Value getCSSStyleProperty(String prop) {
		return null;
	}

	@Override
	public void setRenderingInformation(RenderingInformation ri) {
	}

	@Override
	public HintMap getPositioningHints() {
		return null;
	}

	@Override
	public void setPositioningHints(HintMap hints) {
	}
		
}
