package org.kite9.diagram.adl;

import org.kite9.diagram.position.DiagramRenderingInformation;
import org.kite9.diagram.primitives.AbstractConnectedContainer;
import org.kite9.diagram.primitives.Connection;
import org.kite9.diagram.primitives.Label;
import org.w3c.dom.Node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * This class represents a whole diagram within ADL.  A diagram itself is a container of either Glyphs or
 * contexts.  It also has a key explaining what Symbol's mean.
 * 
 * Arrows and ArrowLinks are implicitly contained in the diagram since they are connected to the Glyphs.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("diagram")
public class Diagram extends AbstractConnectedContainer {

	private static final long serialVersionUID = -7727042271665853389L;
	
	public Diagram() {
		this.tagName = "diagram";
	}
	
	public Diagram(String id, ADLDocument doc) {
		super(id, "diagram", doc);
	}

	public Key getKey() {
		return getProperty("key", Key.class);
	}

	public void setKey(Key k) {
	    replaceProperty("key", k, Key.class);
	}

	public boolean isBordered() {
		return true;
	}

	@XStreamAsAttribute
	String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@SuppressWarnings("unchecked")
	public ContainerProperty<Connection> getAllLinks() {
		return getProperty("allLinks", ContainerProperty.class);
	}
	
	public DiagramRenderingInformation getRenderingInformation() {
		if (renderingInformation==null) {
			renderingInformation = new DiagramRenderingInformation();
		}
		
		return (DiagramRenderingInformation) renderingInformation;
	}

	public String getNodeName() {
		return "diagram";
	}

	@Override
	protected Node newNode() {
		return new Diagram();
	}

	@Override
	public Label getLabel() {
		return getKey();
	}
	
	
}
