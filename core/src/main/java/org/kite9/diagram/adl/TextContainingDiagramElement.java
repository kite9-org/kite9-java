package org.kite9.diagram.adl;

public interface TextContainingDiagramElement extends CompositionalDiagramElement, StyledDiagramElement {

	public void setText(String text);
	
	public String getText();
}
