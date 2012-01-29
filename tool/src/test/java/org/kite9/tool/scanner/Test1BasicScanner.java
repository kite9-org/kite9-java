package org.kite9.tool.scanner;

import org.junit.Test;
import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.framework.Kite9Item;
import org.kite9.tool.AbstractRunnerTest;
import org.kite9.tool.BasicKite9Runner;
import org.kite9.tool.listener.BuildListener;

public class Test1BasicScanner extends AbstractRunnerTest {

	static final String LINE1 = "Here is a kite9 item as a string";
	static final String LINE2 = "Some other item";
	static final String LINE3 = "project_java_class:"+Test1BasicScanner.class.getName();

	@Kite9Item
	public String item1() {
		return LINE1;
	}

	@Kite9Item
	public String item2() {
		return LINE2;
	}
	
	@Kite9Item
	public String item3(DiagramBuilder db) {
	    String name = db.getId(Test1BasicScanner.class);
	    return name;
	}

	@Test
	public void test_1_1_ScanDesignItems() throws Exception {
		BasicKite9Runner bkr = new BasicKite9Runner();
		bkr.setScanners(createList((Scanner) createLocalClassScanner(createModel())));
		bkr.setContext(ctx);
		BuildListener mbl = new MockBuildListener(new String[] {LINE1, LINE2, LINE3} );
		bkr.setListeners(createList((mbl)));
		bkr.process();
	}

}
