package org.kite9.diagram.xml;

import org.kite9.diagram.position.Layout;

public abstract class AbstractXMLContainerElement extends AbstractStyledKite9SVGElement {

	public AbstractXMLContainerElement() {
		super();
	}

	public AbstractXMLContainerElement(String name, ADLDocument owner) {
		super(name, owner);
	}

	public AbstractXMLContainerElement(String id, String tag, ADLDocument doc) {
		super(id, tag, doc);
	}

	public Layout getLayoutDirection() {
		String layout = getAttribute("layout");
		return layout.length() == 0 ? null : Layout.valueOf(layout);
	}

	public void setLayoutDirection(Layout layout) {
	    if (layout == null) {
	    	removeAttribute("layout");
	    } else {
	    	setAttribute("layout", layout.name());
	    }
	}

	public void setLabel(Kite9SVGElement label) {
	    replaceProperty("label", label);
	}
	
	public Kite9SVGElement getLabel() {
		return getProperty("label");
	}
}
