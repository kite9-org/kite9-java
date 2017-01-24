package org.kite9.framework.serialization;

import static org.kite9.framework.serialization.CSSConstants.BOX_SHADOW_COLOR_PROPERTY;
import static org.kite9.framework.serialization.CSSConstants.BOX_SHADOW_OPACITY_PROPERTY;
import static org.kite9.framework.serialization.CSSConstants.BOX_SHADOW_X_OFFSET_PROPERTY;
import static org.kite9.framework.serialization.CSSConstants.BOX_SHADOW_Y_OFFSET_PROPERTY;
import static org.kite9.framework.serialization.CSSConstants.PADDING_LEFT_PROPERTY;
import static org.kite9.framework.serialization.CSSConstants.PADDING_RIGHT_PROPERTY;
import static org.kite9.framework.serialization.CSSConstants.PADDING_TOP_PROPERTY;

import java.net.URL;

import org.apache.batik.anim.dom.SVG12DOMImplementation;
import org.apache.batik.css.dom.CSSOMSVGViewCSS;
import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSEngineUserAgent;
import org.apache.batik.css.engine.SVG12CSSEngine;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.RGBColorValue;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.engine.value.svg.ColorManager;
import org.apache.batik.css.engine.value.svg.OpacityManager;
import org.apache.batik.css.engine.value.svg12.MarginLengthManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.util.ParsedURL;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.style.BorderTraversal;
import org.kite9.diagram.style.DiagramElementSizing;
import org.kite9.diagram.style.DiagramElementType;
import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.DiagramXMLElement;
import org.kite9.diagram.xml.GenericXMLElement;
import org.kite9.diagram.xml.StylesheetReference;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.stylesheets.StyleSheet;

public class ADLExtensibleDOMImplementation extends SVG12DOMImplementation {
	
	public static final boolean USE_GENERIC_XML_ELEMENT = true;

	public ADLExtensibleDOMImplementation() {
		super();
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "diagram", new ElementFactory() {
			 
			public Element create(String prefix, Document doc) {
				DiagramXMLElement out = new DiagramXMLElement((ADLDocument) doc);
				out.setOwnerDocument(doc);
				return out;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "stylesheet", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				StylesheetReference out = new StylesheetReference((ADLDocument) doc);
				return out;
			}
		});
		
		// PADDING CSS
		registerCustomCSSShorthandManager(new PaddingShorthandManager());
		registerCustomCSSValueManager(new MarginLengthManager(PADDING_LEFT_PROPERTY));
		registerCustomCSSValueManager(new MarginLengthManager(PADDING_RIGHT_PROPERTY));
		registerCustomCSSValueManager(new MarginLengthManager(PADDING_TOP_PROPERTY));
		registerCustomCSSValueManager(new MarginLengthManager(CSSConstants.PADDING_BOTTOM_PROPERTY));
		
		
		// SHADOW CSS
		registerCustomCSSShorthandManager(new BoxShadowShorthandManager());
		registerCustomCSSValueManager(new MarginLengthManager(BOX_SHADOW_X_OFFSET_PROPERTY));
		registerCustomCSSValueManager(new MarginLengthManager(BOX_SHADOW_Y_OFFSET_PROPERTY));
		registerCustomCSSValueManager(new OpacityManager(BOX_SHADOW_OPACITY_PROPERTY, false));
		ColorManager colourManager = new ColorManager() {
			
			@Override
			public Value getDefaultValue() {
				return NO_COLOR;
			}

			@Override
			public String getPropertyName() {
				return BOX_SHADOW_COLOR_PROPERTY;
			}
			
		};
		
		/**
		 * This makes 'no colour' available to all the colour managers, since the 
		 * colour list is static.
		 */
		colourManager.getIdentifiers().put("none", NO_COLOR);
		registerCustomCSSValueManager(colourManager) ;
		
		
		// ELEMENT TYPE / SIZING / LAYOUT CONTROL
		registerCustomCSSValueManager(new EnumManager(CSSConstants.ELEMENT_TYPE_PROPERTY, DiagramElementType.class, DiagramElementType.UNSPECIFIED));
		registerCustomCSSValueManager(new EnumManager(CSSConstants.ELEMENT_SIZING_PROPERTY, DiagramElementSizing.class, DiagramElementSizing.UNSPECIFIED));
		registerCustomCSSValueManager(new EnumManager(CSSConstants.LAYOUT_PROPERTY, Layout.class, null));
		registerCustomCSSValueManager(new IntegerRangeManager(CSSConstants.GRID_OCCUPIES_X_PROPERTY));
		registerCustomCSSValueManager(new IntegerRangeManager(CSSConstants.GRID_OCCUPIES_Y_PROPERTY));
		registerCustomCSSValueManager(new GridSizeManager(CSSConstants.GRID_ROWS_PROPERTY));
		registerCustomCSSValueManager(new GridSizeManager(CSSConstants.GRID_COLUMNS_PROPERTY));
		registerCustomCSSShorthandManager(new GridSizeShorthandManager());
		registerCustomCSSShorthandManager(new OccupiesShorthandManager());
		
		// CONNECTION TRAVERSAL
		registerCustomCSSValueManager(new EnumManager(CSSConstants.TRAVERSAL_BOTTOM_PROPERTY, BorderTraversal.class, BorderTraversal.LEAVING));
		registerCustomCSSValueManager(new EnumManager(CSSConstants.TRAVERSAL_RIGHT_PROPERTY, BorderTraversal.class, BorderTraversal.LEAVING));
		registerCustomCSSValueManager(new EnumManager(CSSConstants.TRAVERSAL_LEFT_PROPERTY, BorderTraversal.class, BorderTraversal.LEAVING));
		registerCustomCSSValueManager(new EnumManager(CSSConstants.TRAVERSAL_TOP_PROPERTY, BorderTraversal.class, BorderTraversal.LEAVING));
		registerCustomCSSShorthandManager(new TraversalShorthandManager());
		
		
	}

	public static final RGBColorValue NO_COLOR = new RGBColorValue(
			new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, -1),
			new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, -1),
			new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, -1));


	@Override
	public Element createElementNS(AbstractDocument document, String namespaceURI, String qualifiedName) {
		if (USE_GENERIC_XML_ELEMENT) {
			if (XMLHelper.KITE9_NAMESPACE.equals(namespaceURI)) {
				if ((!"diagram".equals(qualifiedName)) && (!"stylesheet".equals(qualifiedName))) {
					return new GenericXMLElement(qualifiedName, (ADLDocument) document);
				}
			} 
		}
		
		return super.createElementNS(document, namespaceURI, qualifiedName);
	}

	public CSSStyleSheet createCSSStyleSheet(String title, String media) throws DOMException {
        throw new UnsupportedOperationException("StyleSheetFactory.createCSSStyleSheet is not implemented"); // XXX
	}

	public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
		ADLDocument out = new ADLDocument(this);
		createElementNS(out, XMLHelper.KITE9_NAMESPACE, "diagram");
		return out;
	}

	public StyleSheet createStyleSheet(Node node, HashTable pseudoAttrs) {
        throw new UnsupportedOperationException("StyleSheetFactory.createStyleSheet is not implemented"); // XXX
	}

	public CSSEngine createCSSEngine(AbstractStylableDocument doc, CSSContext ctx, ExtendedParser ep, ValueManager[] vms, ShorthandManager[] sms) {
		ParsedURL durl = null; // ((ADLDocument)doc).getParsedURL();
		CSSEngine result = new SVG12CSSEngine(doc, durl, ep, vms, sms, ctx);

		URL url = getClass().getResource("resources/UserAgentStyleSheet.css");
		if (url != null) {
			ParsedURL purl = new ParsedURL(url);
			InputSource is = new InputSource(purl.toString());
			result.setUserAgentStyleSheet(result.parseStyleSheet(is, purl, "all"));
		}

		return result;
	}
	
	public CSSEngine createCSSEngine(ADLDocument doc) {
		CSSEngine e = super.createCSSEngine(doc, new ADLContext(doc));
		e.setCSSEngineUserAgent(new CSSEngineUserAgent() {

			public void displayMessage(String message) {
				System.out.println("message");
			}

			public void displayError(Exception ex) {
				throw new RuntimeException(ex);
			}
		});

		doc.setCSSEngine(e);
		return e;
	}
	

	@Override
	public ViewCSS createViewCSS(AbstractStylableDocument doc) {
        return new CSSOMSVGViewCSS(doc.getCSSEngine());
	}
	
	public static final DOMImplementation DOM_IMPLEMENTATION = new ADLExtensibleDOMImplementation();
	
	/**
     * Returns the default instance of this class.
     */
    public static DOMImplementation getDOMImplementation() {
        return DOM_IMPLEMENTATION;
    }
}
