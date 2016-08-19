package org.kite9.diagram.style;

import org.kite9.diagram.adl.Leaf;
import org.kite9.diagram.adl.TextContainingDiagramElement;
import org.kite9.diagram.common.Connected;
import org.kite9.diagram.xml.StyledXMLElement;

public class TextDiagramElement extends AbstractStyledConnectedXMLDiagramElement implements Leaf, TextContainingDiagramElement, Connected {

	public TextDiagramElement(StyledXMLElement el) {
		super(el);
	}

	@Override
	public String getText() {
		return theElement.getTextContent();
	}

}
