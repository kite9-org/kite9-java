package org.kite9.tool.integration;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.DiffException;
import org.kite9.framework.common.FileDiff;

/**
 * Tests that diagrams can be added to class-level javadocs
 * 
 * @author moffatr
 * 
 */
public class Test2JavadocsChanged extends AbstractRunnerTest {

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
		Glyph g1 = new Glyph(this.getClass().getName(), "stereo", this.getClass().getSimpleName(), null, null);
		Glyph g2 = new Glyph(Another.class.getName(), "stereo", Another.class.getSimpleName(), null, null);
		// the id for this must mach the method name.
		Diagram out = new Diagram(this.getClass().getCanonicalName()+".someDesignItem", createList((Contained) g1, g2), null);
		out.setName("Some Design Item, Yesterday");
		return out;
	}

	@Test
	public void test_1_1_JavadocDiagram() throws Exception {
		String htmlFile = this.getClass().getName().replace(".", "/") + ".html";

		try {
			FileDiff
					.fileContainsLines(
							new File(TARGET_DOCS + "/" + htmlFile),
							"<img src=\"Test1JavadocDiagram/someDesignItem.png\" usemap=\"#someDesignItem\" border=\"0\" alt=\"Some Design Item\"/><br />",
							"<area shape=\"rect\" coords=\"20,20,400,80\" href=\"Test1JavadocDiagram.html\" target=\"_parent\" />",
							"<area shape=\"rect\" coords=\"420,20,880,80\" href=\"Test1JavadocDiagram.Another.html\" target=\"_parent\" />"

					);
		} catch (DiffException e) {
			Assert.fail(e.getMessage());
		}
	}

}
