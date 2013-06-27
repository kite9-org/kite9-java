package org.kite9.tool.scanner;

import org.junit.Test;
import org.kite9.tool.AbstractRunnerTest;
import org.kite9.tool.BasicKite9Runner;
import org.kite9.tool.listener.BuildListener;

public class Test2XMLFileScanner extends AbstractRunnerTest {

    @Test
    public void test_2_1_ScanDesignItems() throws Exception {
	BasicKite9Runner bkr = new BasicKite9Runner();
	bkr.setScanners(createList((Scanner) createLocalFileScanner()));
	bkr.setContext(ctx);
	BuildListener mbl = new MockXMLBuildListener(new String[] {
		"item xmlns=\"http://www.kite9.org/schema/adl\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" name=\"look\" subjectId=\"some.test.design.item\"",
		"<glyph id=\"project:org.kite9.diagram.builders.Test1Class\">",
		"<stereotype>class</stereotype>",
		"<label>Test1Class</label>"
	});
	bkr.setListeners(createList((mbl)));
	bkr.process();
    }
}
