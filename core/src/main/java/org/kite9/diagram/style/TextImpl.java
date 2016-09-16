package org.kite9.diagram.style;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Leaf;
import org.kite9.diagram.adl.Text;
import org.kite9.diagram.common.Connected;
import org.kite9.diagram.xml.StyledXMLElement;

public class TextImpl extends AbstractConnectedXMLDiagramElement implements Leaf, Text, Connected {

	public TextImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public String getText() {
		// to support old xml format.
		String label = theElement.getAttribute("label");
		String stereotype = theElement.getAttribute("stereotype");
		
		return label+stereotype+theElement.getTextContent();
	}

}
