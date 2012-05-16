package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractLabel;
import org.kite9.diagram.primitives.SymbolTarget;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("text-line")
public class TextLine extends AbstractLabel implements SymbolTarget {

	private static final long serialVersionUID = -1917135065467101779L;
	
	String text;

	public String getText() {
		return text;
	}

	List<Symbol> symbols = new ArrayList<Symbol>();

	public TextLine(String id, String text) {
		this.text = text;
	}
	
	public TextLine(String text) {
		this.text = text;
	}
	
	public TextLine() {
	}
	
	public TextLine(String id, String text, List<Symbol> symbols) {
		this(text);
		this.symbols = symbols;
	}
	
	public TextLine(String text, List<Symbol> symbols) {
		this(text);
		this.symbols = symbols;
	}

	public List<Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<Symbol> symbols) {
		this.symbols = symbols;
	}

	public RenderingInformation getRenderingInformation() {
		if (renderingInformation == null) {
			renderingInformation = new RectangleRenderingInformation();
		}
		
		return renderingInformation;
	}
	
	public String toString() {
		return "[TL:"+text+"]";
	}

	public void setText(String text) {
	    this.text = text;
	}

	public boolean hasContent() {
		return hasContent(text) || hasContent(symbols);
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.renderingInformation = ri;
	}
}
