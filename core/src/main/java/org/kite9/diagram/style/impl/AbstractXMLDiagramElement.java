package org.kite9.diagram.style.impl;

import java.io.Serializable;

import org.apache.batik.css.engine.value.Value;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.common.HintMap;
import org.kite9.diagram.xml.StyledKite9SVGElement;
import org.kite9.framework.serialization.CSSConstants;

/**
 * Encapsulates an {@link StyledKite9SVGElement} as a {@link DiagramElement}.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractXMLDiagramElement extends AbstractDiagramElement implements DiagramElement, Serializable {
	
	protected StyledKite9SVGElement theElement;
	
	private boolean initialized = false;

	protected abstract void initialize();
	
	protected void ensureInitialized() {
		if (!initialized) {
			if (parent instanceof AbstractXMLDiagramElement) {
				((AbstractXMLDiagramElement)parent).ensureInitialized();
			}
			initialize();
			this.initialized = true;
		}
	}

	
	@Deprecated
	public StyledKite9SVGElement getTheElement() {
		return theElement;
	}

	public AbstractXMLDiagramElement(StyledKite9SVGElement el, DiagramElement parent) {
		super(parent);
		this.theElement = el;
	}

	@Override
	public Value getCSSStyleProperty(String prop) {
		return theElement.getCSSStyleProperty(prop);
	}
	
	@Override
	public String getID() {
		return theElement.getID();
	}

	public String getShapeName() {
		String out = theElement.getAttribute("shape");
//		if (out.length() == 0) {
//			out = getCSSStyleProperty(CSSConstants.SHAPE_PROPERTY).getStringValue();
//		}
		
		return out;
	}

	public Diagram getDiagram() {
		if (this instanceof Diagram) {
			return (Diagram) this;
		} else {
			return ((AbstractXMLDiagramElement)getParent()).getDiagram();
		}
	}

	@Override
	public String toString() {
		String className = this.getClass().getName();
		className = className.substring(className.lastIndexOf(".")+1);
		return "["+theElement.getTagName()+":'"+getID()+"':"+className+"]";
	}

	@Override
	public HintMap getPositioningHints() {
		return null;
	}
	
}