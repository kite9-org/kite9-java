package org.kite9.diagram.adl.sizing;

import java.awt.geom.Rectangle2D;
import java.util.function.Function;

import org.apache.batik.gvt.GraphicsNode;
import org.kite9.diagram.adl.DiagramElement;

/**
 * Means this {@link DiagramElement} can produce a {@link GraphicsNode} 
 * representing itself in a particular layer of the diagram.
 * 
 * @author robmoffat
 *
 */
public interface HasLayeredGraphics extends DiagramElement {
	
	public GraphicsNode getGraphicsForLayer(Object l);
	
	public void eachLayer(Function<GraphicsNode, Void> cb);
	
	/**
	 * Returns just the bounds of the SVG elements.
	 */
	public Rectangle2D getSVGBounds();
}