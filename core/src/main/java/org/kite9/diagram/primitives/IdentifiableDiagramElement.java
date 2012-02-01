package org.kite9.diagram.primitives;

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
	
	public void setID(String s);

	
}
