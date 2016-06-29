package org.kite9.diagram.primitives;

import org.kite9.diagram.style.StyledDiagramElement;

public interface TextContainingDiagramElement extends CompositionalDiagramElement, StyledDiagramElement {

	public void setText(String text);
	
	public String getText();
}
