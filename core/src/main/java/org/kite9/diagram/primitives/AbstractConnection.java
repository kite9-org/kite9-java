package org.kite9.diagram.primitives;

import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.RouteRenderingInformation;

/**
 * This is the base class for connections within the diagram.  i.e. Links.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractConnection extends AbstractBiDirectional<Connected> implements Connection {

	private static final long serialVersionUID = -1941426216200603569L;
	
	/**
	 * For serialization
	 */
	public AbstractConnection() {
	}
	
	/**
	 * Call this with modify verteConnected false to avoid adding the edge connection to the vertex
	 */
	public AbstractConnection(Connected from, Connected to, Direction drawDirection, Object fromDecoration, Label fromLabel, Object toDecoration, Label tolabel) {
		super(from, to, drawDirection);
		setFromDecoration(fromDecoration);
		setToDecoration(toDecoration);
		if (fromLabel!=null) {
			this.fromLabel = fromLabel;
			fromLabel.setParent(this);
		}
		if (tolabel!=null) { 
			this.toLabel = tolabel;
			tolabel.setParent(this);
		}
		from.addLink(this);
		to.addLink(this);
	}
	
	public abstract Object getFromDecoration();

	public abstract Object getToDecoration();

	public Label getFromLabel() {
		return fromLabel;
	}

	public Label getToLabel() {
		return toLabel;
	}

	protected Label fromLabel;
	protected Label toLabel;
		
	public int compareTo(DiagramElement o) {
		if (o!=null) {
			return this.toString().compareTo(o.toString());
		} else {
			return -1;
		}
	}
	
	public RouteRenderingInformation getRenderingInformation() {
		if (renderingInformation==null) {
			renderingInformation = new RouteRenderingInformation();
		}
		
		return (RouteRenderingInformation) renderingInformation;
	}

	public abstract void setFromDecoration(Object fromDecoration);

	public abstract void setToDecoration(Object toDecoration);

	public void setFromLabel(Label fromLabel) {
	    this.fromLabel = fromLabel;
	}

	public void setToLabel(Label toLabel) {
	    this.toLabel = toLabel;
	}
}