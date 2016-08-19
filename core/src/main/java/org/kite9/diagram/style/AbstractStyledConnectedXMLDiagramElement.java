package org.kite9.diagram.style;

import java.util.ArrayList;
import java.util.Collection;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.common.Connected;
import org.kite9.diagram.position.RectangleRenderingInformationImpl;
import org.kite9.diagram.position.RenderingInformation;
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
public class AbstractStyledConnectedXMLDiagramElement extends AbstractStyledXMLDiagramElement implements Connected {

	public AbstractStyledConnectedXMLDiagramElement(StyledXMLElement el) {
		super(el);
	}
	
	private transient Collection<Connection> links;

	@Override
	public Collection<Connection> getLinks() {
		if (links == null) {
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
		
		return links;
	}
	

	public Connection getConnectionTo(Connected c) {
		for (Connection link : links) {
			if (link.meets(c)) {
				return link;
			}
		}

		return null;
	}

	public boolean isConnectedDirectlyTo(Connected c) {
		return getConnectionTo(c) != null;
	}
	
	private RenderingInformation ri;
	
	@Override
	public RenderingInformation getRenderingInformation() {
		if (ri == null) {
			ri = new RectangleRenderingInformationImpl();
		}
		
		return ri;
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.ri = ri;
	}


}
