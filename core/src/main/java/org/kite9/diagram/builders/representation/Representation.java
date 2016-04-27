package org.kite9.diagram.builders.representation;

import org.kite9.diagram.builders.krmodel.noun.NounPart;


/**
 * Holds a representation of the knowledge collected by the builders.
 * 
 * Knowledge can be added to a representation in a given format.
 * 
 * @author robmoffat
 *
 */
public interface Representation<K> {
	
	public boolean contains(NounPart o);
	
	/**
	 * Outputs all of the knowledge contained in the representation, in K form.
	 */
	public K render();
}
