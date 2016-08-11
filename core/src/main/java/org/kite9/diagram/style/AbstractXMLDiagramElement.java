package org.kite9.diagram.style;

import java.io.Serializable;

import org.kite9.diagram.adl.HintMap;
import org.kite9.diagram.adl.IdentifiableDiagramElement;
import org.kite9.diagram.position.BasicRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.xml.XMLElement;

/**
 * Encapsulates an {@link XMLElement} as a {@link DiagramElement}.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractXMLDiagramElement implements IdentifiableDiagramElement, Serializable {

	private static final long serialVersionUID = -3154895826494435557L;

	public AbstractXMLDiagramElement() {
	}
	
	private XMLElement theElement;
	
	public AbstractXMLDiagramElement(XMLElement el) {
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

	private RenderingInformation ri;
	

	@Override
	public RenderingInformation getRenderingInformation() {
		if (ri == null) {
			ri = new BasicRenderingInformation();
		}
		
		return ri;
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.ri = ri;
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