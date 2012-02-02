package org.kite9.diagram.builders.wizards.fsm;

import org.kite9.diagram.builders.formats.BasicFormats;
import org.kite9.diagram.builders.formats.NounFormat;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.Relationship;
import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;

/**
 * Finite state machine format has a number of glyphs representing states, and
 * arrows representing the transitions to move between the states.
 * 
 * @author robmoffat
 * 
 */
public class FiniteStateMachineWizard {

	NounFormat stateFormat;
	NounFactory nf;
	NounFormat transitionFormat;
	Container c;
	DiagramBuilder db;
	Relationship stateTransitionRelationship;

	public FiniteStateMachineWizard(DiagramBuilder db, Container c) {
		this(db.getNounFactory());
		stateFormat = BasicFormats.asGlyph(null);
		transitionFormat = BasicFormats.asConnectionBody();
		stateTransitionRelationship = new Relationship("transits to");
		this.c = c;
		this.db = db;
	}

	public FiniteStateMachineWizard(NounFactory nf) {
		this.nf = nf;
	}

	/**
	 * Formats the information in the provider, putting all of the created
	 * elements into the container.
	 */
	public void write(FSMDataProvider provider) {
		for (SimpleNoun o : provider.getStates()) {
			stateFormat.returnElement(c, o, db.getInsertionInterface());
		}

		for (Transition t : provider.getTransitions()) {
			if (t != null) {
				SimpleNoun o = t.getTransition();
				DiagramElement body = transitionFormat.returnElement(c, o, db.getInsertionInterface());
			
				for (SimpleNoun from : t.getFromStates()) {
					DiagramElement fromEl = db.getInsertionInterface().returnExisting(from);
					db.getInsertionInterface().returnConnection(fromEl, body, null, null, null, false, null);
				}

				for (SimpleNoun to : t.getToStates()) {
					DiagramElement toEl = db.getInsertionInterface().returnExisting(to);
					db.getInsertionInterface().returnConnection(body, toEl, null, null, null, true, null);
				}
			}
		}
	}
}
