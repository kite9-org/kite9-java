package org.kite9.diagram.primitives;

import org.apache.batik.css.engine.StyleDeclarationProvider;
import org.apache.batik.css.engine.StyleMap;
import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.style.StyledDiagramElement;

/**
 * Base class for Styled diagram elements (i.e. all of them, now).
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractStyledDiagramElement extends AbstractDiagramElement implements StyledDiagramElement {

	public AbstractStyledDiagramElement() {
		super();
	}

	public AbstractStyledDiagramElement(String name, ADLDocument owner) {
		super(name, owner);
	}

	public StyleDeclarationProvider getOverrideStyleDeclarationProvider() {
		return null;
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
	
	private StyleMap sm;

	public StyleMap getComputedStyleMap(String pseudoElement) {
		return sm;
	}

	public void setComputedStyleMap(String pseudoElement, StyleMap sm) {
		this.sm = sm;
	}

	public String getCSSClass() {
		return getAttribute("class")+" "+getTagName();
	}

	public String getShapeName() {
		return getAttribute("shape");
	}
	
	public void setShapeName(String s) {
		setAttribute("shape", s);
	}

	public boolean isPseudoInstanceOf(String pseudoClass) {
		return false;
	}
	
}
