package org.kite9.diagram.builders.wizards.fsm;

import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.formats.adl.BasicFormats;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;
import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.diagram.builders.krmodel.verb.Verb;
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

	Format stateFormat;
	NounFactory nf;
	Format transitionFormat;
	Container c;
	DiagramBuilder db;
	Verb stateTransitionRelationship;

	public FiniteStateMachineWizard(DiagramBuilder db, Container c) {
		this(db.getNounFactory());
		stateFormat = BasicFormats.asGlyph(null);
		transitionFormat = BasicFormats.asConnectionWithBody();
		stateTransitionRelationship = new AbstractVerb("transits to");
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
