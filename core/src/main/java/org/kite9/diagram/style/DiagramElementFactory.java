package org.kite9.diagram.style;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.style.impl.ConnectionImpl;
import org.kite9.diagram.style.impl.ConnectedContainerImpl;
import org.kite9.diagram.style.impl.DiagramImpl;
import org.kite9.diagram.style.impl.LabelContainerImpl;
import org.kite9.diagram.style.impl.LabelTextImpl;
import org.kite9.diagram.style.impl.TerminatorImpl;
import org.kite9.diagram.style.impl.ConnectedTextImpl;
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
			DiagramElementType lt = getElementType(in2);
			switch (lt) {
			case DIAGRAM:
				if (parent != null) {
					throw new Kite9ProcessingException("Can't nest type 'diagram' @ "+in.getID());
				}
				return new DiagramImpl(in2);
			case LABEL:
				DiagramElementSizing sizing = getElementSizing(in2);
				switch (sizing) {
					case CONTAINER:
						return new LabelContainerImpl(in2, parent);
					case DECAL:
						throw new UnsupportedOperationException();
					case FIXED_SIZE:
						throw new UnsupportedOperationException();
					case TEXT:
					case UNSPECIFIED:
					default:
						return new LabelTextImpl(in2, parent);
				}
			case CONNECTED:
				if (parent instanceof Label) {
					throw new Kite9ProcessingException("Labels cannot contain connected elements");
				} else {
					sizing = getElementSizing(in2);
					switch(sizing) {
					case CONTAINER:
						return new ConnectedContainerImpl(in2, parent);
					case DECAL:
						throw new UnsupportedOperationException();
					case FIXED_SIZE:
						throw new UnsupportedOperationException();
					case TEXT:
					case UNSPECIFIED:
					default:
						return new ConnectedTextImpl(in2, parent);	
					}
				}
			case LINK:
				return new ConnectionImpl(in2);
			case LINK_END:
				return ((XMLElement) in.getParentNode()).getDiagramElement();
			case TERMINATOR:
				return new TerminatorImpl(in2, parent);
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

	private static DiagramElementType getElementType(StyledXMLElement in2) {
		EnumValue v = (EnumValue) in2.getCSSStyleProperty(CSSConstants.ELEMENT_TYPE_PROPERTY);
		DiagramElementType lt = (DiagramElementType) v.getTheValue();
		return lt;
	}
	
	private static DiagramElementSizing getElementSizing(StyledXMLElement in2) {
		EnumValue v = (EnumValue) in2.getCSSStyleProperty(CSSConstants.ELEMENT_SIZING_PROPERTY);
		DiagramElementSizing lt = (DiagramElementSizing) v.getTheValue();
		return lt;
	}
}
