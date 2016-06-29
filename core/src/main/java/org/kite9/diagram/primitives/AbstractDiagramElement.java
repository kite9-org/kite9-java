package org.kite9.diagram.primitives;

import org.apache.batik.css.engine.StyleDeclarationProvider;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.dom.AbstractElement;
import org.apache.batik.util.ParsedURL;
import org.kite9.diagram.adl.ADLDocument;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.serialization.XMLHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public abstract class AbstractDiagramElement extends AbstractElement implements XMLDiagramElement, CompositionalDiagramElement {
	
	/**
	 * Used only in test methods.
	 */
	public static ADLDocument TESTING_DOCUMENT = new ADLDocument();
	
	protected String tagName;
	
	protected Object parent;
	
	public AbstractDiagramElement() {
	}

	public AbstractDiagramElement(String name, ADLDocument owner) {
		super(name, owner);
		this.tagName = name;
	}

	public void setClasses(String s) {
		setAttribute("class", s);
	}

	public String getStyle() {
		return getAttribute("style");
	}

	public void setStyle(String s) {
		setAttribute("style", s);
	}

	public StyleMap getComputedStyleMap(String pseudoElement) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setComputedStyleMap(String pseudoElement, StyleMap sm) {
		return;
	
	}

	public String getCSSClass() {
		return getAttribute("class");
	}

	public String getShapeName() {
		// TODO
		return null;
	}
	
	public ParsedURL getCSSBase() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPseudoInstanceOf(String pseudoClass) {
		// TODO Auto-generated method stub
		return false;
	}

	public StyleDeclarationProvider getOverrideStyleDeclarationProvider() {
		// TODO Auto-generated method stub
		return null;
	}
	

	boolean readonly = false;


	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean v) {
		this.readonly = v;
	}
	
	public String getNodeName() {
		return tagName;
	}

	@SuppressWarnings("unchecked")
	public <E extends Element> E getProperty(String name, Class<E> expected) {
		E found = null;
		for (int i = 0; i < getChildNodes().getLength(); i++) {
			Node n = getChildNodes().item(i);
			if ((expected.isInstance(n)) && (((Element)n).getTagName().equals(name))) {
				if (found == null) {
					found = (E) n;
				} else {
					throw new Kite9ProcessingException("Not a unique node name: "+name);
				}
			}
		}
	
		return found;
	}
	
	public <E extends Element> E replaceProperty(String propertyName, E e, Class<E> propertyClass) {
		E existing = getProperty(propertyName, propertyClass);
		if (e == null) {
			if (existing != null) {
				this.removeChild(existing);
			}
		 	return null;
		}

		if (!propertyClass.isInstance(e)) {
			throw new Kite9ProcessingException("Was expecting an element of "+propertyClass.getName()+" but it's: "+e);
		}

		if (e instanceof XMLDiagramElement) {
			((XMLDiagramElement)e).setTagName(propertyName);
			((XMLDiagramElement)e).setOwnerDocument((ADLDocument) this.ownerDocument); 
		}
		
		if (!e.getNodeName().equals(propertyName)) {
			throw new Kite9ProcessingException("Incorrect name.  Expected "+propertyName+" but was "+e.getNodeName());
		}
		
		if (existing != null) {
			this.removeChild(existing);
		}
		
		this.appendChild(e);
		
		return e;
	}
	
	public void setTagName(String name) {
		this.tagName = name;
	}

	protected String getTextData() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getChildNodes().getLength(); i++) {
			Node n = getChildNodes().item(i);
			if (n instanceof Text) {
				sb.append(((Text) n).getData());
			} 
		}
		
		return sb.toString().trim();
	}

	protected void setTextData(String text) {
		int i = 0;
		while (i < getChildNodes().getLength()) {
			Node child = getChildNodes().item(i);
			if (child instanceof Text) {
				removeChild(child);
			} else {
				i++;
			}
		}
		
		appendChild(ownerDocument.createTextNode(text));
	}

	@Override
	public String getNamespaceURI() {
		return XMLHelper.KITE9_NAMESPACE;
	}

	@Override
	public String getLocalName() {
		return getNodeName();
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		if (value == null) {
			removeAttribute(name);
		} else {
			super.setAttribute(name, value);
		}
	}
	
	public Object getParent() {
		return parent;
	}
	
	public void setParent(Object parent) {
		this.parent = parent;
	}

	public void setOwnerDocument(ADLDocument doc) {
		this.ownerDocument = doc;
	}
}