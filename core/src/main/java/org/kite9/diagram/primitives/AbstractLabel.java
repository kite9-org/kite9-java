package org.kite9.diagram.primitives;

import java.io.Serializable;
import java.util.Collection;

import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Base class of all diagram elements.
 * 
 * @author robmoffat
 * 
 */
public abstract class AbstractLabel extends AbstractIdentifiableDiagramElement implements Serializable, Label {

	private static final long serialVersionUID = -1012880238215021108L;

	/**
	 * This is very handy for ensuring repeatability in tests.  Override as necessary.
	 */
	public int compareTo(DiagramElement o) {
		if (o!=null) {
			return this.toString().compareTo(o.toString());
		} else {
			return -1;
		}
	}
	
	@XStreamOmitField
	Object parent;
	
	public void setParent(Object el) {
		this.parent = el;
	}

	public Object getParent() {
		return parent;
	}

	public RenderingInformation getRenderingInformation() {
		if (renderingInformation == null) {
			renderingInformation = new RectangleRenderingInformation();
		}
		
		return renderingInformation;
	}
	
	public boolean hasContent(String s) {
		if (s!=null) {
			return s.trim().length()>0;
		} else {
			return false;
		}
	}
	
	public boolean hasContent(Collection<?> c) {
		if (c!=null) {
			return c.size() >0;
		} else {
			return false;
		}
	}

}