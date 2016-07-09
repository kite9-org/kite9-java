package org.kite9.diagram.adl;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.apache.batik.dom.GenericAttr;
import org.apache.batik.dom.GenericAttrNS;
import org.apache.batik.dom.GenericCDATASection;
import org.apache.batik.dom.GenericComment;
import org.apache.batik.dom.GenericDocumentFragment;
import org.apache.batik.dom.GenericEntityReference;
import org.apache.batik.dom.GenericProcessingInstruction;
import org.apache.batik.dom.GenericText;
import org.apache.batik.dom.StyleSheetProcessingInstruction;
import org.apache.batik.util.XMLConstants;
import org.kite9.framework.serialization.ADLExtensibleDOMImplementation;
import org.kite9.framework.serialization.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.DocumentCSS;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class ADLDocument extends AbstractStylableDocument {
	
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
        if (node.getNamespaceURI() != null) return false;
        return XMLConstants.XML_ID_ATTRIBUTE.equals(node.getNodeName());
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createDocumentFragment()}.
     */
    public DocumentFragment createDocumentFragment() {
        return new GenericDocumentFragment(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createTextNode(String)}.
     */
    public Text createTextNode(String data) {
        return new GenericText(data, this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createComment(String)}.
     */
    public Comment createComment(String data) {
        return new GenericComment(data, this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createCDATASection(String)}.
     */
    public CDATASection createCDATASection(String data) throws DOMException {
        return new GenericCDATASection(data, this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createProcessingInstruction(String,String)}.
     * @return a {@link StyleSheetProcessingInstruction} if target is
     *         "xml-stylesheet" or a GenericProcessingInstruction otherwise.
     */
    public ProcessingInstruction createProcessingInstruction(String target,
                                                             String data)
        throws DOMException {
        return new GenericProcessingInstruction(target, data, this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createAttribute(String)}.
     */
    public Attr createAttribute(String name) throws DOMException {
        return new GenericAttr(name.intern(), this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createEntityReference(String)}.
     */
    public EntityReference createEntityReference(String name)
        throws DOMException {
        return new GenericEntityReference(name, this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createAttributeNS(String,String)}.
     */
    public Attr createAttributeNS(String namespaceURI, String qualifiedName)
        throws DOMException {
        if (namespaceURI != null && namespaceURI.length() == 0) {
            namespaceURI = null;
        }
        if (namespaceURI == null) {
            return new GenericAttr(qualifiedName.intern(), this);
        } else {
            return new GenericAttrNS(namespaceURI.intern(),
                                     qualifiedName.intern(),
                                     this);
        }
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

}
