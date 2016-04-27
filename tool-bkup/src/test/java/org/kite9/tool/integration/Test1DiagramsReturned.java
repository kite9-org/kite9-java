package org.kite9.tool.integration;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.DiffException;
import org.kite9.framework.common.FileDiff;
import org.kite9.framework.common.RepositoryHelp;

/**
 * Runs a test to generate a diagram by connecting to the server.
 * 
 * @author moffatr
 * 
 */
public class Test1DiagramsReturned extends AbstractRunnerTest {

	private static final String DIAGRAM_ID = "some-diagram";

	@Test
	public void test_1_1_SendMessageToServer() throws IOException, DiffException {
		File f = RepositoryHelp.prepareFileName(DIAGRAM_ID, "png", "target/kite9-repo", false);
		Assert.assertTrue(f.exists());
		
		File f2 = RepositoryHelp.prepareFileName(DIAGRAM_ID, "map", "target/kite9-repo", false);
		Assert.assertTrue(f2.exists());
		
		String resourcePath= this.getClass().getResource(DIAGRAM_ID+".map").getFile();
		File f2Comp = new File(resourcePath);
		FileDiff.filesContainSameLines(f2, f2Comp);
	}
	
	@Kite9Item
	public Diagram simpleDiagram(DiagramBuilder db) {
		Glyph test = new Glyph("project_class:bob", "this is a", "test", null, null);
		Diagram out = new Diagram(DIAGRAM_ID, createList((Contained) test), null);
		return out;
	}
}
