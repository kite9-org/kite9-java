package org.kite9.diagram.style;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.adl.LinkTerminator;
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
import org.w3c.dom.Element;

public class ConnectionImpl extends AbstractXMLDiagramElement implements Connection {

	public ConnectionImpl(StyledXMLElement el) {
		super(el, null);
		
		XMLElement fromElement = getFromElement();
		XMLElement toElement = getToElement();

		ADLDocument owner = theElement.getOwnerDocument();
		owner.addReference(fromElement.getID(), el);
		owner.addReference(toElement.getID(), el);
		
		from = (Connected) fromElement.getDiagramElement();
		to = (Connected) toElement.getDiagramElement();
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

	@Override
	public Connected getFrom() {
		return from;
	}

	@Override
	public Connected getTo() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean meets(BiDirectional<Connected> e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean meets(Connected v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Direction getDrawDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Direction getDrawDirectionFrom(Connected from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDrawDirection(Direction d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDrawDirectionFrom(Direction d, Connected from) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkTerminator getFromDecoration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkTerminator getToDecoration() {
		// TODO Auto-generated method stub
		return null;
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
	

}
