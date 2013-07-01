package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractLabel;
import org.kite9.diagram.primitives.Label;
import org.kite9.diagram.primitives.StyledText;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("key")
public class Key extends AbstractLabel implements Label {

	private static final long serialVersionUID = 7705875104684442878L;

	StyledText boldText;
	
	StyledText bodyText;
	
	public StyledText getBodyText() {
		return bodyText;
	}

	public void setBodyText(StyledText bodyText) {
		this.bodyText = bodyText;
	}

	List<TextLine> symbols;
	
	public StyledText getBoldText() {
		return boldText;
	}

	public void setBoldText(StyledText boldText) {
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
		this.boldText = new StyledText(boldText);
		this.bodyText = new StyledText(bodyText);
		this.symbols = symbols;
		for (TextLine textLine : symbols) {
			textLine.setParent(this);
		}
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
