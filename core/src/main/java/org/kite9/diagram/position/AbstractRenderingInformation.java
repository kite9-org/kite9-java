package org.kite9.diagram.position;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Contains details of how the diagram element should be shown.  
 * To speed up the rendering process, path and perimeter should both be normalized to meet 0,0, and
 * an offset from that point should be given.  
 * 
 * This means that when an object moves, we don't need to change the path.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractRenderingInformation implements RenderingInformation {

	private static final long serialVersionUID = 5784432791632841277L;
	@XStreamAsAttribute
	protected boolean rendered = true;
	protected Object displayData;

	public Object getDisplayData() {
		return displayData;
	}

	public void setDisplayData(Object displayData) {
		this.displayData = displayData;
	}

	public AbstractRenderingInformation() {
		super();
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean r) {
		this.rendered = r;
	}
	
	private Dimension2D offset;

	public void setPathOffset(Dimension2D offset) {
		this.offset = offset;
	}
	
	public Dimension2D getPathOffset() {
		return this.offset;
	}
}