package org.kite9.diagram.common;

import java.util.Collection;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.IdentifiableDiagramElement;

/**
 * A diagram element which has connections that link to other Connected items within the diagram.
 * 
 * @author robmoffat
 *
 */
public interface Connected extends IdentifiableDiagramElement {

	/**
	 * Returns an unmodifiable collection of links
	 */
	Collection<Connection> getLinks();
	
	/**
	 * Removes a link from the collection
	 */
	void removeLink(Connection l);
	
	/**
	 * Adds a link to the collection
	 */
	void addLink(Connection l);
	
	/**
	 * Means that there exists a connection with this object at one end and c
	 * at the other.
	 */
	boolean isConnectedDirectlyTo(Connected c);
	
	/**
	 * Returns the connection between this object and c.
	 */
	Connection getConnectionTo(Connected c);
}
