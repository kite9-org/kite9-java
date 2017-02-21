package org.kite9.diagram.adl.sizing;

import org.kite9.diagram.adl.Leaf;
import org.kite9.diagram.adl.Rectangular;

/**
 * A graphics element which just contains some SVG, which has a size depending on
 * the SVG itself.
 * 
 * @author robmoffat
 *
 */
public interface FixedSizeGraphics extends Rectangular, Leaf, HasLayeredGraphics {

}
