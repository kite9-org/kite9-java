package org.kite9.diagram.style;

import org.kite9.diagram.primitives.DiagramElement;

public interface ShapedDiagramElement extends DiagramElement {

	public String getShapeName();
	
	public void setShapeName(String name);
	
}
