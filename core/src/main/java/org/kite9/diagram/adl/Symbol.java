package org.kite9.diagram.adl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("symbol")
public class Symbol implements Comparable<Symbol> {

	private static final long serialVersionUID = 3578883565482903409L;
	
	@XStreamAsAttribute
	String text;
	
	public String getText() {
		return text;
	}

	@XStreamAsAttribute
	char theChar;
	
	@XStreamAsAttribute
	SymbolShape shape;
	
	public enum SymbolShape { HEXAGON, CIRCLE,  DIAMOND };

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == this)
			return true;
	
		if (arg0 instanceof Symbol)
			return ((Symbol)arg0).getText().equals(getText());
		else
			return false;
	}

	@Override
	public String toString() {
		return "Symbol: "+getText();
	}
	
	public Symbol() {
	}

	public Symbol(String text, char preferredChar, SymbolShape shape) {
		this.text = text;
		this.theChar = preferredChar;
		this.shape = shape;
	}

	public char getChar() {
		return theChar;
	}

	public void setChar(char theChar) {
		this.theChar = theChar;
	}

	public SymbolShape getShape() {
		return shape;
	}

	public void setShape(SymbolShape shape) {
		this.shape = shape;
	}

	public int compareTo(Symbol o) {
		int out = ((Character)this.theChar).compareTo(o.theChar);
		if (out==0) {
			return this.shape.compareTo(o.shape);
		} else {
			return out;
		}
	}
	
	
	
}
