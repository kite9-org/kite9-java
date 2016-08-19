package org.kite9.diagram.adl;

import org.apache.batik.css.engine.value.Value;

public interface StyledDiagramElement extends ShapedDiagramElement {
	
	public String getShapeName();
	
	public Value getCSSStyleProperty(String prop);
}
