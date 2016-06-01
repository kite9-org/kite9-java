package org.kite9.diagram.primitives;

import org.kite9.diagram.adl.ADLDocument;
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
	public AbstractConnection(String id, String tag, Connected from, Connected to, Direction drawDirection, Object fromDecoration, Label fromLabel, Object toDecoration, Label tolabel, ADLDocument doc) {
		super(id, tag, from, to, drawDirection, doc);
		setFromDecoration(fromDecoration);
		setToDecoration(toDecoration);
		
		if (fromLabel!=null) {
			setFromLabel(fromLabel);
		}
		
		if (tolabel!=null) { 
			setToLabel(tolabel);
		}
		
		from.addLink(this);
		to.addLink(this);
	}
	
	public abstract Object getFromDecoration();

	public abstract Object getToDecoration();

	public Label getFromLabel() {
		return getProperty("fromLabel", Label.class);
	}

	public Label getToLabel() {
		return getProperty("toLabel", Label.class);
	}

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
	    replaceProperty("fromLabel", fromLabel, Label.class);
	}

	public void setToLabel(Label toLabel) {
	    replaceProperty("toLabel", toLabel, Label.class);
	}
}