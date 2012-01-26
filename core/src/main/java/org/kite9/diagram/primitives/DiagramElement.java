package org.kite9.diagram.primitives;

import org.kite9.diagram.position.RenderingInformation;

/**
 * Parent class for all elements of the diagram
 */
public interface DiagramElement extends Comparable<DiagramElement>{

	public RenderingInformation getRenderingInformation();
	

}