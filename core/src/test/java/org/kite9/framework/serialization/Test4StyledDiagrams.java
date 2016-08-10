package org.kite9.framework.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSEngineUserAgent;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.StringValue;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.dom.AbstractStylableDocument;
import org.apache.batik.util.CSSConstants;
import org.apache.batik.util.SVGTypes;
import org.junit.Assert;
import org.junit.Test;
import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.TextContainingDiagramElement;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.xml.ADLDocument;
import org.kite9.diagram.xml.Arrow;
import org.kite9.diagram.xml.ContainerProperty;
import org.kite9.diagram.xml.Context;
import org.kite9.diagram.xml.Diagram;
import org.kite9.diagram.xml.Glyph;
import org.kite9.diagram.xml.Key;
import org.kite9.diagram.xml.Link;
import org.kite9.diagram.xml.StylesheetReference;
import org.kite9.diagram.xml.Symbol;
import org.kite9.diagram.xml.TextLine;
import org.kite9.diagram.xml.XMLElement;
import org.kite9.diagram.xml.Symbol.SymbolShape;
import org.kite9.framework.common.RepositoryHelp;


public class Test4StyledDiagrams {

	@Test
	public void test_4_1_LoadStyledDiagram() throws IOException {
		InputStream is = this.getClass().getResourceAsStream("s1.txt");
		URL u = this.getClass().getResource("styles.css");
		StringWriter sw = new StringWriter();
		RepositoryHelp.streamCopy(new InputStreamReader(is), sw, true);
		String instring = sw.toString();
		
		Object o = new XMLHelper().fromXML(instring);
		Diagram d = (Diagram) o;
		String outstring = new XMLHelper().toXML(d);

		Assert.assertEquals("bob", d.getCSSClass());
		
		StylesheetReference sr = (StylesheetReference) d.getFirstElementChild();
		
		sr.setHref(u.toString());
		
		Glyph g = (Glyph) sr.getNextElementSibling();
		Assert.assertEquals("james", g.getCSSClass());
		
		final ADLDocument ownerDocument = (ADLDocument) d.getOwnerDocument();
		CSSEngine e = ownerDocument.getCSSEngine();
		StyleMap sm = e.getCascadedStyleMap(g, null);
		Value v = sm.getValue(e.getPropertyIndex(CSSConstants.CSS_FILL_PROPERTY));
		Assert.assertEquals("blue", ((StringValue)v).getCssText());
		
		XMLElement label = g.getLabel();
		Assert.assertEquals("Some Load of Nonsense", label.getTextContent());
		
		XMLElement stereotype = g.getStereotype();
		Assert.assertEquals("The stereotype", stereotype.getTextContent());
		
		// check the text line
		Assert.assertEquals(1, g.getText().getChildXMLElementCount());
		TextLine tl1 = (TextLine) g.getText().iterator().next();
		Assert.assertEquals("Some text", tl1.getText().trim());
		Assert.assertEquals(1, tl1.getSymbols().getChildXMLElementCount());
		Symbol symbol = (Symbol) tl1.getSymbols().iterator().next();
		Assert.assertEquals('z', symbol.getChar());
		Assert.assertEquals("My Z", symbol.getText());
		
		// check the glyph symbols
		Assert.assertEquals(1, g.getSymbols().getChildXMLElementCount());
		
		// check the arrow
		Arrow a = (Arrow) g.getNextElementSibling();
		Assert.assertEquals("arrow1", a.getID());
		Assert.assertEquals("this is in the arrow", a.getLabel().getTextContent());
		
		// check the context
		Context c = (Context) a.getNextElementSibling();
		Assert.assertEquals("context1", c.getID());
		Assert.assertEquals("This is the label", ((TextLine) c.getLabel()).getText());
		Assert.assertEquals(2, c.getChildXMLElementCount());
		Assert.assertTrue(c.iterator().next() instanceof Glyph);
		
		// check the link
		ContainerProperty allLinks = d.getAllLinks();
		Assert.assertEquals(1, allLinks.size());
		Link l = (Link) allLinks.iterator().next();
		Assert.assertEquals(g, l.getFrom());
		Assert.assertEquals(a, l.getTo());
		Assert.assertEquals(Direction.RIGHT, l.getDrawDirection());

		
		// check the key
		Key k = d.getKey();
		Assert.assertNotNull(k);
		Assert.assertEquals("Keykey", k.getBoldText().getTextContent());
		Assert.assertEquals("Body", k.getBodyText().getTextContent());
		Assert.assertEquals(3, k.getSymbols().getChildXMLElementCount());
		TextLine first = (TextLine) k.getSymbols().iterator().next();
		Symbol sym1 = (Symbol)first.getSymbols().iterator().next();
		Assert.assertEquals('a', sym1.getChar());
		Assert.assertEquals(SymbolShape.DIAMOND, sym1.getShape());
		Assert.assertEquals("Some description\n		 taking multiple lines", first.getText());
		
		// ensure we can serialize.  Since it's held as a dom, it's not likely to be a problem
		System.out.println(outstring);
		XMLCompare.checkIdenticalXML(instring, outstring);
	}
}
