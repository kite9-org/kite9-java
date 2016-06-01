package org.kite9.diagram.style;

import org.apache.batik.css.engine.CSSStylableElement;

public interface StyledDiagramElement extends CSSStylableElement {

	public void setClasses(String c);
	
	public void setStyle(String s);
}
