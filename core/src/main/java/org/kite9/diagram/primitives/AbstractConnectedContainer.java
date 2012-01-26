package org.kite9.diagram.primitives;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.position.Layout;
import org.kite9.diagram.position.RectangleRenderingInformation;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public abstract class AbstractConnectedContainer extends AbstractConnectedContained implements Container {

	public Label getLabel() {
		return label;
	}

	public RectangleRenderingInformation getRenderingInformation() {
		if (renderingInformation==null) {
			renderingInformation = new RectangleRenderingInformation();
		}
		
		return (RectangleRenderingInformation) renderingInformation;
	}

	private static final long serialVersionUID = 9108816802892206563L;

	@XStreamImplicit
	private List<Contained> contents = new ArrayList<Contained>();
	
	@XStreamAsAttribute
	private Layout layout = null;
	
	@XStreamAsAttribute
	protected Label label = null;

	public List<Contained> getContents() {
		return contents;
	}

	public AbstractConnectedContainer() {
	}
	
	public AbstractConnectedContainer(String id, List<Contained> contents, Layout d, Label label) {
		super(id);
		this.contents = contents;
		for (Contained c : contents) {
			c.setContainer(this);
		}
		
		this.layout = d;
		if (label!=null) { 
			this.label = label;
			label.setParent(this);
		}
	}

	public Layout getLayoutDirection() {
		return layout;
	}

	public void setLayoutDirection(Layout layout) {
	    this.layout = layout;
	}

	public void setLabel(Label label) {
	    this.label = label;
	    label.setParent(this);
	}
}
