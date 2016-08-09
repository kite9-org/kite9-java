package org.kite9.diagram.xml;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.adl.AbstractLabel;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.adl.TextContainingDiagramElement;
import org.w3c.dom.Node;

public class Key extends AbstractLabel implements Label {

	private static final long serialVersionUID = 7705875104684442878L;
	
	public XMLElement getBodyText() {
		return getProperty("bodyText", XMLElement.class);
	}

	public void setBodyText(XMLElement bodyText) {
		replaceProperty("bodyText", bodyText, XMLElement.class);
	}
	
	public XMLElement getBoldText() {
		return getProperty("boldText", XMLElement.class);
	}

	public void setBoldText(XMLElement boldText) {
		replaceProperty("boldText", boldText, XMLElement.class);
	}
	
	public ContainerProperty<TextLine> convert(List<Symbol> symbols) {
		@SuppressWarnings("unchecked")
		ContainerProperty<TextLine> out = (ContainerProperty<TextLine>) ownerDocument.createElement("text-lines");
		if (symbols == null) {
			return out;
		}

		for (Symbol s : symbols) {
			List<Symbol> sl = new ArrayList<Symbol>(1);
			sl.add(s);
			out.appendChild(new TextLine(null, "text-line", s.getText(), sl, (ADLDocument) ownerDocument));
		}
		return out;
	}
	
	public Key() {		
		this.tagName = "key";
	}
	

	public Key(String boldText, String bodyText, List<Symbol> symbols) {
		this(null, boldText, bodyText, symbols, TESTING_DOCUMENT);
	}

	
	public Key(String id, String boldText, String bodyText, List<Symbol> symbols, ADLDocument doc) {
		super(id, "key", doc);
		
		if (boldText != null) {
			setBoldText(new TextLine(null, "boldText", boldText, null, doc));
		}

		if (bodyText != null) {
			setBodyText(new TextLine(null, "bodyText", bodyText, null, doc));
		}

		if (symbols != null) {
			setSymbols(convert(symbols));
		}
	}
	
	@SuppressWarnings("unchecked")
	public ContainerProperty<TextLine> getSymbols() {
		return getProperty("text-lines", ContainerProperty.class);
	}

	public void setSymbols(ContainerProperty<TextLine> symbols) {
		replaceProperty("text-lines", symbols, ContainerProperty.class);
	}
	
	public String toString() {
		return "KEY";
	}

	public boolean hasContent() {
		return hasContent(getBoldText()) || hasContent(getBodyText()) || hasContent(getSymbols());
	}

	@Override
	protected Node newNode() {
		return new Key();
	}

	public DiagramElement getOwner() {
		return (DiagramElement) getParent();
	}
	
	
}
