package org.kite9.diagram.primitives;

import java.io.Serializable;

import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.style.ShapedDiagramElement;
import org.kite9.diagram.style.StyledDiagramElement;
import org.kite9.framework.logging.LogicException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class AbstractIdentifiableDiagramElement implements IdentifiableDiagramElement, Serializable, StyledDiagramElement, ShapedDiagramElement {

	private static final long serialVersionUID = -3154895826494435557L;


	public AbstractIdentifiableDiagramElement() {
		super();
		this.id = createID();
	}

	public AbstractIdentifiableDiagramElement(String id) {
		if (id==null) {
			throw new LogicException("Cannot create diagram element with null id");
		}
		this.id = id;
	}

	public int compareTo(DiagramElement o) {
		if (o instanceof IdentifiableDiagramElement) {
			return id.compareTo(((IdentifiableDiagramElement)o).getID());
		} else if (o!=null) {
			return this.toString().compareTo(o.toString());
		} else {
			return -1;
		}
	}
	
	@XStreamAsAttribute
	private String id;
	

	@Override
	public int hashCode() {
		// doing this ensures repeatability in tests
		if (id==null) {
			this.id = createID();
		}
		return id.hashCode();
	}
	
	public final String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}

	/**
	 * This is used by layout engines to set the position of the elements in the
	 * diagram
	 */
	protected RenderingInformation renderingInformation = null;
	
	private static int counter = 0; 
	
	protected static synchronized String createID() {
		return AUTO_GENERATED_ID_PREFIX+counter++;
	}
	
	public static final String AUTO_GENERATED_ID_PREFIX = "auto:";


	public static void resetCounter() {
		counter = 0;
	}
	
	@XStreamAsAttribute
	protected String shape;
	
	@XStreamAsAttribute
	protected String style;

	@XStreamAsAttribute
	@XStreamAlias("class")
	protected String classes;
	
	public String getClasses() {
		return classes;
	}

	public void setClasses(String s) {
		this.classes = s;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String s) {
		this.style = s;
	}

	public String getShapeName() {
		return shape;
	}

	public void setShapeName(String name) {
		this.shape = name;
	}
	
	protected HintMap hints;


	public HintMap getPositioningHints() {
		return hints;
	}

	public void setPositioningHints(HintMap hints) {
		this.hints = hints;
	}
	
	
}