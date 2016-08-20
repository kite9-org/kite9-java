package org.kite9.diagram.adl;

import org.apache.batik.css.engine.value.Value;
import org.kite9.diagram.position.RenderingInformation;

/**
 * Parent class for all elements of the diagram.
 */
public interface DiagramElement extends Comparable<DiagramElement> {

	public DiagramElement getParent();
	
	/**
	 * ID should be a project-unique ID to describe this element.  It is also used within the 
	 * XML to allow references between the elements of the XML file. 
	 * 
	 * ID is also used for hashcode and equals.  Set an ID to ensure sorting, maps
	 * and therefore diagram layouts, are deterministic.
	 * 
	 * IDs are expected for most elements, but are optional.
	 * 
	 */
	public String getID();

	public RenderingInformation getRenderingInformation();
	
	/**
	 * Returns some CSS style information for the diagram element.
	 */
	public Value getCSSStyleProperty(String prop);

	
	@Deprecated
	public void setRenderingInformation(RenderingInformation ri);
	
	@Deprecated
	public HintMap getPositioningHints();
	
	@Deprecated
	public void setPositioningHints(HintMap hints);
	
	@Deprecated
	public String getShapeName();

}