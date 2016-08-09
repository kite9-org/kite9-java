package org.kite9.diagram.primitives;

import java.io.Serializable;

import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.position.BasicRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;

public abstract class AbstractIdentifiableDiagramElement extends AbstractStyledDiagramElement implements IdentifiableDiagramElement, Serializable {

	private static final long serialVersionUID = -3154895826494435557L;

	public AbstractIdentifiableDiagramElement() {
	}
	
	public AbstractIdentifiableDiagramElement(String id, String tag, ADLDocument doc) {
		super(tag, doc);
		
		if (id == null) {
			id = createID();
		}
		
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


	public BasicRenderingInformation getBasicRenderingInformation() {
		BasicRenderingInformation ri = getProperty("renderingInformation", BasicRenderingInformation.class);
		if (ri == null) {
			ri = (BasicRenderingInformation) ownerDocument.createElement("renderingInformation");
			setRenderingInformation(ri);
		}
		
		return ri;
	}

	public void setRenderingInformation(RenderingInformation ri) {
		replaceProperty("renderingInformation", ri, RenderingInformation.class);
	}


	@Override
	public int hashCode() {
		String id = getId();
		return id.hashCode();
	}
	
	public final String getID() {
		return getAttribute("id");
	}
	
	public void setID(String id) {
		setAttribute("id", id);
	}
	
	protected static synchronized String createID() {
		return AUTO_GENERATED_ID_PREFIX+counter++;
	}
	
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