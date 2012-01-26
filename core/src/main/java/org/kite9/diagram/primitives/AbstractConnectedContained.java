package org.kite9.diagram.primitives;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * This is the base class for most {@link Connected} elements within the diagram.
 * e.g. Arrows, Glyphs, Contexts.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractConnectedContained extends AbstractIdentifiableDiagramElement implements Connected, Contained {

	public AbstractConnectedContained() {
		super();
	}

	public AbstractConnectedContained(String id) {
		super(id);
	}

	private static final long serialVersionUID = 8856547625295452633L;

	private LinkedHashSet<Connection> links = new LinkedHashSet<Connection>();
	
	public void addLink(Connection l) {
		links.add(l);
	}
	
	@XStreamOmitField
	Container c;

	public Container getContainer() {
		return c; 
	}

	public void setContainer(Container c) {
		this.c = c;
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
}


