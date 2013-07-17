package org.kite9.diagram.position;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


/**
 * Contains details of how to render a rectangle on screen, possibly containing some 
 * text.
 * 
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("rectangle")
public class RectangleRenderingInformation extends AbstractRenderingInformation implements Cloneable {

	public RectangleRenderingInformation(Dimension2D position, Dimension2D size, HPos horizontalJustification,
			VPos verticalJustification, boolean rendered, Double pw, Double ph) {
		super();
		this.position = position;
		this.size = size;
		this.horizontalJustification = horizontalJustification;
		this.verticalJustification = verticalJustification;
		this.rendered = rendered;
		this.preferredHeight = ph;
		this.preferredWidth = pw;
	}
	
	public RectangleRenderingInformation(Dimension2D position, Dimension2D size, HPos horizontalJustification,
			VPos verticalJustification, boolean rendered) {
		super();
		this.position = position;
		this.size = size;
		this.horizontalJustification = horizontalJustification;
		this.verticalJustification = verticalJustification;
		this.rendered = rendered;
		this.preferredHeight = null;
		this.preferredWidth = null;
	}

	private static final long serialVersionUID = -1127180325861115291L;

	Dimension2D position = new Dimension2D();
	Dimension2D size = new Dimension2D();
	Dimension2D internalPosition;
	@XStreamOmitField
	boolean multipleHorizontalLinks;
	public boolean isMultipleHorizontalLinks() {
		return multipleHorizontalLinks;
	}

	public void setMultipleHorizontalLinks(boolean multipleHorizontalLinks) {
		this.multipleHorizontalLinks = multipleHorizontalLinks;
	}

	public boolean isMultipleVerticalLinks() {
		return multipleVerticalLinks;
	}

	public void setMultipleVerticalLinks(boolean multipleVerticalLinks) {
		this.multipleVerticalLinks = multipleVerticalLinks;
	}

	@XStreamOmitField
	boolean multipleVerticalLinks;
	
	
	public Dimension2D getInternalPosition() {
		return internalPosition;
	}

	public void setInternalPosition(Dimension2D internalPosition) {
		this.internalPosition = internalPosition;
	}

	public Dimension2D getInternalSize() {
		return internalSize;
	}

	public void setInternalSize(Dimension2D internalSize) {
		this.internalSize = internalSize;
	}

	Dimension2D internalSize;
	
	HPos horizontalJustification = null;
	VPos verticalJustification = null;
	
	@XStreamAsAttribute
	private Double preferredWidth;
	
	public Double getPreferredWidth() {
		return preferredWidth;
	}

	public void setPreferredWidth(Double preferredWidth) {
		this.preferredWidth = preferredWidth;
	}

	public Double getPreferredHeight() {
		return preferredHeight;
	}

	public void setPreferredHeight(Double preferredHeight) {
		this.preferredHeight = preferredHeight;
	}

	@XStreamAsAttribute
	private Double preferredHeight;
	
	
	
	public RectangleRenderingInformation() {
	}
	
	public String toString() {
		return "[("+position.x()+","+position.y()+")-("+size.x()+","+size.y()+")]";
	}

	public Dimension2D getPosition() {
		return position;
	}

	public void setPosition(Dimension2D position) {
		this.position = position;
	}

	public Dimension2D getSize() {
		return size;
	}

	public void setSize(Dimension2D size) {
		this.size = size;
	}

	public HPos getHorizontalJustification() {
		return horizontalJustification;
	}

	public void setHorizontalJustification(HPos horizontalJustification) {
		this.horizontalJustification = horizontalJustification;
	}

	public VPos getVerticalJustification() {
		return verticalJustification;
	}

	public void setVerticalJustification(VPos verticalJustification) {
		this.verticalJustification = verticalJustification;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	
}
