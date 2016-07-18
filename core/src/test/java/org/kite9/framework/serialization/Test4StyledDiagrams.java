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
import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.ContainerProperty;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Key;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.Symbol.SymbolShape;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.primitives.Connection;
import org.kite9.diagram.primitives.StylesheetReference;
import org.kite9.diagram.primitives.TextContainingDiagramElement;
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
		
		TextContainingDiagramElement label = g.getLabel();
		Assert.assertEquals("Some Load of Nonsense", label.getText());
		
		TextContainingDiagramElement stereotype = g.getStereotype();
		Assert.assertEquals("The stereotype", stereotype.getText());
		
		// check the text line
		Assert.assertEquals(1, g.getText().size());
		TextLine tl1 = (TextLine) g.getText().iterator().next();
		Assert.assertEquals("Some text", tl1.getText().trim());
		Assert.assertEquals(1, tl1.getSymbols().size());
		Symbol symbol = tl1.getSymbols().iterator().next();
		Assert.assertEquals('z', symbol.getChar());
		Assert.assertEquals("My Z", symbol.getText());
		
		// check the glyph symbols
		Assert.assertEquals(1, g.getSymbols().size());
		
		// check the arrow
		Arrow a = (Arrow) g.getNextElementSibling();
		Assert.assertEquals("arrow1", a.getID());
		Assert.assertEquals("this is in the arrow", a.getLabel().getText());
		
		// check the context
		Context c = (Context) a.getNextElementSibling();
		Assert.assertEquals("context1", c.getID());
		Assert.assertEquals("This is the label", ((TextLine) c.getLabel()).getText());
		Assert.assertEquals(1, c.getContents().size());
		Assert.assertTrue(c.getContents().get(0) instanceof Glyph);
		
		// check the link
		ContainerProperty<Connection> allLinks = d.getAllLinks();
		Assert.assertEquals(1, allLinks.size());
		Connection l = allLinks.iterator().next();
		Assert.assertEquals(g, l.getFrom());
		Assert.assertEquals(a, l.getTo());
		Assert.assertEquals(Direction.RIGHT, l.getDrawDirection());
		Assert.assertTrue(g.getLinks().contains(l));
		Assert.assertTrue(a.getLinks().contains(l));
		
		// check the key
		Key k = d.getKey();
		Assert.assertNotNull(k);
		Assert.assertEquals("Keykey", k.getBoldText().getText());
		Assert.assertEquals("Body", k.getBodyText().getText());
		Assert.assertEquals(3, k.getSymbols().size());
		TextLine first = k.getSymbols().iterator().next();
		Assert.assertEquals('a', first.getSymbols().iterator().next().getChar());
		Assert.assertEquals(SymbolShape.DIAMOND, first.getSymbols().iterator().next().getShape());
		Assert.assertEquals("Some description\n		 taking multiple lines", first.getText());
		
		// ensure we can serialize.  Since it's held as a dom, it's not likely to be a problem
		System.out.println(outstring);
		XMLCompare.checkIdenticalXML(instring, outstring);
	}
}
