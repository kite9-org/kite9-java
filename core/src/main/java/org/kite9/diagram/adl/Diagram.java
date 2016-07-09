package org.kite9.diagram.adl;

import java.util.List;

import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.AbstractConnectedContainer;
import org.kite9.diagram.primitives.Connection;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.Label;
import org.kite9.diagram.primitives.StylesheetReference;
import org.w3c.dom.Node;


/**
 * This class represents a whole diagram within ADL.  A diagram itself is a container of either Glyphs or
 * contexts.  It also has a key explaining what Symbol's mean.
 * 
 * Arrows and ArrowLinks are implicitly contained in the diagram since they are connected to the Glyphs.
 * 
 * @author robmoffat
 *
 */
public class Diagram extends AbstractConnectedContainer {

	private static final long serialVersionUID = -7727042271665853389L;
	
	public Diagram() {
		super(createID(), "diagram", TESTING_DOCUMENT);
	}
	
	public Diagram(String id, ADLDocument doc) {
		super(id, "diagram", doc);
		this.setParentNode(doc);
	}

	public Diagram(String id, List<Contained> contents, Key k) {
		this(id, contents, k, TESTING_DOCUMENT);
	}

	
	public Diagram(String id, List<Contained> contents, Key k, ADLDocument doc) {
		this(id, doc);
		this.setParentNode(doc);
		if (contents != null) {
			for (Contained contained : contents) {
				appendChild(contained);
			}
		}
	}
	
	public Diagram(String id, List<Contained> contents) {
		this(id, contents, null, TESTING_DOCUMENT);
	}

	public Diagram(String id, List<Contained> contents, Layout l, Key k) {
		this(id, contents, k, TESTING_DOCUMENT);
		this.setLayoutDirection(l);
	}

	public Diagram(List<Contained> contents, Key k) {
		this(createID(), contents, k);
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

	public String getName() {
		return getAttribute("name");
	}
	
	public void setName(String name) {
		setAttribute("name", name);
	}
	
	@SuppressWarnings("unchecked")
	public ContainerProperty<Connection> getAllLinks() {
		ContainerProperty<Connection> out = getProperty("allLinks", ContainerProperty.class);
		if (out == null) {
			out = replaceProperty("allLinks", (ContainerProperty<Connection>) ownerDocument.createElement("allLinks"), ContainerProperty.class);
		}
		
		return out;
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
	

	public Container getContainer() {
		return null;
	}

	@Override
	public void setContainer(Container c) {
		// diagrams don't have containers.
	}

	public StylesheetReference getStylesheetReference() {
		return getProperty("stylesheet", StylesheetReference.class);
	}
	
	public void setStylesheetReference(StylesheetReference ref) {
		replaceProperty("stylesheet", ref, StylesheetReference.class);
	}
	
}
