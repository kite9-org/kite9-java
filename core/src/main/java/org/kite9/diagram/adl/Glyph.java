package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.primitives.AbstractConnectedContained;
import org.kite9.diagram.primitives.Leaf;
import org.kite9.diagram.primitives.SymbolTarget;

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
public class Glyph extends AbstractConnectedContained implements Leaf, SymbolTarget {

	private static final long serialVersionUID = -6572545083931316651L;
	
	@XStreamAsAttribute
	private String stereotype;
	
	@XStreamAsAttribute
	private String label;
		
	public String getLabel() {
		return label;
	}

	public void setLabel(String name) {
		this.label = name;
	}


	@XStreamAlias("text-lines")
	private List<TextLine> text = new ArrayList<TextLine>();
	
	private List<Symbol> symbols = new ArrayList<Symbol>();
	
	public Glyph() {
	}
	
	public Glyph(String id, String stereotype, String label, List<TextLine> text, List<Symbol> symbols) {
		super(id);
		this.stereotype = stereotype;
		if (text!=null) {
			this.text = text;
		}
		if (symbols!=null) {
			this.symbols = symbols;
		}
		this.label = label;
	}
	
	public Glyph(String sterotype, String label, List<TextLine> text, List<Symbol> symbols) {
		this(createID(), sterotype, label, text, symbols);
	}

	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String sterotype) {
		this.stereotype = sterotype;
	}

	public List<TextLine> getText() {
		return text;
	}

	public void setText(List<TextLine> text) {
		this.text = text;
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
	
}