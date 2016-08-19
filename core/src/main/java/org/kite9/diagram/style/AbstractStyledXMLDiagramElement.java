package org.kite9.diagram.style;

import org.apache.batik.css.engine.StyleDeclarationProvider;
import org.apache.batik.css.engine.value.Value;
import org.kite9.diagram.adl.StyledDiagramElement;
import org.kite9.diagram.xml.StyledXMLElement;

/**
 * Adds implementation for {@link StyledDiagramElement}.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractStyledXMLDiagramElement extends AbstractXMLDiagramElement implements StyledDiagramElement {

	public AbstractStyledXMLDiagramElement(StyledXMLElement el) {
		super(el);
	}

	public StyleDeclarationProvider getOverrideStyleDeclarationProvider() {
		return null;
	}
	
	public String getShapeName() {
		return theElement.getAttribute("shape");
	}

	@Override
	public Value getCSSStyleProperty(String prop) {
		return ((StyledDiagramElement)theElement).getCSSStyleProperty(prop);
	}
	
}
