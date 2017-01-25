package org.kite9.diagram.xml;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.batik.anim.dom.SVG12OMDocument;
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
 * NOTE:  It would be better not to extend SVG12OMDocument, and extend AbstractStyleableDocument,
 * but CSSUtilities does lots of casting to SVGOMDocument, and we want to use that in the
 * kite9-visualisation project.
 * 
 * Now, Kite9 elements seem to be first-class members of SVG?  eh...
 * 
 * @author robmoffat
 *
 */
public class ADLDocument extends SVG12OMDocument {
	
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
//
//    /**
//     * The CSS engine.
//     */
//    protected transient CSSEngine cssEngine;

//
//    /**
//     * Sets the CSS engine.
//     */
//    public void setCSSEngine(CSSEngine ctx) {
//        cssEngine = ctx;
//    }

//    /**
//     * Returns the CSS engine.
//     */
//    public CSSEngine getCSSEngine() {
//    	if (cssEngine == null) {
//    		ADLExtensibleDOMImplementation impl = ADLExtensibleDOMImplementation.getDOMImplementation();
//    		cssEngine = impl.createCSSEngine(this);
//    	}
//    	
//        return cssEngine;
//    }

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

	/**
	 * Maybe move this all into the testing package?
	 */
	private transient Set<Kite9SVGElement> tempConnections = new LinkedHashSet<>();

	public void addConnection(StyledKite9SVGElement xmlElement) {	
		tempConnections.add(xmlElement);
	}

	public Set<Kite9SVGElement> getConnectionElements() {
		return tempConnections;
	}

	public DiagramXMLElement getDocumentElement() {
		return (DiagramXMLElement) super.getDocumentElement();
	}
	
}
