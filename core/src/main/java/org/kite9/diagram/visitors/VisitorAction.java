package org.kite9.diagram.visitors;

import org.kite9.diagram.style.DiagramElement;

public interface VisitorAction {

	public void visit(DiagramElement de);
}
