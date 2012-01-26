package org.kite9.diagram.position;

import java.awt.Dimension;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This extends and is compatible with the awt dimension class, which is used for a
 * lot of rendering.  
 * This has double precision though and has internal scaling operations, as well
 * as actions to allow you to apply operations to a specific direction.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("d")
public class Dimension2D extends Dimension {

	private static final long serialVersionUID = -2042867297107289469L;

	@Override
	public double getHeight() {
		return y;
	}

	@Override
	public Dimension getSize() {
		return this;
	}

	@Override
	public double getWidth() {
		return x;
	}

	@Override
	public void setSize(Dimension arg0) {
		this.x = arg0.getWidth();
		this.y = arg0.getHeight();
		setInts();
	}

	@Override
	public void setSize(double x, double y) {
		this.x = x;
		this.y = y;
		setInts();
	}

	@Override
	public void setSize(int x, int y) {
		this.x = x;
		this.y = y;
		setInts();
	}

	@XStreamAsAttribute
	double x;
	@XStreamAsAttribute
	double y;
	
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
		this.x = x;
		this.y = y;
		setInts();
	}
	
	public Dimension2D(Dimension2D clone) {
		this(clone.x, clone.y);
		setInts();
	}
	
	private void setInts() {
		this.width = (int) x;
		this.height = (int) y;
	}

	public Dimension2D divide(Dimension2D by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x / by.x;
		d2.y = y / by.y;
		d2.setInts();
		return d2;
	}
	
	public Dimension2D multiply(Dimension2D by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x * by.x;
		d2.y = y * by.y;
		d2.setInts();
		return d2;
	}
	
	public Dimension2D multiply(double by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x * by;
		d2.y = y * by;
		d2.setInts();
		return d2;
	}
	
	
	public Dimension2D roundUpTo(Dimension2D factor) {
		Dimension2D d2 = new Dimension2D();
		d2.x = Math.ceil(x / factor.x) * factor.x;
		d2.y = Math.ceil(y / factor.y) * factor.y;
		d2.setInts();
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
		d2.setInts();
		return d2;
	}
	
	public Dimension2D minus(Dimension2D by) {
		Dimension2D d2 = new Dimension2D();
		d2.x = x - by.x;
		d2.y = y - by.y;
		d2.setInts();
		return d2;
	}
	
	public double x() {
		return x;
	}
	
	public double y() { 
		return y;
	}

	public void setX(double x) {
		this.x = x;
		setInts();
	}

	public void setY(double y) {
		this.y = y;
		setInts();
	}
	
	public void increaseY(double y) {
		this.y += y;
		setInts();
	}
	
	public void increaseX(double x) {
		this.x += x;
		setInts();
	}
}
