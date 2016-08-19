package org.kite9.diagram.position;

import java.util.Collections;
import java.util.List;

public class RouteRenderingInformationImpl extends AbstractRenderingInformationImpl implements RouteRenderingInformation {

	private List<Dimension2D> routePositions;
	private List<Boolean> hops;
	private boolean contradicting;
	
	public List<Dimension2D> getRoutePositions() {
		return routePositions;
	}

	public List<Boolean> getHops() {
		return hops;
	}
	

	public boolean isHop(int pos) {
		return hops.get(pos);
	}

	public void setHop(int pos) {
		List<Boolean> hops = getHops();
		hops.set(pos, true);
	}

	public void reverse() {
		Collections.reverse(hops);
		Collections.reverse(routePositions);
	}

	public Decoration getFromDecoration() {
		return null;
	}

	public void setFromDecoration(Decoration fromDecoration) {
	}

	public Decoration getToDecoration() {
		return null;
	}

	public void setToDecoration(Decoration toDecoration) {
	}

	public Dimension2D getWaypoint(int pos) {
		return getRoutePositions().get(pos);
	}

	public void clear() {
		getRoutePositions().clear();
		getHops().clear();
	}

	public int size() {
		return getRoutePositions().size();
	}

	public void add(Dimension2D d) {
		getRoutePositions().add(d);
	}

	public boolean isContradicting() {
		return contradicting;
	}

	public void setContradicting(boolean b) {
		this.contradicting = b;
	}

}
