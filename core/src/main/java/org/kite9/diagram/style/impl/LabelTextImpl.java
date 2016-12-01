package org.kite9.diagram.style.impl;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.adl.Text;
import org.kite9.diagram.xml.StyledXMLElement;

public class LabelTextImpl extends AbstractRectangularXMLDiagramElement implements Text, Label {

	public LabelTextImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public String getText() {
		// to support old xml format.
		String label = theElement.getAttribute("label");
		String stereotype = theElement.getAttribute("stereotype");
		
		return label+stereotype+theElement.getTextContent();
	}

	@Override
	public boolean hasContent() {
		return !getText().isEmpty();
	}
	
	@Override
	public boolean isConnectionLabel() {
		return getParent() instanceof Connection;
	}

	@Override
	protected void initialize() {
	}
}
