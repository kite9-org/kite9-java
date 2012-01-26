package org.kite9.diagram.primitives;

import org.kite9.diagram.position.Direction;
import org.kite9.framework.logging.LogicException;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class AbstractBiDirectional<X> extends AbstractIdentifiableDiagramElement implements BiDirectional<X> {

    /**
     * For serialization
     */
    public AbstractBiDirectional() {
    }

    public AbstractBiDirectional(X from, X to, Direction drawDirection) {
	super();
	this.from = from;
	this.to = to;
	this.drawDirection = drawDirection;
    }

    private static final long serialVersionUID = -2932750084676000416L;
    protected X from;
    protected X to;

	@XStreamAsAttribute
    protected Direction drawDirection = null;

    public Direction getDrawDirection() {
	return drawDirection;
    }

    public X getFrom() {
	return from;
    }

    public X getTo() {
	return to;
    }

    public void setFrom(X v) {
	this.from = v;
    }

    public void setTo(X v) {
	this.to = v;
    }

    @Override
    public String toString() {
	return "[" + from + "-" + to + "]";
    }

    public X otherEnd(X end) {
	if (end == from)
	    return to;
	if (end == to)
	    return from;
	throw new LogicException("This is not an end: " + end + " of " + this);
    }

    public boolean meets(BiDirectional<X> e) {
	return getFrom().equals(e.getTo()) || getFrom().equals(e.getFrom()) || getTo().equals(e.getTo())
		|| getTo().equals(e.getFrom());
    }

    public boolean meets(X v) {
	return getFrom().equals(v) || getTo().equals(v);
    }

    public Direction getDrawDirectionFrom(X end) {
	if (drawDirection == null)
	    return null;

	if (end.equals(getFrom())) {
	    return drawDirection;
	}

	if (end.equals(getTo())) {
	    return Direction.values()[(drawDirection.ordinal() + 2) % 4];
	}

	throw new RuntimeException("Trying to get direction from an end that's not set: " + end + " in " + this);
    }

    public void setDrawDirection(Direction d) {
	this.drawDirection = d;
    }

    public void setDrawDirectionFrom(Direction d, X end) {

	if (end.equals(getFrom())) {
	    this.drawDirection = d;
	}

	if (end.equals(getTo())) {
	    this.drawDirection = Direction.reverse(d);
	}

	throw new RuntimeException("Trying to set direction from an end that's not set: " + end + " in " + this);

    }
}