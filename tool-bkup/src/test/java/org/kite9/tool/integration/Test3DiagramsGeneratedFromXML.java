package org.kite9.tool.integration;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class Test3DiagramsGeneratedFromXML extends AbstractRunnerTest {

	@Test
	public void test_2_1_ScanDesignItems() throws Exception {
		File f = new File("target/kite9-repo/some/test/design/item/look/look.png");
		Assert.assertTrue(f.exists());
		
	}
}
