package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kite9.diagram.position.Layout;
import org.kite9.diagram.xml.ADLDocument;
import org.w3c.dom.Node;

public abstract class AbstractConnectedContainer extends AbstractConnectedContained implements Container {

	public Label getLabel() {
		return getProperty("label", Label.class);
	}

	private static final long serialVersionUID = 9108816802892206563L;
	
	private List<Contained> contents;

	public List<Contained> getContents() {
		if (contents != null) {
			return contents;
		}
		
		contents = new ArrayList<Contained>();
		for (int i = 0; i < getChildNodes().getLength(); i++) {
			Node n = getChildNodes().item(i);
			if (n instanceof Contained) {
				contents.add((Contained) n);
			}
		}
		return contents;
	}
	
	public AbstractConnectedContainer() {
	}
	
	
	public AbstractConnectedContainer(String id, String tag, ADLDocument doc) {
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

	public void setLabel(Label label) {
	    replaceProperty("label", label, Label.class);
	}
}
