package org.kite9.diagram.adl;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.AbstractConnectedContainer;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.Label;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


/**
 * A context is a portion of the diagram with a border around it, and a label.  
 * It contains other Glyphs or context to give the diagram a hierarchy.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("context")
public class Context extends AbstractConnectedContainer {
	
	@Override
	public String toString() {
		return "[C:"+getID()+"]";
	}

	private static final long serialVersionUID = -311300007972605832L;
	
	@XStreamOmitField
	private Container container;
	
	
	@XStreamAsAttribute
	private boolean bordered = true;

	public Context() {
	}
	
	public Context(List<Contained> contents, boolean bordered, Label label, Layout layoutDirection) {
		super(createID(), contents==null ? new ArrayList<Contained>() : contents, layoutDirection, label);	
		this.bordered = bordered;
	}
	
	public Context(String id, List<Contained> contents, boolean bordered, Label label, Layout layoutDirection) {
		super(id, contents==null ? new ArrayList<Contained>() : contents, layoutDirection, label);
		this.bordered = bordered;
	}

	public boolean isBordered() {
		return bordered;
	}

	public void setBordered(boolean bordered) {
		this.bordered = bordered;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container c) {
		this.container =  c;
	}
	
}
