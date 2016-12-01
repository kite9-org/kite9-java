package org.kite9.diagram.adl;

import java.util.List;

import org.kite9.diagram.position.Layout;
import org.kite9.diagram.position.RectangleRenderingInformation;


/**
 * Interface to say that this diagram element contains a 
 * variable number of others rendered within it.
 * 
 * @see Leaf
 * 
 * @author robmoffat
 *
 */
public interface Container extends DiagramElement, Rectangular {

	public List<DiagramElement> getContents();
	
	public Layout getLayout();
	
	public Label getLabel();

	@Deprecated
	public boolean isBordered();
	
}
