package org.kite9.diagram.common;

import java.util.Collection;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.Container;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.position.RectangleRenderingInformation;

/**
 * A diagram element which has connections that link to other Connected items within the diagram.
 * 
 * @author robmoffat
 *
 */
public interface Connected extends DiagramElement {

	/**
	 * Returns an unmodifiable collection of links
	 */
	Collection<Connection> getLinks();
	
	/**
	 * Means that there exists a connection with this object at one end and c
	 * at the other.
	 */
	boolean isConnectedDirectlyTo(Connected c);
	
	/**
	 * Returns the connection between this object and c.
	 */
	Connection getConnectionTo(Connected c);
	
	/**
	 * Overrides the main one, since all Connecteds are areas on the diagram rather than links.
	 */
	RectangleRenderingInformation getRenderingInformation();
	
}

