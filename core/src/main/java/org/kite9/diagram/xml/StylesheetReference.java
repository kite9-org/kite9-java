package org.kite9.diagram.xml;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStyleSheetNode;
import org.apache.batik.css.engine.StyleSheet;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGConstants;
import org.kite9.diagram.style.DiagramElement;
import org.w3c.dom.Node;

/**
 * Holds the address (href) of a CSS Stylesheet for a Diagram.
 * 
 * @author robmoffat
 *
 */
public class StylesheetReference extends AbstractStyleableXMLElement implements CSSStyleSheetNode {

	public static final String DEFAULT_STYLESHEET = "";
	
	public StylesheetReference() {
		this(TESTING_DOCUMENT, DEFAULT_STYLESHEET);
	}
	
	public StylesheetReference(ADLDocument owner) {
		super("stylesheet", owner);
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

}
