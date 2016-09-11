package org.kite9.diagram.style;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.StyledXMLElement;
import org.kite9.diagram.xml.XMLElement;

public class DiagramImpl extends ContainerImpl implements Diagram {

	private List<Connection> connections;

	public DiagramImpl(StyledXMLElement el) {
		super(el, null);
	}
	

	@Override
	public List<Connection> getConnectionList() {
		if (connections == null) {
			ADLDocument doc = theElement.getOwnerDocument();
			connections = new ArrayList<>(doc.getConnectionElements().size());
			for (XMLElement xmlElement : theElement) {
				connections.add((Connection) xmlElement.getDiagramElement());
			}
		}
		return connections;
	}

}
