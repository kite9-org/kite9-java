package org.kite9.diagram.adl;

import java.io.Serializable;

import org.kite9.diagram.primitives.AbstractStyledDiagramElement;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.DiagramElement;
import org.w3c.dom.Node;

public class LinkTerminator  extends AbstractStyledDiagramElement implements Serializable, CompositionalDiagramElement {

	public LinkTerminator() {
		super();
	}

	public LinkTerminator(String name, ADLDocument owner) {
		super(name, owner);
	}
	
	public LinkTerminator(String name, ADLDocument owner, String shape) {
		super(name, owner);
		this.setTextContent(shape);
	}

	public int compareTo(DiagramElement o) {
		return 0;
	}

	public String getXMLId() {
		return null;
	}

	@Override
	protected Node newNode() {
		return new LinkTerminator();
	}

	@Override
	public String getCSSClass() {
		return super.getClasses() + " link-terminator " + getTextContent().toLowerCase();
	}

	
	
}
