package org.kite9.tool.scanner;

import junit.framework.Assert;

import org.junit.Test;
import org.kite9.tool.AbstractRunnerTest;
import org.kite9.tool.BasicKite9Runner;
import org.kite9.tool.listener.BuildListener;

public class Test2DefaultScanner extends AbstractRunnerTest {

	static final String LINE3 = "project_java_class:"+Test2DefaultScanner.class.getName();

	@Test
	public void test_2_1_ScanDesignItems() throws Exception {
		BasicKite9Runner bkr = new BasicKite9Runner();
		BasicClassScanner scanner = (BasicClassScanner) createLocalClassScanner(createModel());
		scanner.setBasePackage(this.getClass().getName());
		bkr.setScanners(createList((Scanner)scanner));
		bkr.setContext(ctx);
		MockBuildListener mbl = new MockBuildListener(new String[] {} );
		bkr.setListeners(createList((BuildListener) mbl));
		bkr.process();
		Assert.assertTrue("Should be something in the listener: ",mbl.getOut().toString().length() > 0);
	}

}
