package org.kite9.java.examples.adlclasses;

import static org.kite9.framework.common.HelpMethods.listOf;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.framework.Kite9Item;

public class SimpleDiagram {

	@Kite9Item
	public Diagram simpleDiagram() {
		Glyph hf = new Glyph("harrison_ford","Actor","Harrison Ford", null, null);
		Glyph rs = new Glyph("ridley_scott", "Director", "Ridley Scott", null, null);
		Diagram d1 = new Diagram("my_diagram", listOf(hf, rs), null);
		return d1;
	}
	
	@Kite9Item
	public Diagram simpleDiagramWithoutIDs() {
		Glyph hf = new Glyph("Actor","Harrison Ford", null, null);
		Glyph rs = new Glyph("Director", "Ridley Scott", null, null);
		Diagram d1 = new Diagram("my_diagram", listOf(hf, rs), null);
		return d1;
	}

}
