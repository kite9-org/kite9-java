package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractLabel;
import org.kite9.diagram.primitives.Label;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("key")
public class Key extends AbstractLabel implements Label {

	private static final long serialVersionUID = 7705875104684442878L;

	String boldText;
	
	String bodyText;
	
	public String getBodyText() {
		return bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	List<TextLine> symbols;
	
	public String getBoldText() {
		return boldText;
	}

	public void setBoldText(String boldText) {
		this.boldText = boldText;
	}
	
	public static List<TextLine> convert(List<Symbol> symbols) {
		List<TextLine> out = new ArrayList<TextLine>();
		if (symbols == null) {
			return out;
		}
		for (Symbol s : symbols) {
			List<Symbol> sl = new ArrayList<Symbol>(1);
			sl.add(s);
			out.add(new TextLine(s.getText(), sl));
		}
		return out;
	}
	
	public Key(String boldText, String bodyText, List<Symbol> symbols) {
		this(convert(symbols), boldText, bodyText);
	}

	public Key(List<TextLine> symbols, String boldText, String bodyText) {
		this.boldText = boldText;
		this.bodyText = bodyText;
		this.symbols = symbols;
	}
	
	public void setRenderingInformation(RenderingInformation ri) {
		this.renderingInformation = ri;
	}

	public List<TextLine> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<TextLine> symbols) {
		this.symbols = symbols;
	}
	
	public String toString() {
		return "KEY";
	}

	public boolean hasContent() {
		return hasContent(boldText) || hasContent(bodyText) || hasContent(symbols);
	}
	
	
}
