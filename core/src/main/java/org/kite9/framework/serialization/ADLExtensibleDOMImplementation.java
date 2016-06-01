package org.kite9.framework.serialization;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.dom.ExtensibleDOMImplementation;
import org.apache.batik.dom.util.HashTable;
import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.ContainerProperty;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Key;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.stylesheets.StyleSheet;

public class ADLExtensibleDOMImplementation extends ExtensibleDOMImplementation {

	public ADLExtensibleDOMImplementation() {
		super();
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "diagram", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				Diagram out = new Diagram();
				out.setOwnerDocument(doc);
				return out;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "glyph", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				Glyph g = new Glyph();
				g.setOwnerDocument(doc);
				return g;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "label", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				TextLine g = new TextLine(null, "label", null, null, (ADLDocument) doc);
				return g;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "stereotype", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				TextLine g = new TextLine(null, "stereotype", null, null, (ADLDocument) doc);
				return g;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "boldText", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				TextLine g = new TextLine(null, "boldText", null, null, (ADLDocument) doc);
				return g;
			}
		});

		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "bodyText", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				TextLine g = new TextLine(null, "bodyText", null, null, (ADLDocument) doc);
				return g;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "symbols", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				return new ContainerProperty<Symbol>("symbols", (ADLDocument) doc);
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "text-lines", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				return new ContainerProperty<CompositionalDiagramElement>("text-lines", (ADLDocument) doc);
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "allLinks", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				return new ContainerProperty<CompositionalDiagramElement>("allLinks", (ADLDocument) doc);
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "text-line", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				TextLine out = new TextLine();
				out.setOwnerDocument(doc);
				return out;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "symbol", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				Symbol out = new Symbol();
				out.setOwnerDocument(doc);
				return out;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "arrow", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				Arrow out = new Arrow();
				out.setOwnerDocument(doc);
				return out;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "context", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				Context out = new Context();
				out.setOwnerDocument(doc);
				return out;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "link", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				Link out = new Link();
				out.setOwnerDocument(doc);
				return out;
			}
		});
		
		registerCustomElementFactory(XMLHelper.KITE9_NAMESPACE, "key", new ElementFactory() {
			
			public Element create(String prefix, Document doc) {
				Key out = new Key();
				out.setOwnerDocument(doc);
				return out;
			}
		});
	}
	
	

	public CSSStyleSheet createCSSStyleSheet(String title, String media) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
		ADLDocument out = new ADLDocument(this);
		Element root = createElementNS(out, XMLHelper.KITE9_NAMESPACE, "diagram");
		out.appendChild(root);
		return out;
	}

	public StyleSheet createStyleSheet(Node node, HashTable pseudoAttrs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CSSEngine createCSSEngine(AbstractStylableDocument doc, CSSContext ctx, ExtendedParser ep, ValueManager[] vms, ShorthandManager[] sms) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewCSS createViewCSS(AbstractStylableDocument doc) {
		// TODO Auto-generated method stub
		return null;
	}
}
