package org.kite9.diagram.position;

import java.io.Serializable;



/**
 * This holds formatting information for the graphical renderer to use.
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
