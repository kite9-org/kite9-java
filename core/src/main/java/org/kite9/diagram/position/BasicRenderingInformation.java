package org.kite9.diagram.position;

/**
 * Contains details of how the diagram element should be shown.  
 * To speed up the rendering process, path and perimeter should both be normalized to meet 0,0, and
 * an offset from that point should be given.  
 * 
 * This means that when an object moves, we don't need to change the path.
 * 
 * @author robmoffat
 *
 */
public class BasicRenderingInformation extends RouteRenderingInformationImpl {

	public BasicRenderingInformation(Dimension2D position, Dimension2D size, HPos horizontalJustification,
			VPos verticalJustification, boolean rendered) {
		setPosition(position);
		setSize(size);
		setHorizontalJustification(horizontalJustification);
		setVerticalJustification(verticalJustification);
		setRendered(rendered);
	}
	
	public BasicRenderingInformation() {
	}
}