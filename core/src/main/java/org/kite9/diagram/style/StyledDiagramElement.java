package org.kite9.diagram.style;

import org.apache.batik.css.engine.CSSStylableElement;

public interface StyledDiagramElement extends CSSStylableElement, ShapedDiagramElement {

	public void setClasses(String c);
	
	public String getClasses();
	
	public void setStyle(String s);
	
	/**
	 * Temporary method, to keep things working for now.
	 */
	public String getStyle();
	
	public void setShapeName(String s);
	
	public String getShapeName();
}
