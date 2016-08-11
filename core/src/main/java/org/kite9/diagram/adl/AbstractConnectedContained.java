package org.kite9.diagram.adl;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.kite9.diagram.common.Connected;
import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.style.AbstractXMLDiagramElement;
import org.kite9.diagram.xml.ADLDocument;

/**
 * This is the base class for most {@link Connected} elements within the diagram.
 * e.g. Arrows, Glyphs, Contexts.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractConnectedContained extends AbstractXMLDiagramElement implements Connected, Contained {

	public AbstractConnectedContained() {
	}
	
	public AbstractConnectedContained(String id, String tag, ADLDocument doc) {
		super(id, tag, doc);
	}

	private static final long serialVersionUID = 8856547625295452633L;

	private LinkedHashSet<Connection> links = new LinkedHashSet<Connection>();
	
	public void addLink(Connection l) {
		links.add(l);
	}

	public Container getContainer() {
		return (Container) getParentNode();
	}

	public void setContainer(Container c) {
		setParentNode(c);
	}

	public Collection<Connection> getLinks() {
		return links;
	}

	public void removeLink(Connection l) {
		links.remove(l);
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

	public RectangleRenderingInformation getRenderingInformation() {
		return getBasicRenderingInformation();
	}
	
	
}


