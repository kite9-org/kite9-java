package org.kite9.diagram.adl;

import java.util.List;

import org.kite9.diagram.common.Connected;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.position.RectangleRenderingInformation;


/**
 * Interface to say that this diagram element contains others rendered within it.
 * @see Leaf
 * 
 * @author robmoffat
 *
 */
public interface Container extends IdentifiableDiagramElement, Connected {

	public List<Contained> getContents();
	
	/**
	 * Returns the order in which contents of the container should be
	 * laid out, or null if there is no canonical ordering.
	 */
	public Layout getLayoutDirection();

	public void setLayoutDirection(Layout d);
	
	public Label getLabel();
	
	public void setLabel(Label l);
	
	public RectangleRenderingInformation getRenderingInformation();
	
	public boolean isBordered();

}
