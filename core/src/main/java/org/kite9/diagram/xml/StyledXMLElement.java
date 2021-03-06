package org.kite9.diagram.xml;

import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.value.Value;

public interface StyledXMLElement extends CSSStylableElement, XMLElement {

	public void setClasses(String c);
	
	public String getClasses();
	
	public void setStyle(String s);
	
	public String getStyle();
	
	public void setShapeName(String s);
	
	public String getShapeName();
	
	public Value getCSSStyleProperty(String prop);
}


