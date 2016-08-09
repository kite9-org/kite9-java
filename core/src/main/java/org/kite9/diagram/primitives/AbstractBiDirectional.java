package org.kite9.diagram.primitives;

import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.common.BiDirectional;
import org.kite9.diagram.position.Direction;
import org.kite9.framework.logging.LogicException;
import org.w3c.dom.Element;

public abstract class AbstractBiDirectional<X extends IdentifiableDiagramElement> extends
		AbstractIdentifiableDiagramElement implements BiDirectional<X> {

	/**
	 * For serialization
	 */
	public AbstractBiDirectional() {
	}

	public AbstractBiDirectional(String id, String tag, X from, X to, Direction drawDirection, ADLDocument doc) {
		super(id, tag, doc);
		setFrom(from);
		setTo(to);
		setDrawDirection(drawDirection);
	}

	private static final long serialVersionUID = -2932750084676000416L;

	public Direction getDrawDirection() {
		String dd = getAttribute("drawDirection");
		if (dd == null) {
			return null;
		}
		return Direction.valueOf(dd);
	}

	@SuppressWarnings("unchecked")
	public X getFrom() {
		Element from = getProperty("from", Element.class);
		String reference = from.getAttribute("reference");
		Element e = ownerDocument.getElementById(reference);
		return (X) e;
	}

	@SuppressWarnings("unchecked")
	public X getTo() {
		Element to = getProperty("to", Element.class);
		String reference = to.getAttribute("reference");
		Element e = ownerDocument.getElementById(reference);
		return (X) e;
	}

	public void setFrom(X v) {
		Element from = ownerDocument.createElement("from");
		from.setAttribute("reference", v.getID());
		replaceProperty("from", from, Element.class);
	}

	public void setTo(X v) {
		Element to = ownerDocument.createElement("to");
		to.setAttribute("reference", v.getID());
		replaceProperty("to", to, Element.class);
	}

	@Override
	public String toString() {
		return "[" + getFrom() + "-" + getTo() + "]";
	}

	public X otherEnd(X end) {
		if (end == getFrom())
			return getTo();
		if (end == getTo())
			return getFrom();
		throw new LogicException("This is not an end: " + end + " of " + this);
	}

	public boolean meets(BiDirectional<X> e) {
		return getFrom().equals(e.getTo()) || getFrom().equals(e.getFrom())
				|| getTo().equals(e.getTo()) || getTo().equals(e.getFrom());
	}

	public boolean meets(X v) {
		return getFrom().equals(v) || getTo().equals(v);
	}

	public Direction getDrawDirectionFrom(X end) {
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

	public void setDrawDirectionFrom(Direction d, X end) {

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