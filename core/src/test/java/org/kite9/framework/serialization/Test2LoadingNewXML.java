package org.kite9.framework.serialization;

import java.net.URL;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.kite9.diagram.adl.Connected;
import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.xml.DiagramXMLElement;
import org.kite9.diagram.xml.XMLElement;

/**
 * This is based on the idea that we should be able to load any XML 
 * as a diagram and render it.
 * 
 * @author robmoffat
 *
 */
public class Test2LoadingNewXML {

	@Test
	public void test_2_1_LoadSomeXML() {
		URL u = this.getClass().getResource("styles2.css");

		
		String theXML = "<diagram xmlns='http://www.kite9.org/schema/adl' id='My Diagram' class='bob'>"+
				"<arrow2 id='one'><name>My Arrow</name></arrow2>"+
				"<arrow3 id='two'><name>My Arrow</name></arrow3>"+
				"<link id='link'><from reference='one' /><to reference='two' /></link>"+
				"<stylesheet href='"+u.toString()+"' /></diagram>";
		
		DiagramXMLElement d = (DiagramXMLElement) new XMLHelper().fromXML(theXML);
		
		Assert.assertEquals("My Diagram", d.getID());
		Assert.assertTrue(d.getStylesheetReference().getHref().endsWith("styles2.css"));
		Iterator<XMLElement> iterator = d.iterator();
		XMLElement one = iterator.next();
		Assert.assertEquals("arrow2", one.getTagName());
		XMLElement two = iterator.next();
		Assert.assertEquals("arrow3", two.getTagName());
		
		Connected oneDe, twoDe;
		oneDe = (Connected) one.getDiagramElement();
		twoDe = (Connected) two.getDiagramElement();
		
		Diagram de = d.getDiagramElement();
		
		Connection l = oneDe.getLinks().iterator().next();
		Assert.assertEquals(oneDe, l.getFrom());
		Assert.assertEquals(twoDe, l.getTo());
		
		Assert.assertEquals(l, oneDe.getLinks().iterator().next());
		Assert.assertEquals(l, twoDe.getLinks().iterator().next());
	
	}
}
