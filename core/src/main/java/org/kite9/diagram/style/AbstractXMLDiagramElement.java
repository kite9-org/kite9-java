package org.kite9.diagram.style;

import java.io.Serializable;

import org.apache.batik.css.engine.value.Value;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.HintMap;
import org.kite9.diagram.xml.StyledXMLElement;

/**
 * Encapsulates an {@link StyledXMLElement} as a {@link DiagramElement}.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractXMLDiagramElement implements DiagramElement, Serializable {
	
	protected StyledXMLElement theElement;
	private DiagramElement parent;
	
	public AbstractXMLDiagramElement(StyledXMLElement el, DiagramElement parent) {
		this.theElement = el;
		this.parent = parent;
	}

	public int compareTo(DiagramElement o) {
		return getID().compareTo(o.getID());
	}
	
	@Override
	public int hashCode() {
		String id = getID();
		return id.hashCode();
	}

	protected HintMap hints;


	public HintMap getPositioningHints() {
		return hints;
	}

	public void setPositioningHints(HintMap hints) {
		this.hints = hints;
	}
	
	@Override
	public String getID() {
		return theElement.getID();
	}
	
	public String getShapeName() {
		return theElement.getAttribute("shape");
	}

	@Override
	public Value getCSSStyleProperty(String prop) {
		return theElement.getCSSStyleProperty(prop);
	}

	@Override
	public DiagramElement getParent() {
		return parent;
	}
	
}