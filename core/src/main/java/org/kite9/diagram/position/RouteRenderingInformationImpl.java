package org.kite9.diagram.position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.ContainerProperty;
import org.w3c.dom.Node;

public class RouteRenderingInformationImpl extends RectangleRenderingInformationImpl implements RouteRenderingInformation {

	@SuppressWarnings("unchecked")
	public ContainerProperty<Dimension2D> getRoutePositions() {
		ContainerProperty<Dimension2D> cp = getProperty("route", ContainerProperty.class);
		if (cp == null) {
			cp = new ContainerProperty<Dimension2D>("route", (ADLDocument) ownerDocument);
			replaceProperty("route", cp, ContainerProperty.class);
		}
		
		return cp;
	}

	public List<Boolean> getHops() {
		String hops = getAttribute("hops");
		int routeSize = getRoutePositions().size();
		List<Boolean> out = new ArrayList<Boolean>(routeSize);
		for (int i = 0; i < routeSize; i++) {
			out.add(((hops.length() > i) && (hops.charAt(i) == 'H')) ? true : false);
		}
		return out;
	}
	

	public boolean isHop(int pos) {
		String hopStr = getAttribute("hops");
		return (hopStr.length() > pos) && (hopStr.charAt(pos) == 'H');
	}

	public void setHop(int pos) {
		List<Boolean> hops = getHops();
		hops.set(pos, true);
		setHopsString(hops);
	}

	public void setHopsString(List<Boolean> hops) {
		StringBuilder sb = new StringBuilder();
		for (Boolean boolean1 : hops) {
			if (boolean1) {
				sb.append('H');
			} else {
				sb.append('-');
			}
		}
		setAttribute("hops", sb.toString());
	}

	public void reverse() {
		List<Boolean> hops = getHops();
		Collections.reverse(hops);
		setHopsString(hops);
		
		List<Node> nodes = new ArrayList<Node>();
		ContainerProperty<Dimension2D> routePositions = getRoutePositions();
		for (Node node : routePositions) {
			if (node instanceof Dimension2D) {
				nodes.add(node);
			}
		}

		Collections.reverse(nodes);
		routePositions.clear();
		
		for (Node n : nodes) {
			routePositions.appendChild(n);
		}
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
		return (Dimension2D) getRoutePositions().getChildNodes().item(pos);
	}

	public void clear() {
		getRoutePositions().clear();
	}

	public int size() {
		return getRoutePositions().size();
	}

	public void add(Dimension2D d) {
		d.setOwnerDocument(this.getOwnerDocument());
		getRoutePositions().appendChild(d.copy((ADLDocument) this.ownerDocument));
	}

	public boolean isContradicting() {
		return "true".equals(getAttribute("contradicting"));
	}

	public void setContradicting(boolean b) {
		setAttribute("contradicting", ""+b);
	}

}
