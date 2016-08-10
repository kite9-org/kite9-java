package org.kite9.diagram.xml;

import org.apache.batik.css.engine.StyleDeclarationProvider;
import org.apache.batik.css.engine.StyleMap;

/**
 * Handles setting/getting properties of diagram elements.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractStyleableXMLElement extends AbstractXMLElement implements XMLElement, StyledXMLElement {
	
	public AbstractStyleableXMLElement() {
	}

	public AbstractStyleableXMLElement(String name, ADLDocument owner) {
		super(name, owner);
		this.tagName = name;
	}
	
	public AbstractStyleableXMLElement(String id, String tag, ADLDocument doc) {
		this(tag, doc);
		
		if (id == null) {
			id = createID();
		}
		
		setID(id);
	}
	
	public String getShapeName() {
		return getAttribute("shape");
	}
	
	public void setShapeName(String s) {
		setAttribute("shape", s);
	}


	public void setClasses(String s) {
		setAttribute("class", s);
	}
	
	public String getClasses() {
		return getAttribute("class");
	}

	public String getStyle() {
		return getAttribute("style");
	}

	public void setStyle(String s) {
		setAttribute("style", s);
	}
	
	public StyleMap getComputedStyleMap(String pseudoElement) {
		return sm;
	}

	public void setComputedStyleMap(String pseudoElement, StyleMap sm) {
		this.sm = sm;
	}

	public String getCSSClass() {
		return getAttribute("class");
	}
	

	public StyleDeclarationProvider getOverrideStyleDeclarationProvider() {
		return null;
	}


	public boolean isPseudoInstanceOf(String pseudoClass) {
		return false;
	}
	
	
}