package org.kite9.diagram.visitors;

import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;

/**
 * This visits only the Contexts and Glyphs within a diagram, traversing the
 * hierarchy in a structured way.
 * 
 * @author robmoffat
 *
 */
public abstract class ContainerVisitor {

	protected abstract void containerStart(Container c);
	protected abstract void containerEnd(Container c);
	
	protected abstract void contained(Contained g);
	
	public void visit(Container c) {
		containerStart(c);
		for (Contained c2 : c.getContents()) {
			contained(c2);
			if (c2 instanceof Container) {
				visit((Container) c2);
			}
		}
		containerEnd(c);
	}
	
	
}
