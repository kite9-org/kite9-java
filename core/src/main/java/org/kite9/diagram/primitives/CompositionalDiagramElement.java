package org.kite9.diagram.primitives;

/**
 * This type of diagram element is a composite part of another element.  
 * Compare with {@link IdentifiableDiagramElement}.  This means that only a single (compositional)
 * reference is held to this object.  
 * 
 * This is used to simplify xml serialization.
 * 
 * @author robmoffat
 *
 */
public interface CompositionalDiagramElement extends DiagramElement {

	public Object getParent();
	
	public void setParent(Object parent);
	
}
