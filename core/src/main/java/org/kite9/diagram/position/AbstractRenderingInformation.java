package org.kite9.diagram.position;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class AbstractRenderingInformation implements RenderingInformation {

	private static final long serialVersionUID = 5784432791632841277L;
	@XStreamAsAttribute
	protected boolean rendered = true;
	public String path;
	public String perimeter;

	public AbstractRenderingInformation() {
		super();
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean r) {
		this.rendered = r;
	}

	public void setShapePath(String gp) {
		this.path = gp;
	}

	public String getShapePath() {
		return path;
	}
	
	public void setPerimeterPath(String gp) {
		this.perimeter = gp;
	}
	
	public String getPerimeterPath() {
		return perimeter;
	}

}