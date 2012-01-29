package org.kite9.tool.listener;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.DiffException;
import org.kite9.framework.common.FileDiff;
import org.kite9.framework.common.HelpMethods;
import org.kite9.tool.AbstractRunnerTest;
import org.kite9.tool.BasicKite9Runner;
import org.kite9.tool.scanner.BasicClassScanner;
import org.kite9.tool.scanner.Scanner;

/**
 * Tests that diagrams can be added to class-level javadocs
 * 
 * @author moffatr
 * 
 */
public class Test1JavadocDiagram extends AbstractRunnerTest {

	/**
	 * Some other referenced class
	 */
	public static class Another {

	}

	/**
	 * Here is some bobbins related to the design item
	 * 
	 * @return
	 */
	@Kite9Item
	public Diagram someDesignItem() {
		Glyph g1 = new Glyph("stereo", "project:" + this.getClass().getName(), null, null);
		Glyph g2 = new Glyph("stereo", "project:" + Another.class.getName(), null, null);

		return new Diagram("somename", createList((Contained) g1, g2), null);
	}

	@Test
	public void test_1_1_JavadocDiagram() throws Exception {
		generateJavadocs();

		BasicKite9Runner bkr = new BasicKite9Runner();
		BasicClassScanner scs = createLocalClassScanner(createModel());
		scs.setBasePackage(Test1JavadocDiagram.class.getName());
		bkr.setScanners(HelpMethods.createList((Scanner) scs));
		bkr.setContext(ctx);
		Kite9DiagramJavadocListener mbl = new Kite9DiagramJavadocListener();
		mbl.setContext(ctx);
		mbl.setDocRoot(TARGET_DOCS);
		ServerCallingBuildListener scbl = createServerCallingListener();
		bkr.setListeners(createList((BuildListener) mbl, scbl));
		bkr.process();

		String htmlFile = this.getClass().getName().replace(".", "/") + ".html";

		try {
			FileDiff
					.fileContainsLines(
							new File(TARGET_DOCS + "/" + htmlFile),
							"<img src=\"Test1JavadocDiagram/someDesignItem.png\" usemap=\"#someDesignItem\" border=\"0\" alt=\"Some Design Item\"/><br />",
							"<area shape=\"rect\" coords=\"2,2,40,8\" href=\"Test1JavadocDiagram.html\" target=\"_parent\" />",
							"<area shape=\"rect\" coords=\"42,2,88,8\" href=\"Test1JavadocDiagram.Another.html\" target=\"_parent\" />"

					);
		} catch (DiffException e) {
			Assert.fail(e.getMessage());
		}
	}

}
