package org.kite9.diagram.style;

import java.util.ArrayList;
import java.util.Collection;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.common.Connected;
import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.StyledXMLElement;
import org.kite9.diagram.xml.XMLElement;

/**
 * Handles DiagramElements which are also Connnected.
 * 
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractConnectedXMLDiagramElement extends AbstractRectangularXMLDiagramElement implements Connected {
	
	public AbstractConnectedXMLDiagramElement(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}
	
	/**
	 * Call this method prior to using the functionality, so that we can ensure 
	 * all the members are set up correctly.
	 */
	protected void initialize() {
		ADLDocument doc = theElement.getOwnerDocument();
		Collection<XMLElement> references = doc.getReferences(theElement.getID());
		links = new ArrayList<>(references.size());
		for (XMLElement xmlElement : references) {
			DiagramElement de = xmlElement.getDiagramElement();
			if (de instanceof Connection) {
				links.add((Connection) de);
			}
		}
	}

	private transient Collection<Connection> links;

	@Override
	public Collection<Connection> getLinks() {
		ensureInitialized();
		return links;
	}

	public Connection getConnectionTo(Connected c) {
		for (Connection link : getLinks()) {
			if (link.meets(c)) {
				return link;
			}
		}

		return null;
	}

	public boolean isConnectedDirectlyTo(Connected c) {
		return getConnectionTo(c) != null;
	}

}
