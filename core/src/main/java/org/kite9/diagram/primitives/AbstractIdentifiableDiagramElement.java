package org.kite9.diagram.primitives;

import org.kite9.diagram.position.RenderingInformation;
import org.kite9.framework.logging.LogicException;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class AbstractIdentifiableDiagramElement implements IdentifiableDiagramElement {

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
			throw new LogicException("This diagram element should have an id: "+this);
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
		return "auto:"+counter++;
	}


	public static void resetCounter() {
		counter = 0;
	}
}