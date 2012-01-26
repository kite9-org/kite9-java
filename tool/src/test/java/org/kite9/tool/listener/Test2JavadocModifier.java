package org.kite9.tool.listener;

import java.io.File;

import org.junit.Test;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.FileDiff;
import org.kite9.tool.AbstractRunnerTest;
import org.kite9.tool.BasicKite9Runner;
import org.kite9.tool.scanner.Scanner;

/**
 * here is some class level documentation
 * 
 * @author moffatr
 * 
 */
public class Test2JavadocModifier extends AbstractRunnerTest {


	/**
	 * Here is some bobbins related to the design item
	 * 
	 * @return
	 */
	@Kite9Item
	public String someDesignItem() {
		return "Hello";
	}

	@Test
	public void test_2_1_JavadocModification() throws Exception {
		generateJavadocs();
		
		BasicKite9Runner bkr = new BasicKite9Runner();
		bkr.setScanners(createList((Scanner) createLocalClassScanner(createModel())));
		bkr.setContext(ctx);
		MockJavadocModifierListener mbl = new MockJavadocModifierListener();
		mbl.setContext(ctx);
		mbl.setDocRoot(TARGET_DOCS);
		bkr.setListeners(createList((BuildListener) mbl));
		bkr.process();
		
		String htmlFile = this.getClass().getName().replace(".", "/")+".html";
		
		String className = this.getClass().getCanonicalName();
		
		FileDiff.fileContainsLines(new File(TARGET_DOCS+"/"+htmlFile),
			"<h2>Modifying: "+className+"</h2>",
			"Modifying: "+className+": method : someDesignItem",
			"Modifying: "+className+": method : test_2_1_JavadocModification"


		
		);
	}

	
}
