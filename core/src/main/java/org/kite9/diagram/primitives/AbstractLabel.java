package org.kite9.diagram.primitives;

import java.io.Serializable;

import org.kite9.diagram.adl.ADLDocument;
import org.kite9.diagram.adl.ContainerProperty;
import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;

/**
 * 
 * @author robmoffat
 * 
 */
public abstract class AbstractLabel extends AbstractIdentifiableDiagramElement implements Serializable, Label {

	private static final long serialVersionUID = -1012880238215021108L;	
	
	public AbstractLabel() {
		super();
	}

	public AbstractLabel(String id, String tag, ADLDocument doc) {
		super(id, tag, doc);
	}

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
	
	public boolean hasContent(TextContainingDiagramElement s) {
		if (s!=null) {
			return hasContent(s.getText());
		} else {
			return false;
		}
	}
	
	public boolean hasContent(ContainerProperty<?> c) {
		if (c!=null) {
			return c.size() >0;
		} else {
			return false;
		}
	}

}