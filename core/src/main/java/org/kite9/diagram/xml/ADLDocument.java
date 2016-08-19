package org.kite9.diagram.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.anim.dom.SVGOMDocument;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.apache.batik.util.XMLConstants;
import org.kite9.framework.serialization.ADLExtensibleDOMImplementation;
import org.kite9.framework.serialization.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.DocumentCSS;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 * NOTE:  It would be better not to extend SVGOMDocument, and extend AbstractStyleableDocument,
 * but CSSUtilities does lots of casting to SVGOMDocument, and we want to use that in the
 * kite9-visualisation project.
 * 
 * @author robmoffat
 *
 */
public class ADLDocument extends SVGOMDocument {
	
	/**
     * Is this document immutable?
     */
    protected boolean readonly;

	public ADLDocument() {
		this(new ADLExtensibleDOMImplementation());
	}

	public ADLDocument(ADLExtensibleDOMImplementation impl) {
		super(null, impl);
	}

	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		return ((ADLExtensibleDOMImplementation)implementation).createElementNS(this, namespaceURI, qualifiedName);
	}

	public Element createElement(String name) throws DOMException {
		return ((ADLExtensibleDOMImplementation)implementation).createElementNS(this, XMLHelper.KITE9_NAMESPACE, name);
	}
	
	 /**
     * Returns true if the given Attr node represents an 'id'
     * for this document.
     */
    public boolean isId(Attr node) {
        return XMLConstants.XML_ID_ATTRIBUTE.equals(node.getNodeName());
    }

    /**
     * The default view.
     */
    protected transient AbstractView defaultView;

    /**
     * The CSS engine.
     */
    protected transient CSSEngine cssEngine;


    /**
     * Sets the CSS engine.
     */
    public void setCSSEngine(CSSEngine ctx) {
        cssEngine = ctx;
    }

    /**
     * Returns the CSS engine.
     */
    public CSSEngine getCSSEngine() {
    	if (cssEngine == null) {
    		ADLExtensibleDOMImplementation impl = (ADLExtensibleDOMImplementation) getImplementation();
    		cssEngine = impl.createCSSEngine(this);
    	}
    	
        return cssEngine;
    }

    // DocumentStyle /////////////////////////////////////////////////////////

    public StyleSheetList getStyleSheets() {
        throw new RuntimeException(" !!! Not implemented");
    }

    // DocumentView ///////////////////////////////////////////////////////////

    /**
     * <b>DOM</b>: Implements {@link DocumentView#getDefaultView()}.
     * @return a ViewCSS object.
     */
    public AbstractView getDefaultView() {
        if (defaultView == null) {
            ExtensibleDOMImplementation impl;
            impl = (ExtensibleDOMImplementation)implementation;
            defaultView = impl.createViewCSS(this);
        }
        return defaultView;
    }

    /**
     * Clears the view CSS.
     */
    public void clearViewCSS() {
        defaultView = null;
        if (cssEngine != null) {
            cssEngine.dispose();
        }
        cssEngine = null;
    }

    // DocumentCSS ////////////////////////////////////////////////////////////

    /**
     * <b>DOM</b>: Implements
     * {@link DocumentCSS#getOverrideStyle(Element,String)}.
     */
    public CSSStyleDeclaration getOverrideStyle(Element elt,
                                                String pseudoElt) {
        throw new RuntimeException(" !!! Not implemented");
    }
    
    /**
     * Tests whether this node is readonly.
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Sets this node readonly attribute.
     */
    public void setReadonly(boolean v) {
        readonly = v;
    }

	@Override
	protected Node newNode() {
		return new ADLDocument();
	}

	private transient Map<String, Collection<XMLElement>> references = new HashMap<>();
	
	public Collection<XMLElement> getReferences(String id) {
		Collection<XMLElement> collection = references.get(id);
		return collection == null ? Collections.emptySet() : collection;
	}

	public void addReference(String fromId, XMLElement to) {
		Collection<XMLElement> c = references.get(fromId);
		if (c == null) {
			c = new ArrayList<>(3);
			references.put(fromId, c);
		}
		
		if (!c.contains(fromId)) {
			c.add(to);
		}
	}

}
