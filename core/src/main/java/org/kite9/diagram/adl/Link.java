package org.kite9.diagram.adl;

import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractConnection;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Label;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
	
	public Link(String id, Connected from, Connected to, ADLDocument doc) {
		this(id, from, to, null, null, null, null, null, doc);
	}
	
	public Link(String id, Connected from, Connected to, String fromStyle, Label fromLabel, String toEndStyle, Label toLabel, Direction drawDirection, ADLDocument doc) {
		super(id, "link", from, to, drawDirection, fromStyle, fromLabel, toEndStyle, toLabel, doc);
	}

	@Override
	public Object getFromDecoration() {
		return getDecoration("fromDecoration");
	}

	@Override
	public Object getToDecoration() {
		return getDecoration("toDecoration");
	}
	
	private void setDecoration(String name, Object d) {
		Element e = ownerDocument.createElement(name);
		e.setTextContent((String) d);
		replaceProperty(name, e, Element.class);
	}
	
	private String getDecoration(String name) {
		Element e = getProperty(name, Element.class);
		if (e == null) {
			return null;
		} else {
			return e.getTextContent();
		}
	}

	@Override
	public void setFromDecoration(Object fromDecoration) {
		setDecoration("fromDecoration", fromDecoration);
	}

	@Override
	public void setToDecoration(Object toDecoration) {
		setDecoration("toDecoration", toDecoration);
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.renderingInformation = ri;
	}
	
	/**
	 * Contains the ordering of the field within the diagram allLinks() list.
	 */
	@XStreamOmitField
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
