package org.kite9.diagram.style.impl;

import org.kite9.diagram.adl.Container;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.xml.StyledKite9SVGElement;

public class ConnectedContainerImpl extends AbstractConnectedDiagramElement implements Container {
	
	Label label;
	
	public ConnectedContainerImpl(StyledKite9SVGElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public Label getLabel() {
		ensureInitialized();
		return label;
	}

	@Override
	public boolean isBordered() {
		return !"false".equals(theElement.getAttribute("border"));
	}

	@Override
	protected void addLabelReference(Label de) {
		this.label = de;
	}
	
	
}
