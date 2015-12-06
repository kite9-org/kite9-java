package org.kite9.tool.integration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.DiffException;
import org.kite9.framework.common.FileDiff;
import org.kite9.tool.AbstractRunnerTest;
import org.kite9.tool.Main;

/**
 * Runs a test to generate a diagram by connecting to the server.
 * 
 * @author moffatr
 * 
 */
public class Test1DiagramServer extends AbstractRunnerTest {

	@Test
	public void test_1_1_SendMessageToServer() throws IOException, DiffException {
		File dir = new File("target/kite9-repo/"+this.getClass().getName().replace(".", "/"));
		File f1 = checkDelete(dir, "simpleDiagramVersion1.png");
		File f2 = checkDelete(dir, "simpleDiagramVersion2.png");
		
		URL p =  Test1DiagramServer.class.getResource("kite9-test.props");
		Main.main(new String[] { "-p", p.getFile(), "-Xcl" });
		
		Assert.assertTrue(f1.exists());
		Assert.assertTrue(f2.exists());
		
		File f3 = new File(Test1DiagramServer.class.getResource("simpleDiagramVersion1.map").getFile());
		File f3comp = new File(dir, "simpleDiagramVersion1.map");
		
		FileDiff.filesContainSameLines(f3, f3comp);
	}

	protected File checkDelete(File dir, String name) {
		File f = new File(dir, name);
		if (f.exists()) {
			f.delete();
		}
		return f;
	}
	
	@Kite9Item
	public Diagram simpleDiagramVersion1(DiagramBuilder db) {
		Glyph test = new Glyph("project_class:bob", "this is a", "test1", null, null);
		Diagram out = new Diagram("some diagram", createList((Contained) test), null);
		return out;
	}
	
	@Kite9Item
	public Diagram simpleDiagramVersion2(DiagramBuilder db) {
		Glyph test = new Glyph("project_class:bob", "this is a", "test2", null, null);
		Diagram out = new Diagram("some diagram", createList((Contained) test), null);
		return out;
	}
}
