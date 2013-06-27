package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractConnectedContained;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.Leaf;
import org.kite9.diagram.primitives.SymbolTarget;
import org.kite9.diagram.style.StyledDiagramElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * A Glyph is a white node on the diagram which has a fixed hierarchical position
 * within its container.  It has a label and optionally a type, and it can optionally
 * contain multiple rows of text.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("glyph")
public class Glyph extends AbstractConnectedContained implements Leaf, SymbolTarget, StyledDiagramElement {

	private static final long serialVersionUID = -6572545083931316651L;
	
	private String stereotype;
	
	private String label;
		
	public String getLabel() {
		return label;
	}

	public void setLabel(String name) {
		this.label = name;
	}


	@XStreamAlias("text-lines")
	private List<CompositionalDiagramElement> text = new ArrayList<CompositionalDiagramElement>();
	
	private List<Symbol> symbols = new ArrayList<Symbol>();
	
	public Glyph() {
	}
	
	/**
	 * Adds a divider to the glyph, so that it is backwards compatible with tests
	 */
	private List<CompositionalDiagramElement> convertText(List<TextLine> t, boolean divider) {
		if ((t==null) || (t.size()==0)) {
			return null;
		}
		List<CompositionalDiagramElement> result = new ArrayList<CompositionalDiagramElement>(t.size() + 1);
		if (divider) {
			CompositionalShape d = new CompositionalShape("divider");
			d.setParent(this);
			result.add(d);
		}
		for (TextLine diagramElement : t) {
			result.add(diagramElement);
		}
		return result;
	}
	
	public Glyph(String id, String stereotype, String label,  List<TextLine> text, List<Symbol> symbols, boolean divider) {
		super(id);
		this.stereotype = stereotype;
		if (text!=null) {
			setText(convertText(text, divider));
		}
		if (symbols!=null) {
			this.symbols = symbols;
		}
		this.label = label;
	}

	public Glyph(String id, String stereotype, String label,  List<TextLine> text, List<Symbol> symbols) {
		this(id, stereotype, label, text, symbols, true);
	}
	
	public Glyph(String sterotype, String label, List<TextLine> text, List<Symbol> symbols) {
		this(createID(), sterotype, label,text, symbols);
	}

	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String sterotype) {
		this.stereotype = sterotype;
	}

	public List<CompositionalDiagramElement> getText() {
		return text;
	}

	public void setText(List<CompositionalDiagramElement> text) {
		this.text = text;
		for (CompositionalDiagramElement compositionalDiagramElement : text) {
			compositionalDiagramElement.setParent(this);
		}
	}


	public List<Symbol> getSymbols() {
		return symbols;
	}
	
	public void setSymbols(List<Symbol> syms) {
		this.symbols = syms;
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
	
	@XStreamAsAttribute
	String shape;

	public String getShapeName() {
		return shape;
	}

	public void setShapeName(String name) {
		this.shape = name;
	}
	
}