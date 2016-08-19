package org.kite9.diagram.style;

import java.io.Serializable;

import org.kite9.diagram.adl.HintMap;
import org.kite9.diagram.adl.IdentifiableDiagramElement;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.xml.StyledXMLElement;

/**
 * Encapsulates an {@link StyledXMLElement} as a {@link DiagramElement}.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractXMLDiagramElement implements IdentifiableDiagramElement, Serializable {
	
	protected StyledXMLElement theElement;
	
	public AbstractXMLDiagramElement(StyledXMLElement el) {
		this.theElement = el;
	}

	public int compareTo(DiagramElement o) {
		if (o instanceof IdentifiableDiagramElement) {
			return getID().compareTo(((IdentifiableDiagramElement)o).getID());
		} else if (o!=null) {
			return this.toString().compareTo(o.toString());
		} else {
			return -1;
		}
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
	
}