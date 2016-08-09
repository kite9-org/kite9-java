package org.kite9.diagram.adl;

import org.kite9.diagram.position.Direction;
import org.kite9.diagram.primitives.Label;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Joins glyphs and arrows to one another. 
 * Can have text attached to the 'to' end, and decoration at the 'to' end
 * 
 * Set the drawDirection if the rendering system should draw the link in a particular orientation on the
 * diagram.
 * 
 * @author robmoffat
 */
public class Link extends AbstractConnection {

	private static final long serialVersionUID = -5950978530304852748L;

	public Link() {
		this.tagName = "link";
	}
	
	public Link(XMLElement from, XMLElement to) {
		this(from.getID()+"-"+to.getID(), from, to, TESTING_DOCUMENT);
	}
		
	public Link(String id, XMLElement from, XMLElement to, ADLDocument doc) {
		this(id, from, to, null, null, null, null, null, doc);
	}
	
	public Link(XMLElement from, XMLElement to, String fromStyle, Label fromLabel, String toEndStyle, Label toLabel, Direction drawDirection) {
		this(from.getID()+"-"+to.getID(), from, to, fromStyle, fromLabel, toEndStyle, toLabel, drawDirection, TESTING_DOCUMENT);
	}
	
	public Link(String id, XMLElement from, XMLElement to, String fromStyle, Label fromLabel, String toEndStyle, Label toLabel, Direction drawDirection, ADLDocument doc) {
		super(id, "link", from, to, drawDirection, fromStyle, fromLabel, toEndStyle, toLabel, doc);
	}

	public Link(XMLElement from, XMLElement to, String fromStyle, Label fromLabel, String toStyle, Label toLabel) {
		super(from.getID()+"-"+to.getID(), "link",  from, to, null, null, fromLabel, null, toLabel, TESTING_DOCUMENT);
	}

	@Override
	public LinkTerminator getFromDecoration() {
		return getProperty("fromDecoration", LinkTerminator.class);
	}

	@Override
	public LinkTerminator getToDecoration() {
		return getProperty("toDecoration", LinkTerminator.class);
	}
	
	private void setDecoration(String name, Object d) {
		Element e = ownerDocument.createElement(name);
		e.setTextContent((String) d);
		replaceProperty(name, e, Element.class);
	}

	@Override
	public void setFromDecoration(LinkTerminator fromDecoration) {
		replaceProperty("fromDecoration", fromDecoration, LinkTerminator.class);
	}

	@Override
	public void setToDecoration(LinkTerminator toDecoration) {
		replaceProperty("toDecoration", toDecoration, LinkTerminator.class);
	}

	/**
	 * Contains the ordering of the field within the diagram allLinks() list.
	 */
	int rank;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	protected Node newNode() {
		return new Link();
	}
	
	
}
