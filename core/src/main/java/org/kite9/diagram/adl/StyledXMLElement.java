package org.kite9.diagram.adl;

import org.apache.batik.css.engine.CSSStylableElement;

public interface StyledXMLElement extends CSSStylableElement {

	public void setClasses(String c);
	
	public String getClasses();
	
	public void setStyle(String s);
	
	public String getStyle();
	
	public void setShapeName(String s);
	
	public String getShapeName();
}
