package org.kite9.diagram.style;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.xml.StyledXMLElement;

public class DiagramImpl extends ContainerImpl implements Diagram {

	private List<Connection> connections;

	public DiagramImpl(StyledXMLElement el) {
		super(el, null);
	}
	

	@Override
	public List<Connection> getConnectionList() {
		return connections;
	}

	protected void handleConnection(Connection c) {
		if (connections == null) {
			connections = new ArrayList<>(100);
		}
		connections.add(c);
	}

}
