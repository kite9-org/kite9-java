package org.kite9.framework.serialization;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.kite9.diagram.adl.Contained;
import org.kite9.diagram.xml.Diagram;

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
		String theXML = "<diagram xmlns='http://www.kite9.org/schema/adl' id='My Diagram' class='bob'><arrow2><name>My Arrow</name></arrow2><stylesheet href='styles.css' /></diagram>";
		
		Diagram d = (Diagram) new XMLHelper().fromXML(theXML);
		
		Assert.assertEquals("My Diagram", d.getID());
		Assert.assertEquals("styles.css", d.getStylesheetReference().getHref());
		
		List<Contained> contents = d.getContents();
		
		
		
	}
}
