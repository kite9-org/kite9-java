package org.kite9.diagram.adl;

import org.kite9.diagram.style.DiagramElement;

public interface ShapedDiagramElement extends DiagramElement {

	/**
	 * This is entirely derived from style info, but left here as it's often needed
	 */
	public String getShapeName();
}
