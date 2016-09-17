package org.kite9.framework.serialization;

import java.net.URL;

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
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.util.ParsedURL;
import org.kite9.diagram.style.DiagramElementSizing;
import org.kite9.diagram.style.DiagramElementType;
import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.DiagramXMLElement;
import org.kite9.diagram.xml.GenericXMLElement;
import org.kite9.diagram.xml.StylesheetReference;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.stylesheets.StyleSheet;

public class ADLExtensibleDOMImplementation extends ExtensibleDOMImplementation {
	
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
		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "glyph", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				Glyph g = new Glyph();
//				g.setOwnerDocument(doc);
//				return g;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "label", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				TextLine g = new TextLine(null, "label", null, null, (ADLDocument) doc);
//				return g;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "stereotype", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				TextLine g = new TextLine(null, "stereotype", null, null, (ADLDocument) doc);
//				return g;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "boldText", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				TextLine g = new TextLine(null, "boldText", null, null, (ADLDocument) doc);
//				return g;
//			}
//		});
//
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "bodyText", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				TextLine g = new TextLine(null, "bodyText", null, null, (ADLDocument) doc);
//				return g;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "symbols", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				return new ContainerProperty("symbols", (ADLDocument) doc);
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "text-lines", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				return new ContainerProperty("text-lines", (ADLDocument) doc);
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "allLinks", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				return new ContainerProperty("allLinks", (ADLDocument) doc);
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "text-line", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				TextLine out = new TextLine();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "symbol", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				Symbol out = new Symbol();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "arrow", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				Arrow out = new Arrow();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "context", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				Context out = new Context();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "link", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				Link out = new Link();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "key", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				Key out = new Key();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});
//		
//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "fromDecoration", new ElementFactory() {
//			
//			public Element create(String prefix, Document doc) {
//				GenericElement out = new Key();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});

//		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "key", new ElementFactory() {
//	
//			public Element create(String prefix, Document doc) {
//				Key out = new Key();
//				out.setOwnerDocument(doc);
//				return out;
//			}
//		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "stylesheet", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				StylesheetReference out = new StylesheetReference((ADLDocument) doc);
				return out;
			}
		});
		
		registerCustomCSSShorthandManager(new PaddingShorthandManager());
		
		registerCustomCSSValueManager(new MarginLengthManager("padding-left"));
		registerCustomCSSValueManager(new MarginLengthManager("padding-right"));
		registerCustomCSSValueManager(new MarginLengthManager("padding-top"));
		registerCustomCSSValueManager(new MarginLengthManager("padding-bottom"));
		
		registerCustomCSSShorthandManager(new BoxShadowShorthandManager());
		registerCustomCSSValueManager(new MarginLengthManager("box-shadow-x-offset"));
		registerCustomCSSValueManager(new MarginLengthManager("box-shadow-y-offset"));
		registerCustomCSSValueManager(new OpacityManager("box-shadow-opacity", false));
		ColorManager colourManager = new ColorManager() {
			
			@Override
			public Value getDefaultValue() {
				return NO_COLOR;
			}

			@Override
			public String getPropertyName() {
				return "box-shadow-color";
			}
			
		};
		
		registerCustomCSSValueManager(new EnumManager(CSSConstants.ELEMENT_TYPE_PROPERTY, DiagramElementType.class, DiagramElementType.UNSPECIFIED));
		registerCustomCSSValueManager(new EnumManager(CSSConstants.ELEMENT_SIZING_PROPERTY, DiagramElementSizing.class, DiagramElementSizing.UNSPECIFIED));
		
		/**
		 * This makes 'no colour' available to all the colour managers, since the 
		 * colour list is static.
		 */
		colourManager.getIdentifiers().put("none", NO_COLOR);
		
		
		registerCustomCSSValueManager(colourManager) ;
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
		Element root = createElementNS(out, XMLHelper.KITE9_NAMESPACE, "diagram");
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
}
