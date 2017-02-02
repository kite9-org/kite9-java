package org.kite9.diagram.xml;

import java.util.Collections;
import java.util.Iterator;

import org.apache.batik.anim.dom.SVGOMStyleElement;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStyleSheetNode;
import org.apache.batik.css.engine.StyleSheet;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGConstants;
import org.kite9.diagram.adl.DiagramElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Holds the address (href) of a CSS Stylesheet for a Diagram.
 * 
 * @author robmoffat
 * 
 * @TODO: replace with SVGOMStyleElement
 *
 */
public class StylesheetReference extends SVGOMStyleElement implements CSSStyleSheetNode, XMLElement {

	public static final String DEFAULT_STYLESHEET = "";
	
	public StylesheetReference() {
		this(AbstractStyleableXMLElement.TESTING_DOCUMENT, DEFAULT_STYLESHEET);
	}
	
	private String tagName;
	
	public StylesheetReference(ADLDocument owner) {
		super(null, owner);
		this.tagName = "stylesheet";
	}

	public StylesheetReference(ADLDocument owner, String href) {
		this(owner);
		setHref(href);
	}

	public int compareTo(DiagramElement o) {
		if (o instanceof StylesheetReference) {
			return this.getHref().compareTo(((StylesheetReference) o).getHref());
		} else {
			return 0;
		}
	}

    /**
     * The DOM CSS style-sheet.
     */
    protected transient StyleSheet styleSheet;
	
	/**
     * Returns the associated style-sheet.
     * TODO: Also need to handle in-line style sheets.
     */
    public StyleSheet getCSSStyleSheet() {
        if (styleSheet == null) {
            ADLDocument doc = (ADLDocument)getOwnerDocument();
            CSSEngine e = doc.getCSSEngine();
            String bu = getHref();
            ParsedURL burl = new ParsedURL(getBaseURI(), bu);
            String media = getMedia();
            styleSheet = e.parseStyleSheet(burl, media);
        }
        return styleSheet;
    }
	
	public String getHref() {
		return getAttribute("href");
	}
	
	public void setHref(String href) {
		setAttribute("href", href);
	}
	
	public String getMedia() {
		return getAttribute(SVGConstants.SVG_MEDIA_ATTRIBUTE);
	}
	
	public void setMedia(String media) {
		setAttribute(SVGConstants.SVG_MEDIA_ATTRIBUTE, media);
	}
		
	@Override
	protected Node newNode() {
		return new StylesheetReference();
	}

	@Override
	public Iterator<XMLElement> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public String getID() {
		return getHref();
	}

	@Override
	public void setTagName(String tag) {
		this.tagName = tag;
	}

	@Override
	public void setOwnerDocument(ADLDocument doc) {
		super.setOwnerDocument(doc);
	}

	@Override
	public int getChildXMLElementCount() {
		return 0;
	}

	@Override
	public DiagramElement getDiagramElement() {
		throw new UnsupportedOperationException();

	}

	@Override
	public ADLDocument getOwnerDocument() {
		return (ADLDocument) super.getOwnerDocument();
	}

	@Override
	public <E extends Element> E getProperty(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends Element> E replaceProperty(String propertyName, E e) {
		throw new UnsupportedOperationException();
	}

	public String getTagName() {
		return tagName;
	}
}
