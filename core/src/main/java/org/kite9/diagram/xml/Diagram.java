package org.kite9.diagram.xml;

import java.util.List;

import org.kite9.diagram.adl.Container;
import org.kite9.diagram.position.Layout;
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
public class Diagram extends AbstractXMLContainerElement {

	private static final long serialVersionUID = -7727042271665853389L;
	
	public Diagram() {
		super(createID(), "diagram", TESTING_DOCUMENT);
	}
	
	public Diagram(String id, ADLDocument doc) {
		super(id, "diagram", doc);
		doc.appendChild(this);
	}

	public Diagram(String id, List<XMLElement> contents, Key k) {
		this(id, contents, k, TESTING_DOCUMENT);
	}

	
	public Diagram(String id, List<XMLElement> contents, Key k, ADLDocument doc) {
		this(id, doc);
		this.setParentNode(doc);
		if (contents != null) {
			for (XMLElement contained : contents) {
				appendChild(contained);
			}
		}
		if (k != null) {
			replaceProperty("key", k);
		}
	}
	
	public Diagram(String id, List<XMLElement> contents) {
		this(id, contents, null, TESTING_DOCUMENT);
	}

	public Diagram(String id, List<XMLElement> contents, Layout l, Key k) {
		this(id, contents, k, TESTING_DOCUMENT);
		this.setLayoutDirection(l);
	}

	public Diagram(List<XMLElement> contents, Key k) {
		this(createID(), contents, k);
	}

	public Key getKey() {
		return getProperty("key");
	}

	public void setKey(Key k) {
	    replaceProperty("key", k);
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
	
	public ContainerProperty getAllLinks() {
		ContainerProperty out = getProperty("allLinks");
		if (out == null) {
			out = replaceProperty("allLinks", (ContainerProperty) ownerDocument.createElement("allLinks"));
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
	public XMLElement getLabel() {
		return getKey();
	}
	

	public Container getContainer() {
		return null;
	}

	public StylesheetReference getStylesheetReference() {
		return getProperty("stylesheet");
	}
	
	public void setStylesheetReference(StylesheetReference ref) {
		replaceProperty("stylesheet", ref);
	}
	
//	public Container getDiagramElement() {
//		
//	}
}
