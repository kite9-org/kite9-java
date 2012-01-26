package org.kite9.diagram.adl;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractConnectedContained;
import org.kite9.diagram.primitives.Leaf;
import org.kite9.diagram.primitives.VertexOnEdge;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This class models the black body of the arrow, which will have links 
 * entering and leaving it.
 * 
 * @author moffatr
 *
 */
@XStreamAlias("arrow")
public class Arrow extends AbstractConnectedContained implements VertexOnEdge, Leaf {

	private static final long serialVersionUID = 5054715961820271315L;

	public Arrow() {
	}
	
	@XStreamAsAttribute
	private String label;
		
	public String getLabel() {
		return label;
	}

	public void setLabel(String name) {
		this.label = name;
	}

	
	public Arrow(String id, String label) {
		super(id);
		this.label = label;
	}
	
	public Arrow(String label) {
		this(createID(), label);
	}
	
	public boolean hasDimension() {
		return true;
	}

	public String toString() {
		return "[A:"+getID()+"]";
	}
	
	public RenderingInformation getRenderingInformation() {
		if (renderingInformation==null)
			renderingInformation = new RectangleRenderingInformation();
		
		return renderingInformation;
	}

}
