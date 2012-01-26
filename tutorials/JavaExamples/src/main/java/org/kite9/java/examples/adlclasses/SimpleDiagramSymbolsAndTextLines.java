package org.kite9.java.examples.adlclasses;

import static org.kite9.framework.common.HelpMethods.createList;
import static org.kite9.framework.common.HelpMethods.listOf;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Key;
import org.kite9.diagram.adl.KeyHelper;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.adl.Symbol.SymbolShape;
import org.kite9.framework.Kite9Item;

public class SimpleDiagramSymbolsAndTextLines {

	@Kite9Item
	public Diagram simpleDiagramWithSymbolsAndTextLines() {
		Symbol s = new Symbol("Brother of Tony Scott", 'T', SymbolShape.CIRCLE);
		Glyph rs = new Glyph("Director", "Ridley Scott", createList(new TextLine("Directed: Thelma & Louise"), new TextLine("Directed: Gladiator")),
				createList(s));
		Diagram d1 = new Diagram("my_diagram", listOf(rs), new Key("Big Movies", "", createList(s)));
		return d1;
	}
	
	@Kite9Item
	public Diagram simpleDiagramWithKeyHelper() {
		KeyHelper kh = new KeyHelper();
		Symbol s = kh.createSymbol("Brother of Tony Scott");
		
		Glyph hf = new Glyph("Actor","Harrison Ford", null, null);
		Glyph rs = new Glyph("Director", "Ridley Scott", null,
				createList(s));
		Diagram d1 = new Diagram("my_diagram", listOf(hf, rs), 
				new Key("Big Movies", "", kh.getUsedSymbols()));
		return d1;
	}
}
