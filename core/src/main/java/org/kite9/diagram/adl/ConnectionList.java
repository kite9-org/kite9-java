package org.kite9.diagram.adl;

import java.util.List;

/**
 * Implemented where the {@link DiagramElement} has a list of connections. 
 * 
 * @author robmoffat
 *
 */
public interface ConnectionList extends DiagramElement {

	public List<Connection> getConnectionList();
}
