package org.kite9.diagram.primitives;

import org.kite9.diagram.builders.id.Address;

/**
 * This means that the element has an ID, so that the id can be used to reference 
 * it from multiple places.  This is used where the element is likely to be given a
 * specific name within either the diagram, or across all diagrams within the project.
 * 
 * @author robmoffat
 *
 */
public interface IdentifiableDiagramElement extends PositionableDiagramElement {

	/**
	 * ID should be a project-unique ID to describe this element.  It is also used within the 
	 * XML to allow references between the elements of the XML file. 
	 * 
	 * ID is also used for hashcode and equals.  Set an ID to ensure sorting, maps
	 * and therefore diagram layouts, are deterministic.
	 * 
	 */
	public String getID();
		
	/**
	 * We now force IDs to be set using CompoundIDs, as this ensures that we have actually
	 * thought about what the ID should be.  They are stored as strings within the object though.
	 */
	public void setID(Address s);

	/**
	 * Returns a description for this element, put together loosely from maybe the name or something.
	 * This is a "human readable" way of identifying the element on the diagram.  Should not be relied upon for
	 * code.
	 */
	public String getDescription();
	
	/**
	 * Some human-readable indication of the type of element.
	 */
	public String getType();
	
}
