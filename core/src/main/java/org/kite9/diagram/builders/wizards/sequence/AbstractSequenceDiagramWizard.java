package org.kite9.diagram.builders.wizards.sequence;

import org.kite9.diagram.builders.formats.InsertionInterface;
import org.kite9.diagram.builders.krmodel.KRDiagramBuilder;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.alias.Aliaser;

/**
 * This wizard represents sequence flow within a diagram by showing a number of contexts 
 * representing the entities involved in an operation.  Within these contexts are the operations
 * that each entity performs, as glyphs.  The links between the glyphs represent control flow.
 * 
 * @author robmoffat
 * 
 */
public abstract class AbstractSequenceDiagramWizard {

	InsertionInterface ii;
	NounFactory nf;
	Aliaser a;

	public AbstractSequenceDiagramWizard(KRDiagramBuilder db) {
		this(db.getInsertionInterface(), db.getNounFactory(), db.getAliaser());
	}

	public AbstractSequenceDiagramWizard(InsertionInterface ii, NounFactory nf, Aliaser a) {
		this.ii = ii;
		this.nf = nf;
		this.a = a;
	}

	protected Label buildFromLabel(Step s, Label existing) {
		if (existing != null) {
			return existing;
		}
		return s.getFromLabel();
	}
	
	protected Label buildToLabel(Step s, Label existing) {
		if (existing != null) {
			return existing;
		}
		return s.getToLabel();
	}

	protected Direction getDirectionFor(Layout contextLayout) {
		if (contextLayout==null) { 
			return null;
		}
		switch (contextLayout) {
		case UP:
			return Direction.UP;
		case DOWN:
			return Direction.DOWN;
		case LEFT:
			return Direction.LEFT;
		case RIGHT:
			return Direction.RIGHT;
			
			default:
				return null;
		}
	}

}
