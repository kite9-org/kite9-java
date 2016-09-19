package org.kite9.diagram.style;

import java.io.Serializable;

import org.apache.batik.css.engine.value.Value;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.HintMap;
import org.kite9.diagram.xml.StyledXMLElement;

/**
 * Encapsulates an {@link StyledXMLElement} as a {@link DiagramElement}.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractXMLDiagramElement extends AbstractDiagramElement implements DiagramElement, Serializable {
	
	protected StyledXMLElement theElement;
	
	private boolean initialized = false;

	protected abstract void initialize();
	
	protected void ensureInitialized() {
		if (!initialized) {
			initialize();
			this.initialized = true;
		}
	}

	
	@Deprecated
	public StyledXMLElement getTheElement() {
		return theElement;
	}

	public AbstractXMLDiagramElement(StyledXMLElement el, DiagramElement parent) {
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
		return theElement.getAttribute("shape");
	}

	public Diagram getDiagram() {
		return theElement.getOwnerDocument().getDocumentElement().getDiagramElement();
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