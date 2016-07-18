package org.kite9.framework.serialization;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.SystemColorSupport;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.SVGConstants;
import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.style.StyledDiagramElement;
import org.w3c.dom.Element;


public final class ADLContext extends BridgeContext implements CSSContext {
	private final ADLDocument ownerDocument;

	public ADLContext(ADLDocument ownerDocument) {
		this.ownerDocument = ownerDocument;
	}

	public boolean isInteractive() {
		return false;
	}

	public boolean isDynamic() {
		return false;
	}

	public Value getSystemColor(String ident) {
	    return SystemColorSupport.getSystemColor(ident);
	}

	public float getPixelUnitToMillimeter() {
	    return 0.26458333333333333333333333333333f; // 96dpi
	}

	public float getPixelToMillimeter() {
		return getPixelUnitToMillimeter();
	}

	public float getMediumFontSize() {
	    return 9f * 25.4f / (72f * getPixelUnitToMillimeter());
	}

	public float getLighterFontWeight(float f) {
	    // Round f to nearest 100...
	    int weight = ((int)((f+50)/100))*100;
	    switch (weight) {
	    case 100: return 100;
	    case 200: return 100;
	    case 300: return 200;
	    case 400: return 300;
	    case 500: return 400;
	    case 600: return 400;
	    case 700: return 400;
	    case 800: return 400;
	    case 900: return 400;
	    default:
	        throw new IllegalArgumentException("Bad Font Weight: " + f);
	    }
	}

	public Value getDefaultFontFamily() {
	    // No cache needed since the default font family is asked only
	    // one time on the root element (only if it does not have its
	    // own font-family).
		StyledDiagramElement root = (StyledDiagramElement)ownerDocument.getFirstChild();
	    String str = "Arial, Helvetica, sans-serif";
	    return ownerDocument.getCSSEngine().parsePropertyValue
	        (root,SVGConstants.CSS_FONT_FAMILY_PROPERTY, str);
	}

	public CSSEngine getCSSEngineForElement(Element e) {
		ADLDocument doc = (ADLDocument)e.getOwnerDocument();
	    return doc.getCSSEngine();
	}

	public float getBolderFontWeight(float f) {
	    // Round f to nearest 100...
	    int weight = ((int)((f+50)/100))*100;
	    switch (weight) {
	    case 100: return 600;
	    case 200: return 600;
	    case 300: return 600;
	    case 400: return 600;
	    case 500: return 600;
	    case 600: return 700;
	    case 700: return 800;
	    case 800: return 900;
	    case 900: return 900;
	    default:
	        throw new IllegalArgumentException("Bad Font Weight: " + f);
	    }
	}

	public float getBlockWidth(Element elt) {
	    throw new UnsupportedOperationException("We don't support viewports");
	}

	public float getBlockHeight(Element elt) {
	    throw new UnsupportedOperationException("We don't support viewports");
	}

	public void checkLoadExternalResource(ParsedURL externalResourceURL, ParsedURL docURL) throws SecurityException {
		 
	}
}