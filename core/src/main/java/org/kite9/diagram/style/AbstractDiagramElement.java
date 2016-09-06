package org.kite9.diagram.style;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.HintMap;

public abstract class AbstractDiagramElement implements DiagramElement {

	protected DiagramElement parent;
	protected HintMap hints;
	
	public AbstractDiagramElement(DiagramElement parent) {
		super();
		this.parent = parent;
	}

	public int compareTo(DiagramElement o) {
		return getID().compareTo(o.getID());
	}

	@Override
	public int hashCode() {
		String id = getID();
		return id.hashCode();
	}

	public AbstractDiagramElement() {
		super();
	}

	public HintMap getPositioningHints() {
		return hints;
	}

	public void setPositioningHints(HintMap hints) {
		this.hints = hints;
	}

	@Override
	public DiagramElement getParent() {
		return parent;
	}

}