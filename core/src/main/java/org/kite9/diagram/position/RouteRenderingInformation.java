package org.kite9.diagram.position;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * This is used to hold the route of an edge, or container border.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("route")
public class RouteRenderingInformation implements RenderingInformation {

    public RouteRenderingInformation() {
	}

	private static final long serialVersionUID = 1958606211421399887L;

	public List<Dimension2D> positions = new ArrayList<Dimension2D>();
	public List<Boolean> hops = new ArrayList<Boolean>();
	
	public Dimension2D getWaypoint(int pos) {
		return positions.get(pos);
	}
	
	public void clear() {
		positions.clear();
		hops.clear();
	}
	
	public int size() {
		return positions.size();
	}
	
	public void add(Dimension2D d) {
		positions.add(d);
		hops.add(false);
	}
	
	public void set(int point, Dimension2D d) {
		while (point>=positions.size()) {
			positions.add(null);
			hops.add(false);
		}
		positions.set(point, d);
	}
	
	@Override
	public String toString() {
		StringBuffer sb= new StringBuffer();
		for (int i = 0; i < size(); i++) {
			sb.append("("+getWaypoint(i).x()+", "+getWaypoint(i).y()+")");
		}
		return sb.toString();
	}

	public boolean isHop(int pos) {
		return hops.get(pos);
	}
	
	public void setHop(int pos) {
		hops.set(pos, true);
	}
	
	public Dimension2D getBoundingSize() {
	    Dimension2D pos = getBoundingPosition();
	    double x =0, y= 0;
	    for (Dimension2D p : positions) {
		x = Math.max(p.x(),x);
		y = Math.max(p.y(),y);
	    }
	    
	    return new Dimension2D(x-pos.x(), y-pos.y());
	}
	
	public Dimension2D getBoundingPosition() {
	    double x =Double.MAX_VALUE, y= Double.MAX_VALUE;
	    for (Dimension2D p : positions) {
		x = Math.min(p.x(),x);
		y = Math.min(p.y(),y);
	    }
	    
	    return new Dimension2D(x,y);
	}
	
	private boolean contradicting;

	public boolean isContradicting() {
		return contradicting;
	}

	public void setContradicting(boolean b) {
		this.contradicting = b;
	}

	public boolean isRendered() {
		return rendered;
	}
	
	public void setNotRendered() {
		this.rendered = false;
	}

	@XStreamAsAttribute
	private boolean rendered = true;
}