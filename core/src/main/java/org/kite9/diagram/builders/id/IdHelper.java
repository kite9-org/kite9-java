package org.kite9.diagram.builders.id;

/** 
 * Returns Ids to use for the ADL elements from a given object.
 * 
 * @author robmoffat
 *
 */
public interface IdHelper {

	public String getId(Object o);
	
	public Address getCompoundId(Object o);
}