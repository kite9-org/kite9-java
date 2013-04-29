package org.kite9.diagram.position;

import java.io.Serializable;



/**
 * This holds formatting information for the graphical renderer to use.
 * 
 * TODO: eventually, should have colour information (for green/red test coverage
 * reports) 
 * 
 * @author robmoffat
 *
 */
public interface RenderingInformation extends Serializable {
	
	/**
	 * Returns true if this item should be drawn
	 */
	public boolean isRendered();

	public abstract void setRendered(boolean r);
  
}
