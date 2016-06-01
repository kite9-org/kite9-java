package org.kite9.diagram.primitives;

import java.io.Serializable;

import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.style.StyledDiagramElement;

public abstract class AbstractIdentifiableDiagramElement extends AbstractDiagramElement implements IdentifiableDiagramElement, Serializable, StyledDiagramElement {

	private static final long serialVersionUID = -3154895826494435557L;

	public AbstractIdentifiableDiagramElement() {
	}
	
	public AbstractIdentifiableDiagramElement(String id, String tag, ADLDocument doc) {
		super(tag, doc);
		setID(id);
	}

	public int compareTo(DiagramElement o) {
		if (o instanceof IdentifiableDiagramElement) {
			return getId().compareTo(((IdentifiableDiagramElement)o).getID());
		} else if (o!=null) {
			return this.toString().compareTo(o.toString());
		} else {
			return -1;
		}
	}


	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	public final String getID() {
		return getAttribute("id");
	}
	
	public void setID(String id) {
		setAttribute("id", id);
	}

	/**
	 * This is used by layout engines to set the position of the elements in the
	 * diagram
	 */
	protected RenderingInformation renderingInformation = null;
	
	private static int counter = 0; 
	
	protected static synchronized String createID() {
		return AUTO_GENERATED_ID_PREFIX+counter++;
	}
	
	public static final String AUTO_GENERATED_ID_PREFIX = "auto:";


	public static void resetCounter() {
		counter = 0;
	}

	protected HintMap hints;


	public HintMap getPositioningHints() {
		return hints;
	}

	public void setPositioningHints(HintMap hints) {
		this.hints = hints;
	}

	public String getXMLId() {
		return getId();
	}
	
}