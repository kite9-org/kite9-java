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
		File f = new File(dir, "simpleDiagram.png");
		if (f.exists()) {
			f.delete();
		}
		
		URL p =  Test1DiagramServer.class.getResource("kite9-test.props");
		Main.main(new String[] { "-p", p.getFile(), "-Xcl" });
		
		Assert.assertTrue(f.exists());
		
		File f2 = new File(Test1DiagramServer.class.getResource("simpleDiagram.map").getFile());
		File f2comp = new File(dir, "simpleDiagram.map");
		
		FileDiff.filesContainSameLines(f2, f2comp);
	}
	
	@Kite9Item
	public Diagram simpleDiagram(DiagramBuilder db) {
		Glyph test = new Glyph("project_class:bob", "this is a", "test", null, null);
		Diagram out = new Diagram("some diagram", createList((Contained) test), null);
		return out;
	}
}
