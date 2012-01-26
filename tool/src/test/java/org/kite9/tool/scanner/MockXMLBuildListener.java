package org.kite9.tool.scanner;

import org.kite9.framework.serialization.XMLHelper;
import org.kite9.framework.server.WorkItem;

public class MockXMLBuildListener extends MockBuildListener {

    public MockXMLBuildListener(String[] toContain) {
	super(toContain);
    }

    XMLHelper helper = new XMLHelper();
    
    @Override
    public void process(WorkItem designItem) {
	String xml = helper.toXML(designItem);
	out.append(xml);
	out.append("\n");
    }

}
