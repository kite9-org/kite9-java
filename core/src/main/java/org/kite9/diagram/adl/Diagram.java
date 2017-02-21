package org.kite9.diagram.adl;

import java.util.Collection;

import org.kite9.diagram.adl.sizing.HasLayeredGraphics;

public interface Diagram extends Container, HasLayeredGraphics {

	public Collection<Connection> getConnectionsFor(String id);
}
