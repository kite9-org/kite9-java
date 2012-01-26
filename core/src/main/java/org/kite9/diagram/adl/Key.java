package org.kite9.diagram.adl;

import java.util.List;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractLabel;
import org.kite9.diagram.primitives.Label;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
	
	public List<Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(List<Symbol> symbols) {
		this.symbols = symbols;
	}
	
	@XStreamOmitField
	Object labelling;

	public Object getParent() {
		return labelling;
	}

	public void setParent(Object el) {
		this.labelling = el;
	}
	
	public RenderingInformation getRenderingInformation() {
		if (renderingInformation==null) { 
			renderingInformation = new RectangleRenderingInformation();
		}
		
		return renderingInformation;
	}
	
	public String toString() {
		return "KEY";
	}

	public boolean hasContent() {
		return hasContent(boldText) || hasContent(bodyText) || hasContent(symbols);
	}
	
		
	
	
	
	
	
	
	
	
	
	
	
}
