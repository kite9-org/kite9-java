package org.kite9.java.examples.adlclasses;

import static org.kite9.framework.common.HelpMethods.listOf;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.LinkEndStyle;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.Direction;
import org.kite9.framework.Kite9Item;

public class SimpleDiagramWithArrow {

	@Kite9Item
	public Diagram simpleDiagramWithArrow() {
		Glyph hf = new Glyph("harrison_ford","Actor","Harrison Ford", null, null);
		Glyph rs = new Glyph("ridley_scott", "Director", "Ridley Scott", null, null);
		Arrow ww = new Arrow("worked_with", "worked with");
		
		new Link(ww, hf);
		new Link(ww, rs);
		
		Diagram d1 = new Diagram("my_diagram", listOf(hf, rs, ww), null);
		return d1;
	}
	
	@Kite9Item
	public Diagram simpleDiagramWithArrow2() {
		Glyph hf = new Glyph("harrison_ford","Actor","Harrison Ford", null, null);
		Glyph rs = new Glyph("ridley_scott", "Director", "Ridley Scott", null, null);
		Arrow ww = new Arrow("worked_with", "worked with");
		
		new Link(ww, hf, LinkEndStyle.ARROW, null, LinkEndStyle.ARROW, null);
		new Link(ww, rs, null, new TextLine("label 1"), null, new TextLine("label 2"));
		
		Diagram d1 = new Diagram("my_diagram", listOf(hf, rs, ww), null);
		return d1;
	}
	
	@Kite9Item
	public Diagram simpleDiagramWithDirectedArrow() {
		Glyph hf = new Glyph("harrison_ford","Actor","Harrison Ford", null, null);
		Glyph rs = new Glyph("ridley_scott", "Director", "Ridley Scott", null, null);
		Arrow ww = new Arrow("worked_with", "worked with");
		
		new Link(ww, hf, null, null, null, null, Direction.RIGHT);
		new Link(ww, rs);
		
		Diagram d1 = new Diagram("my_diagram", listOf(hf, rs, ww), null);
		return d1;
	}

}
