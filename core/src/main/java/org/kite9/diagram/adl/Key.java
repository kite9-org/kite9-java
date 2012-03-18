package org.kite9.diagram.adl;

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

	List<Symbol> symbols;
	
	public String getBoldText() {
		return boldText;
	}

	public void setBoldText(String boldText) {
		this.boldText = boldText;
	}

	public Key(String boldText, String bodyText, List<Symbol> symbols) {
		this.boldText = boldText;
		this.bodyText = bodyText;
		this.symbols = symbols;
	}
	
	public void setRenderingInformation(RenderingInformation ri) {
		this.renderingInformation = ri;
	}

	public List<Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<Symbol> symbols) {
		this.symbols = symbols;
	}
	
	public String toString() {
		return "KEY";
	}

	public boolean hasContent() {
		return hasContent(boldText) || hasContent(bodyText) || hasContent(symbols);
	}
	
	
}
