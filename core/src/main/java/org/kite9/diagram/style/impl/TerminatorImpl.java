package org.kite9.diagram.style.impl;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Terminator;
import org.kite9.diagram.xml.StyledXMLElement;

public class TerminatorImpl extends AbstractRectangularXMLDiagramElement implements Terminator {

	public TerminatorImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	protected void initialize() {
	}

}
