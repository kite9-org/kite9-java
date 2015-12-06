package org.kite9.framework.serialization;

import junit.framework.Assert;

import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.KeyHelper;
import org.kite9.framework.common.HelpMethods;
import org.kite9.framework.server.BasicWorkItem;
import org.kite9.framework.server.WorkItem;

public class Test2SerializeWorkItems extends HelpMethods {

    @Test
	public void quickSerializeTest() {
		KeyHelper kh = new KeyHelper();
		Diagram d1 = Test1SerializeDiagram.createDiagram(kh);
		WorkItem w1 = new BasicWorkItem(d1, "hello", "diagram.png", "x", "x", "PNG");
		
		
		XMLHelper helper = new XMLHelper();
		
		String firstXml = helper.toXML(w1);
		
		WorkItem back =  (WorkItem) helper.fromXML(firstXml);
		
		String secondXML = helper.toXML(back);
		
		
		Assert.assertEquals(firstXml+"\n is not same as \n"+secondXML, firstXml, secondXML);
		
	}
}
