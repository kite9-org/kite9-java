package org.kite9.diagram.primitives;


/**
 * A simple marker interface for diagram elements to say that they are 
 * embedded in a (single) container, in which they must be drawn.
 */
public interface Contained extends IdentifiableDiagramElement {

	public Container getContainer();
	
	public void setContainer(Container c);

}