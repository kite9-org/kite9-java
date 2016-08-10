package org.kite9.diagram.position;

import org.kite9.diagram.common.DiagramElement;
import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.AbstractStyleableXMLElement;
import org.kite9.diagram.xml.AbstractXMLElement;
import org.kite9.framework.common.Kite9ProcessingException;
import org.w3c.dom.Node;

/**
 * This is compatible with the awt dimension class, which is used for a
 * lot of rendering.  
 * 
 * This has double precision though and has internal scaling operations, as well
 * as actions to allow you to apply operations to a specific direction.
 * 
 * <em>Immutable</em>
 * 
 * @author robmoffat
 *
 */
public class Dimension2D extends AbstractStyleableXMLElement {

	private double x, y;
	
	public double getHeight() {
		return y;
	}

	public Dimension2D getSize() {
		return this;
	}

	public double getWidth() {
		return x;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dimension2D) {
			Dimension2D d2d = (Dimension2D) obj;
			return (x==d2d.x) && (y==d2d.y);
		}
		return false;
	}

	public Dimension2D() {
	}
	
	public Dimension2D(double x, double y) {
		super("dim2d", AbstractXMLElement.TESTING_DOCUMENT);
		this.x = x;
		this.y = y;
		setAttribute("x", ""+x);
		setAttribute("y", ""+y);
		this.readonly = true;
	}
	
	public Dimension2D(Dimension2D clone) {
		this(clone.x, clone.y);
	}

	public Dimension2D divide(Dimension2D by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x / by.x;
		d2.y = y / by.y;
		return d2;
	}
	
	public Dimension2D multiply(Dimension2D by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x * by.x;
		d2.y = y * by.y;
		return d2;
	}
	
	public Dimension2D multiply(double by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x * by;
		d2.y = y * by;
		return d2;
	}
	
	
	public Dimension2D roundUpTo(Dimension2D factor) {
		Dimension2D d2 = new Dimension2D();
		d2.x = Math.ceil(x / factor.x) * factor.x;
		d2.y = Math.ceil(y / factor.y) * factor.y;
		return d2;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}

	public Dimension2D add(Dimension2D by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x + by.x;
		d2.y = y + by.y;
		return d2;
	}
	
	public Dimension2D minus(Dimension2D by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x - by.x;
		d2.y = y - by.y;
		return d2;
	}
	
	public double x() {
		return x;
	}
	
	public double y() { 
		return y;
	}
	
	public Dimension2D setX(double x) {
		return new Dimension2D(x, this.y);
	}

	public Dimension2D setY(double y) {
		return new Dimension2D(this.x, y);
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean v) {
		throw new Kite9ProcessingException("Immutable");
	}

	@Override
	protected Node newNode() {
		throw new Kite9ProcessingException("Immutable");
	}
	
	public Dimension2D copy(ADLDocument doc) {
		Dimension2D out = new Dimension2D(this);
		out.setOwnerDocument(doc);
		return out;
	}

	public int compareTo(DiagramElement o) {
		return 0;
	}
}
