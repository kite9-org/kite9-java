package org.kite9.diagram.adl;

import java.util.List;

import org.kite9.diagram.position.Layout;
import org.kite9.diagram.position.RectangleRenderingInformation;


/**
 * Interface to say that this diagram element contains a 
 * variable number of others rendered within it.
 * 
 * Opposite of {@link Leaf}
 * 
 * @author robmoffat
 *
 */
public interface Container extends DiagramElement {

	public List<DiagramElement> getContents();
	
	public Layout getLayout();
	
	/**
	 * Not sure if we should have this method or not.  - Deprecated for now, needs further thought.
	 */
	@Deprecated
	public Label getLabel();

	@Deprecated
	public boolean isBordered();
	
	public RectangleRenderingInformation getRenderingInformation();
}
