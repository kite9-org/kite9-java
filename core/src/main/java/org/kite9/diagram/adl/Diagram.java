package org.kite9.diagram.adl;

import java.util.LinkedHashSet;
import java.util.List;

import org.kite9.diagram.position.DiagramRenderingInformation;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.AbstractConnectedContainer;
import org.kite9.diagram.primitives.Connection;
import org.kite9.diagram.primitives.Contained;

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
	}
	
	public Diagram(List<Contained> contained, Key key) {
		super(createID(), contained, null, key);
	}
	
	public Diagram(String id, List<Contained> contained, Key key) {
		super(id, contained, null, key);
	}
	
	public Diagram(String id, List<Contained> contained, Layout d, Key key) {
		super(id, contained, d, key);
	}

	public Key getKey() {
		return (Key) label;
	}

	public void setKey(Key k) {
	    this.label = k;
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
	
	/**
	 * This is used by XStream to hold a set of all the links within the diagram.  
	 */
	private LinkedHashSet<Connection> allLinks = new LinkedHashSet<Connection>();

	public LinkedHashSet<Connection> getAllLinks() {
		return allLinks;
	}
	
	public DiagramRenderingInformation getRenderingInformation() {
		if (renderingInformation==null) {
			renderingInformation = new DiagramRenderingInformation();
		}
		
		return (DiagramRenderingInformation) renderingInformation;
	}
	
	public String getDescription() {
		if (name != null) {
			return name;
		} else {
			return "";
		}
	}

	public String getType() {
		return "diagram";
	}
}
