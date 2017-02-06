package org.kite9.diagram.style.impl;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.FixedSizeGraphics;
import org.kite9.diagram.xml.StyledKite9SVGElement;

public class FixedSizeGraphicsImpl extends AbstractConnectedDiagramElement implements FixedSizeGraphics {

	public FixedSizeGraphicsImpl(StyledKite9SVGElement el, DiagramElement parent) {
		super(el, parent);
	}

}
