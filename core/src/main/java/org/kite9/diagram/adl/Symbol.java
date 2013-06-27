package org.kite9.diagram.adl;

import java.io.Serializable;

import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.DiagramElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


@XStreamAlias("symbol")
public class Symbol implements Serializable, CompositionalDiagramElement {

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

	public int compareTo(DiagramElement o) {
		if (o instanceof Symbol) {
			int out = ((Character)this.theChar).compareTo(((Symbol) o).theChar);
			if (out==0) {
				return this.shape.compareTo(((Symbol) o).shape);
			} else {
				return out;
			}
		} else {
			return 1;
		}
	}
	
	@XStreamOmitField
	Object parent;
	
	public void setParent(Object el) {
		this.parent = el;
	}

	public Object getParent() {
		return parent;
	}

	
}
