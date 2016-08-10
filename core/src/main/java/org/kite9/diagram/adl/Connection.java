package org.kite9.diagram.adl;

import org.kite9.diagram.common.BiDirectional;
import org.kite9.diagram.common.Connected;
import org.kite9.diagram.position.RouteRenderingInformation;
import org.kite9.diagram.xml.LinkTerminator;

/**
 * A connection is a link between two Connected items within the diagram.  Connections have a notional
 * 'from' and 'to', as well as decorations to show how the links should look.
 */
public interface Connection extends PositionableDiagramElement, BiDirectional<Connected> {

	/**
	 * The shape of the end of the edge at the from end
	 */
	public LinkTerminator getFromDecoration();
	
	/**
	 * The shape of the end of the edge at the to end
	 */
	public LinkTerminator getToDecoration();
	
	/**
	 * The text written on the from end
	 */
	public Label getFromLabel();
	
	/**
	 * Text on the to end
	 */
	public Label getToLabel();

	
	public RouteRenderingInformation getRenderingInformation();
}
