package org.kite9.framework.serialization;

import java.io.IOException;

import org.junit.Test;
import org.kite9.diagram.adl.ADLDocument;
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
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.common.HelpMethods;

public class Test1SerializeDiagram extends HelpMethods {

	@Test
	public void quickSerializeTest() throws IOException {
		ADLDocument doc = new ADLDocument();
		doc.setDocumentXmlEncoding("UTF-8");

		Diagram d1 = createDiagram(doc);

		XMLHelper helper = new XMLHelper();

		String firstXml = helper.toXML(d1);

		System.out.println(firstXml);

		Diagram d2 = (Diagram) helper.fromXML(firstXml);

		String secondXML = helper.toXML(d2);
		System.out.println(secondXML);

		XMLCompare.checkIdenticalXML(firstXml, secondXML);

	}

	public static Diagram createDiagram(ADLDocument doc) {
		KeyHelper kh = new KeyHelper(doc);
		Glyph g1 = new Glyph("g1", "class", "Test Class", 
			createList((CompositionalDiagramElement) new TextLine("tl1", "text-line", "Here is a line of text", createList(kh.createSymbol("Baphomet", "B")), doc)), 
			createList(kh.createSymbol("Public", "P")), false, doc);

		Arrow a1 = new Arrow("a1", "leaver", doc);

		Glyph g2 = new Glyph("ref2", "", "Some Item A", null, null, true, doc);
		Glyph g3 = new Glyph("ref3", "", "Some Item B", null, null, false, doc);

		Context inside = new Context("inside", createList((Contained) g1, g2, g3), true,
				null, null, doc);

		new Link("l1", a1, g1, null, null, LinkEndStyle.ARROW, new TextLine("label1", "toLabel", "Some Label", null, doc), Direction.RIGHT, doc);
		new Link("l2", a1, g2, doc);
		new Link("l3", a1, g3, doc);

		Key k = new Key("key", "Here is my amazing diagram", null, kh.getUsedSymbols(), doc);
		Diagram d1 = new Diagram("My Diagram", createList((Contained) inside, (Contained) a1), k, doc);
		return d1;
	}
}
