package org.kite9.diagram.adl;

import org.kite9.diagram.position.Direction;
import org.kite9.diagram.primitives.AbstractConnection;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.logging.LogicException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * Joins glyphs and arrows to one another. 
 * Can have text attached to the 'to' end, and decoration at the 'to' end
 * 
 * Set the drawDirection if the rendering system should draw the link in a particular orientation on the
 * diagram.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("link")
public class Link extends AbstractConnection {

	private static final long serialVersionUID = -5950978530304852748L;

	public Link() {
	}
	
	public Link(Connected from, Connected to) {
		super(from, to, null, null, null, null, null);
	}
	
	public Link(Connected from, Connected to, LinkEndStyle fromStyle, Label fromLabel, LinkEndStyle toEndStyle, Label toLabel) {
		this(from, to, fromStyle, fromLabel, toEndStyle, toLabel, null);
	}

	public Link(Connected from, Connected to, LinkEndStyle fromStyle, Label fromLabel, LinkEndStyle toEndStyle, Label toLabel, Direction drawDirection) {
		super(from, to, drawDirection, fromStyle, fromLabel, toEndStyle, toLabel);
	}
	
	@XStreamAsAttribute
	private LinkLineStyle style = null;

	public LinkLineStyle getStyle() {
		if (style==null) {
			// defaults to normal
			return LinkLineStyle.NORMAL;
		} else {
			return style;
		}
	}

	public void setStyle(LinkLineStyle style) {
		this.style = style;
	}

	LinkEndStyle fromDecoration;
	LinkEndStyle toDecoration;
	
	
	@Override
	public Object getFromDecoration() {
		return fromDecoration;
	}

	@Override
	public Object getToDecoration() {
		return toDecoration;
	}

	@Override
	public void setFromDecoration(Object fromDecoration) {
		if (fromDecoration instanceof LinkEndStyle) {
			this.fromDecoration = (LinkEndStyle) fromDecoration;
		} else if (fromDecoration!=null) {
			throw new LogicException("From Decoration should be LinkEndStyle");
		}
	}

	@Override
	public void setToDecoration(Object toDecoration) {
		if (toDecoration instanceof LinkEndStyle) {
			this.toDecoration = (LinkEndStyle) toDecoration;
		} else if (toDecoration!=null) {
			throw new LogicException("To Decoration should be LinkEndStyle");
		}
	}
	
}
