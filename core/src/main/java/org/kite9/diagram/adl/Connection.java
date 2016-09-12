package org.kite9.diagram.adl;

import org.kite9.diagram.common.BiDirectional;
import org.kite9.diagram.common.Connected;
import org.kite9.diagram.position.RouteRenderingInformation;

/**
 * A connection is a link between two Connected items within the diagram.  Connections have a notional
 * 'from' and 'to', as well as decorations to show how the links should look.
 */
public interface Connection extends DiagramElement, BiDirectional<Connected> {

	/**
	 * The shape of the end of the edge at the from end
	 */
	public org.kite9.diagram.adl.Terminator getFromDecoration();
	
	/**
	 * The shape of the end of the edge at the to end
	 */
	public org.kite9.diagram.adl.Terminator getToDecoration();
	
	/**
	 * The text written on the from end
	 */
	public Label getFromLabel();
	
	/**
	 * Text on the to end
	 */
	public Label getToLabel();

	
	public RouteRenderingInformation getRenderingInformation();
	
	@Deprecated
	public String getStyle();
	
	/**
	 * Returns the rank of the connection from the ordering of all the connections on the diagram.
	 */
	public int getRank();
}
