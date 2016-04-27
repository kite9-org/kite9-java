package org.kite9.diagram.builders.id;

public interface HasAddress {

	
	
	/**
	 * Returns a stand-alone address for this element.
	 */
	public Address getID();
	
	public String getStringID();
	
	/**
	 * This is used in compound IDs, where further components provide elements of the
	 * address.
	 */
	public Address extend(Address in);

}
