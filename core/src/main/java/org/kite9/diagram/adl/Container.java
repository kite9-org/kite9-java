package org.kite9.diagram.adl;

import java.util.List;

import org.kite9.diagram.position.Layout;


/**
 * Interface to say that this diagram element contains a 
 * variable number of others rendered within it.
 * 
 * @see Leaf
 * 
 * @author robmoffat
 *
 */
public interface Container extends DiagramElement {

	public List<DiagramElement> getContents();
	
	/**
	 * Returns the order in which contents of the container should be
	 * laid out, or null if there is no canonical ordering.
	 * 
	 * In future, should use stylesheet to figure out layout direction.
	 */
	@Deprecated
	public Layout getLayoutDirection();
	
	public Label getLabel();

	@Deprecated
	public boolean isBordered();
}
