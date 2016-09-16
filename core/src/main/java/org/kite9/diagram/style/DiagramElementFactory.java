package org.kite9.diagram.style;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.xml.StyledXMLElement;
import org.kite9.diagram.xml.XMLElement;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.serialization.CSSConstants;
import org.kite9.framework.serialization.EnumValue;

public class DiagramElementFactory {

	/**
	 * Produces the diagram element for the underlying XML.
	 */
	public static DiagramElement createDiagramElement(XMLElement in, DiagramElement parent) {
		if (in instanceof StyledXMLElement) {
			StyledXMLElement in2 = (StyledXMLElement) in;
			LayoutType lt = getElementType(in2);
			switch (lt) {
			case DIAGRAM:
				return new DiagramImpl(in2);
			case CONTAINER:
				return new ContainerImpl(in2, parent);
			case TEXT:
				return new TextImpl(in2, parent);
			case FIXED_SHAPE:
			case LINK:
				return new ConnectionImpl(in2);
			case LINK_END:
				return ((XMLElement) in.getParentNode()).getDiagramElement();
			case TERMINATOR:
				return new TerminatorImpl(in2, parent);
			case COMPOSED:
			case UNSPECIFIED:
			case NONE:
				return null;
			default:
				throw new Kite9ProcessingException("Not implemented yet");	
			}
			
		} else {
			throw new Kite9ProcessingException("Don't know how to create diagram element from "+in);
		}
	}

	public static LayoutType getElementType(StyledXMLElement in2) {
		EnumValue v = (EnumValue) in2.getCSSStyleProperty(CSSConstants.LAYOUT_TYPE_PROPERTY);
		LayoutType lt = (LayoutType) v.getTheValue();
		return lt;
	}
}
