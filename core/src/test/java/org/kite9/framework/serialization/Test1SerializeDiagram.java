package org.kite9.framework.serialization;

import junit.framework.Assert;

import org.junit.Test;
import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Key;
import org.kite9.diagram.adl.KeyHelper;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.LinkEndStyle;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.common.HelpMethods;

public class Test1SerializeDiagram extends HelpMethods {

	@Test
	public void quickSerializeTest() {
		KeyHelper kh = new KeyHelper();

		Diagram d1 = createDiagram(kh);

		XMLHelper helper = new XMLHelper();

		String firstXml = helper.toXML(d1);

		System.out.println(firstXml);

		Diagram d2 = (Diagram) helper.fromXML(firstXml);

		String secondXML = helper.toXML(d2);
		System.out.println(secondXML);

		Assert.assertEquals(firstXml + "\n is not same as \n" + secondXML,
				firstXml, secondXML);

	}

	public static Diagram createDiagram(KeyHelper kh) {
		Glyph g1 = new Glyph("class", "Test Class", createList(new TextLine(
				"Here is a line of text", createList(kh.createSymbol(
						"Baphomet", "B")))), createList(kh.createSymbol(
				"Public", "P")));

		Arrow a1 = new Arrow("leaver");

		// adding problem with references.
		Glyph g2 = new Glyph("ref1", "", "Some Item A", null, null);
		Glyph g3 = new Glyph("ref1", "", "Some Item B", null, null);

		Context inside = new Context(createList((Contained) g1, g2, g3), true,
				null, null);

		new Link(a1, g1, null, null, LinkEndStyle.ARROW, new TextLine(
				"Some Label"), Direction.RIGHT);
		new Link(a1, g2);
		new Link(a1, g3);

		Diagram d1 = new Diagram("My Diagram", createList((Contained) inside,
				(Contained) a1), new Key("Here is my amazing diagram", null,
				kh.getUsedSymbols()));
		return d1;
	}
}
