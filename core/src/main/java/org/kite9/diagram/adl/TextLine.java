package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.primitives.AbstractLabel;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.IdentifiableDiagramElement;
import org.kite9.diagram.primitives.SymbolTarget;
import org.kite9.diagram.primitives.TextContainingDiagramElement;
import org.w3c.dom.Node;

/**
 * TODO: rename to text-box.  This is a formatted area containing text, and could
 * consist of several lines of text.
 * 
 * @author robmoffat
 *
 */
public class TextLine extends AbstractLabel implements SymbolTarget, IdentifiableDiagramElement, TextContainingDiagramElement {

	private static final long serialVersionUID = -1917135065467101779L;
	

	List<Symbol> symbols = new ArrayList<Symbol>();

	public TextLine() {
		this.tagName = "text-line";
	}
	
	public TextLine(String text, ADLDocument doc) {
		this(null, "text-line", text, null, doc);
	}
	
	public TextLine(String text) {
		this(text, TESTING_DOCUMENT);
	}
	
	public TextLine(String text, List<Symbol> symbols) {
		this(null, "text-line", text, symbols, TESTING_DOCUMENT);
	}

	
	public TextLine(String id, String tag, String text, List<Symbol> symbols, ADLDocument doc) {
		super(id, tag, doc);
		setText(text);
		if (symbols != null) {
			setSymbols(new ContainerProperty<Symbol>("symbols", doc, symbols));
		}
	}

	@SuppressWarnings("unchecked")
	public ContainerProperty<Symbol> getSymbols() {
		return getProperty("symbols", ContainerProperty.class);
	}
	
	public void setSymbols(ContainerProperty<Symbol> syms) {
		replaceProperty("symbols", syms, ContainerProperty.class);
	}
	
	public String toString() {
		return "[TL:"+getText()+"]";
	}

	public boolean hasContent() {
		return hasContent(getText()) || hasContent(getSymbols());
	}

	@Override
	protected Node newNode() {
		return new TextLine();
	}

	public void setText(String text) {
		setTextData(text);
	}

	public String getText() {
		return getTextData();
	}

	public DiagramElement getOwner() {
		Node parent = getParentNode();
		if (parent instanceof ContainerProperty) {
			return (DiagramElement) parent.getParentNode();
		} else {
			return (DiagramElement) parent;
		}
		
	}

}
