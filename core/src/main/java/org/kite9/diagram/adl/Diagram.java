package org.kite9.diagram.adl;

import java.util.Collection;

public interface Diagram extends Container, HasLayeredGraphics {

	public Collection<Connection> getConnectionsFor(String id);
}
