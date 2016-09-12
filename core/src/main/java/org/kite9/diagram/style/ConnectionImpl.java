package org.kite9.diagram.style;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.adl.Terminator;
import org.kite9.diagram.common.BiDirectional;
import org.kite9.diagram.common.Connected;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.position.RouteRenderingInformation;
import org.kite9.diagram.position.RouteRenderingInformationImpl;
import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.LinkLineStyle;
import org.kite9.diagram.xml.StyledXMLElement;
import org.kite9.diagram.xml.XMLElement;
import org.kite9.framework.common.Kite9ProcessingException;
import org.w3c.dom.Element;

public class ConnectionImpl extends AbstractXMLDiagramElement implements Connection {

	public ConnectionImpl(StyledXMLElement el) {
		super(el, null);
	}
	
	
	@Override
	protected void initialize() {
		System.out.println("Creating for "+theElement.getID());
		
		XMLElement fromElement = getFromElement();
		XMLElement toElement = getToElement();

		ADLDocument owner = theElement.getOwnerDocument();
		owner.addConnectionReference(fromElement.getID(), theElement);
		owner.addConnectionReference(toElement.getID(), theElement);
		
		from = (Connected) fromElement.getDiagramElement();
		to = (Connected) toElement.getDiagramElement();
		drawDirection = Direction.getDirection(theElement.getAttribute("direction"));
		
		XMLElement fromDecorationEl = theElement.getProperty("fromDecoration");
		this.fromDecoration = getTerminator(fromDecorationEl);
		
		XMLElement toDecorationEl = theElement.getProperty("toDecoration");
		this.toDecoration = getTerminator(toDecorationEl);
	}


	private Terminator getTerminator(XMLElement el) {
		if (el == null) {
			el = (XMLElement) theElement.getOwnerDocument().createElement("terminator");
			theElement.appendChild(el);
		}
		return (Terminator) el.getDiagramElement();
	}


	private XMLElement getFromElement() {
		Element fromEl = theElement.getProperty("from");
		String reference = fromEl.getAttribute("reference");
		ADLDocument owner = theElement.getOwnerDocument();
		XMLElement from = (XMLElement) owner.getChildElementById(owner, reference);
		return from;
	}

	private XMLElement getToElement() {
		Element toEl = theElement.getProperty("to");
		String reference = toEl.getAttribute("reference");
		ADLDocument owner = theElement.getOwnerDocument();
		XMLElement to = (XMLElement) owner.getChildElementById(owner, reference);
		return to;
	}
	
	private Connected from;
	private Connected to;
	private Direction drawDirection;
	private Terminator fromDecoration;
	private Terminator toDecoration;
	

	@Override
	public Connected getFrom() {
		ensureInitialized();
		return from;
	}

	@Override
	public Connected getTo() {
		ensureInitialized();
		return to;
	}

	@Override
	public void setFrom(Connected v) {
		System.out.println("Setting value in something that should be immutable");
		this.from = v;
	}

	@Override
	public void setTo(Connected v) {
		System.out.println("Setting value in something that should be immutable");
		this.to = v;
	}

	@Override
	public Connected otherEnd(Connected end) {
		if (end == getFrom()) {
			return getTo();
		} else if (end == getTo()) {
			return getFrom();
		} else {
			throw new Kite9ProcessingException("otherEnd of neither from or to "+this+" "+end);
		}
	}

	@Override
	public boolean meets(BiDirectional<Connected> e) {
		return meets(e.getFrom()) || meets(e.getTo());
	}

	@Override
	public boolean meets(Connected v) {
		return (getFrom()==v) || (getTo()==v);
	}

	@Override
	public Direction getDrawDirection() {
		return drawDirection;
	}

	@Override
	public Direction getDrawDirectionFrom(Connected from) {
		if (getFrom() == from) {
			return getDrawDirection();
		} else {
			return Direction.reverse(getDrawDirection());
		}
	}

	@Override
	public void setDrawDirectionFrom(Direction d, Connected from) {
		throw new Kite9ProcessingException("Should be immutable");
	}
	
	@Override
	public void setDrawDirection(Direction d) {
		throw new Kite9ProcessingException("Should be immutable");
	}

	@Override
	public Terminator getFromDecoration() {
		ensureInitialized();
		return fromDecoration;
	}

	@Override
	public Terminator getToDecoration() {
		ensureInitialized();
		return toDecoration;
	}

	@Override
	public Label getFromLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getToLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	private RouteRenderingInformation ri;
	
	@Override
	public RouteRenderingInformation getRenderingInformation() {
		if (ri == null) {
			ri = new RouteRenderingInformationImpl();
		}
		
		return ri;
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.ri = (RouteRenderingInformation) ri;
	}

	@Override
	public String getStyle() {
		return LinkLineStyle.NORMAL;
	}

	@Override
	public int getRank() {
		// TODO Auto-generated method stub
		return 0;
	}

}
