package org.kite9.java.examples.adlclasses;

import static org.kite9.framework.common.HelpMethods.listOf;

import java.io.IOException;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.LinkEndStyle;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.framework.Kite9Item;


public class SimpleDiagramWithContexts {

	@Kite9Item
	public Diagram contextExample() {
			Glyph hf = new Glyph("harrison_ford","Actor","Harrison Ford", null, null);
			Glyph rs = new Glyph("ridley_scott", "Director", "Ridley Scott", null, null);
			Arrow ww = new Arrow("worked_with", "worked with");
			
			new Link(ww, hf, null, null, null, null, Direction.RIGHT);
			new Link(ww, rs);

			Context bladerunner = new Context("bladerunner", listOf(hf, rs, ww), true, new TextLine("Bladerunner"), null);

			
			Diagram d1 = new Diagram("my_diagram", listOf(bladerunner), null);
			return d1;
		
	}
	
	@Kite9Item
	public Diagram contextDirectionExamples() {
		Glyph[] g = new Glyph[9];
		for (int i = 0; i < g.length; i++) {
			g[i] = new Glyph(""+i, null, ""+i, null, null);
		}
	
		Context leftToRight = new Context(listOf(g[0], g[1], g[2]), true, new TextLine("Left to Right"), Layout.RIGHT);
		Context bottomToTop = new Context(listOf(g[3], g[4], g[5]), true, new TextLine("Bottom to Top"), Layout.UP);
		Context topToBottom = new Context(listOf(g[6], g[7], g[8]), true, new TextLine("Top to Bottom"), Layout.DOWN);
		
		Diagram d1 = new Diagram("my_diagram", listOf(leftToRight, bottomToTop, topToBottom), null);
		
		return d1;
		
	}
	
	@Kite9Item
	public Diagram contextDirection2() throws IOException {
		Glyph[] g = new Glyph[8];
		for (int i = 0; i < g.length; i++) {
			g[i] = new Glyph(""+i, null, ""+i, null, null);
		}
	
		Context hor = new Context(listOf(g[0], g[1], g[2], g[3]), true, new TextLine("Horizontal"), Layout.HORIZONTAL);
		Context vert = new Context(listOf(g[4], g[5], g[6], g[7]), true, new TextLine("Vertical"), Layout.VERTICAL);
		
		new Link(g[2], g[5]);
		
		Diagram d1 = new Diagram("my_diagram", listOf(hor, vert), null);
		
		return d1;
	}
	
	@Kite9Item
	public Diagram hiddenContext() throws IOException {
			Glyph rs = new Glyph("ridley_scott", "Director", "Ridley Scott", null, null);
			Arrow directed = new Arrow("directed");
			
			new Link(directed, rs);

			Glyph bladerunner = new Glyph("film", "Bladerunner", null, null);
			Glyph glad = new Glyph("film", "Gladiator", null, null);
			Glyph thelma = new Glyph("film", "Thelma & Louise", null, null);

			new Link(directed, bladerunner, null, null, LinkEndStyle.ARROW, null);
			new Link(directed, glad, null, null, LinkEndStyle.ARROW, null);
			new Link(directed, thelma, null, null, LinkEndStyle.ARROW, null);

			Context hidden = new Context(listOf(bladerunner, glad, thelma), false, null, Layout.HORIZONTAL);
			
			Diagram d1 = new Diagram("my_diagram", listOf(rs, directed, hidden), null);
			return d1;
	}

}
