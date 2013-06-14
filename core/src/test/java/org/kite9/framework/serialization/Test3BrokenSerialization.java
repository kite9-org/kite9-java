package org.kite9.framework.serialization;

import junit.framework.Assert;

import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;

public class Test3BrokenSerialization {

	@Test
	public void test_3_1_BrokenLinks() {
		XMLHelper help = new XMLHelper();
		Diagram d = (Diagram) help.fromXML(
				"<?xml version=\"1.0\" ?><diagram xmlns=\"http://www.kite9.org/schema/adl\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" id=\"p1\">"+
				   "<glyph id=\"g1\" />" +
				   "<allLinks>"+"<link id=\"5\">"+
				   "<from xsi:type=\"glyph\" reference=\"bob\" />"+
				   "<to xsi:type=\"context\" reference=\"g1\" />"+
				   "</link></allLinks></diagram>");
				  
		Assert.assertEquals(d.getContents().size(),1);
		Glyph g = (Glyph) d.getContents().get(0);
		Assert.assertEquals(0, g.getLinks().size());
	}
}
