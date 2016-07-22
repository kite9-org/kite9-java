package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.primitives.AbstractLabel;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.diagram.primitives.TextContainingDiagramElement;
import org.w3c.dom.Node;

public class Key extends AbstractLabel implements Label {

	private static final long serialVersionUID = 7705875104684442878L;
	
	public TextContainingDiagramElement getBodyText() {
		return getProperty("bodyText", TextContainingDiagramElement.class);
	}

	public void setBodyText(TextContainingDiagramElement bodyText) {
		replaceProperty("bodyText", bodyText, TextContainingDiagramElement.class);
	}
	
	public TextContainingDiagramElement getBoldText() {
		return getProperty("boldText", TextContainingDiagramElement.class);
	}

	public void setBoldText(TextContainingDiagramElement boldText) {
		replaceProperty("boldText", boldText, TextContainingDiagramElement.class);
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
