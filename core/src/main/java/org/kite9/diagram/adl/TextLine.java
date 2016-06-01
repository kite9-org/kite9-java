package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractLabel;
import org.kite9.diagram.primitives.IdentifiableDiagramElement;
import org.kite9.diagram.primitives.SymbolTarget;
import org.kite9.diagram.primitives.TextContainingDiagramElement;
import org.w3c.dom.Node;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * TODO: rename to text-box.  This is a formatted area containing text, and could
 * consist of several lines of text.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("text-line")
public class TextLine extends AbstractLabel implements SymbolTarget, IdentifiableDiagramElement, TextContainingDiagramElement {

	private static final long serialVersionUID = -1917135065467101779L;
	

	List<Symbol> symbols = new ArrayList<Symbol>();

	public TextLine() {
		this.tagName = "text-line";
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

	RenderingInformation renderingInformation;
	
	public RenderingInformation getRenderingInformation() {
		if (renderingInformation == null) {
			renderingInformation = new RectangleRenderingInformation();
		}
		
		return renderingInformation;
	}
	
	public String toString() {
		return "[TL:"+getText()+"]";
	}

	public boolean hasContent() {
		return hasContent(getText()) || hasContent(getSymbols());
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.renderingInformation = ri;
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

}
