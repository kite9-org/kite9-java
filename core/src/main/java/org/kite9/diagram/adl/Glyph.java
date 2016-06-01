package org.kite9.diagram.adl;

import java.util.List;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractConnectedContained;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.Leaf;
import org.kite9.diagram.primitives.SymbolTarget;
import org.kite9.diagram.primitives.TextContainingDiagramElement;
import org.w3c.dom.Node;


/**
 * A Glyph is a white node on the diagram which has a fixed hierarchical position
 * within its container.  It has a label and optionally a type, and it can optionally
 * contain multiple rows of text.
 * 
 * @author robmoffat
 *
 */
public class Glyph extends AbstractConnectedContained implements Leaf, SymbolTarget {

	private static final long serialVersionUID = -6572545083931316651L;
		
	public TextContainingDiagramElement getLabel() {
		return getProperty("label", TextContainingDiagramElement.class);
	}

	public void setLabel(TextContainingDiagramElement name) {
		replaceProperty("label", name, TextContainingDiagramElement.class);
	}
	
	public Glyph() {
		this.tagName = "glyph";
	}
	
	public Glyph(String id, ADLDocument doc) {
		super(id, "glyph", doc);
	}
	
	public Glyph(String id, String stereotype, String label,  List<CompositionalDiagramElement> text, List<Symbol> symbols, boolean divider, ADLDocument doc) {
		super(id, "glyph", doc);
		
		if (stereotype != null) {
			setStereotype(new TextLine(null, "stereotype", stereotype, null, doc));
		}
		
		if (label != null) {
			setLabel(new TextLine(null, "label", label, null, doc));
		}
		
		if (text!=null) {
			setText(new ContainerProperty<CompositionalDiagramElement>("text-lines", doc, text));
		}
		
		if (symbols!=null) {
			setSymbols(new ContainerProperty<Symbol>("symbols", doc, symbols));
		}		
	}

	public TextContainingDiagramElement getStereotype() {
		return getProperty("stereotype", TextContainingDiagramElement.class);
	}

	public void setStereotype(TextContainingDiagramElement sterotype) {
		replaceProperty("stereotype", sterotype, TextContainingDiagramElement.class);
	}

	@SuppressWarnings("unchecked")
	public ContainerProperty<CompositionalDiagramElement> getText() {
		return getProperty("text-lines", ContainerProperty.class);
	}

	public void setText(ContainerProperty<CompositionalDiagramElement> text) {
		replaceProperty("text-lines", text, ContainerProperty.class);
	}


	@SuppressWarnings("unchecked")
	public ContainerProperty<Symbol> getSymbols() {
		return getProperty("symbols", ContainerProperty.class);
	}
	
	public void setSymbols(ContainerProperty<Symbol> syms) {
		replaceProperty("symbols", syms, ContainerProperty.class);
	}

	public boolean hasDimension() {
		return true;
	}
	
	
	public String toString() {
		return "[G:"+getID()+"]";
	}

	public RenderingInformation getRenderingInformation() {
		if (renderingInformation==null)
			renderingInformation = new RectangleRenderingInformation();
		
		return renderingInformation;
	}

	public void setRenderingInformation(RenderingInformation ri) {
		this.renderingInformation = ri;
	}

	@Override
	protected Node newNode() {
		return new Glyph(null, (ADLDocument) ownerDocument);
	}
	
}