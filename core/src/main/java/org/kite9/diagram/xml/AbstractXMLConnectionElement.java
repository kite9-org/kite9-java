package org.kite9.diagram.xml;

import org.kite9.diagram.adl.Connected;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.common.BiDirectional;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.RouteRenderingInformation;
import org.kite9.framework.logging.LogicException;
import org.w3c.dom.Element;

/**
 * This is the base class for connections within the diagram.  i.e. Links.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractXMLConnectionElement extends AbstractStyleableXMLElement {

	private static final long serialVersionUID = -1941426216200603569L;
	
	/**
	 * For serialization
	 */
	public AbstractXMLConnectionElement() {
	}
	
	/**
	 * Call this with modify verteConnected false to avoid adding the edge connection to the vertex
	 */
	public AbstractXMLConnectionElement(String id, String tag, XMLElement from, XMLElement to, Direction drawDirection, String fromDecoration, XMLElement fromLabel, String toDecoration, XMLElement tolabel, ADLDocument doc) {
		super(id, tag, doc);
		setFrom(from);
		setTo(to);
		setDrawDirection(drawDirection);
		
		if (fromDecoration != null) {
			setFromDecoration(new LinkTerminator("fromDecoration", this.getOwnerDocument(), fromDecoration));
		}

		if (toDecoration != null) {
			setToDecoration(new LinkTerminator("toDecoration", this.getOwnerDocument(), toDecoration));
		}
		
		if (fromLabel!=null) {
			setFromLabel(fromLabel);
		}
		
		if (tolabel!=null) { 
			setToLabel(tolabel);
		}
	}
	
	public abstract LinkTerminator getFromDecoration();

	public abstract LinkTerminator getToDecoration();

	public Label getFromLabel() {
		return getProperty("fromLabel", Label.class);
	}

	public Label getToLabel() {
		return getProperty("toLabel", Label.class);
	}

	public int compareTo(DiagramElement o) {
		if (o!=null) {
			return this.toString().compareTo(o.toString());
		} else {
			return -1;
		}
	}
	
	public RouteRenderingInformation getRenderingInformation() {
		return getBasicRenderingInformation();
	}

	public abstract void setFromDecoration(LinkTerminator fromDecoration);

	public abstract void setToDecoration(LinkTerminator toDecoration);

	public void setFromLabel(XMLElement fromLabel) {
	    replaceProperty("fromLabel", fromLabel, XMLElement.class);
	}

	public void setToLabel(XMLElement toLabel) {
	    replaceProperty("toLabel", toLabel, XMLElement.class);
	}
	
	public Direction getDrawDirection() {
		String dd = getAttribute("drawDirection");
		if (dd.length() == 0) {
			return null;
		}
		return Direction.valueOf(dd);
	}
	
	public Connected getFrom() {
		Element fromEl = getProperty("from", Element.class);
		String reference = fromEl.getAttribute("reference");
		Connected from = (Connected) ownerDocument.getChildElementById(ownerDocument, reference);
		return from;
	}

	public Connected getTo() {
		Element toEl = getProperty("to", Element.class);
		String reference = toEl.getAttribute("reference");
		Connected to = (Connected) ownerDocument.getChildElementById(ownerDocument, reference);
		return to;
	}

	public void setFrom(XMLElement v) {
		Element from = ownerDocument.createElement("from");
		from.setAttribute("reference", v.getID());
		replaceProperty("from", from, Element.class);
		from = v;
	}

	public void setTo(XMLElement v) {
		Element to = ownerDocument.createElement("to");
		to.setAttribute("reference", v.getID());
		replaceProperty("to", to, Element.class);
		to = v;
	}
	
	public Connected otherEnd(Connected end) {
		Connected from = getFrom();
		Connected to = getTo();
		if (end == from)
			return to;
		if (end == to)
			return from;
		throw new LogicException("This is not an end: " + end + " of " + this);
	}

	public boolean meets(BiDirectional<Connected> e) {
		return getFrom().equals(e.getTo()) || getFrom().equals(e.getFrom())
				|| getTo().equals(e.getTo()) || getTo().equals(e.getFrom());
	}

	public boolean meets(Connected v) {
		return getFrom().equals(v) || getTo().equals(v);
	}

	public Direction getDrawDirectionFrom(Connected end) {
		if (getDrawDirection() == null)
			return null;

		if (end.equals(getFrom())) {
			return getDrawDirection();
		}

		if (end.equals(getTo())) {
			return Direction.reverse(getDrawDirection());
		}

		throw new RuntimeException(
				"Trying to get direction from an end that's not set: " + end
						+ " in " + this);
	}

	public void setDrawDirection(Direction d) {
		if (d == null) {
			removeAttribute("drawDirection");
		} else {
			setAttribute("drawDirection", d.toString());
		}
	}

	public void setDrawDirectionFrom(Direction d, Connected end) {

		if (end.equals(getFrom())) {
			setDrawDirection(d);
		} else if (end.equals(getTo())) {
			setDrawDirection(Direction.reverse(d));
		} else {
			throw new RuntimeException(
					"Trying to set direction from an end that's not set: " + end
							+ " in " + this);
		}
	}
}