package org.kite9.diagram.adl;

/**
 * Marker interface to say that this diagram element contains some text, and that the
 * element should be sized to contain the text.
 * 
 * @author robmoffat
 *
 */
public interface Text extends Leaf {
	
	public String getText();
}
