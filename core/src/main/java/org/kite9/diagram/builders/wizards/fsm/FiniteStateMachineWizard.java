package org.kite9.diagram.builders.wizards.fsm;

import java.util.HashMap;
import java.util.Map;

import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.diagram.builders.InsertionInterface;
import org.kite9.diagram.builders.noun.NounFactory;
import org.kite9.diagram.builders.noun.SimpleNoun;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * Finite state machine format has a number of glyphs representing states, and
 * arrows representing the transitions to move between the states.
 * 
 * @author robmoffat
 * 
 */
public class FiniteStateMachineWizard {

	InsertionInterface ii;
	NounFactory nf;

	public FiniteStateMachineWizard(DiagramBuilder db) {
		this(db.getInsertionInterface(), db.getNounFactory());
	}

	public FiniteStateMachineWizard(InsertionInterface ii, NounFactory nf) {
		this.ii = ii;
		this.nf = nf;
	}

	/**
	 * Formats the information in the provider, putting all of the created
	 * elements into the container.
	 */
	public void write(FSMDataProvider provider, Container c) {
		Map<Object, DiagramElement> stateMap = new HashMap<Object, DiagramElement>();

		for (SimpleNoun o : provider.getStates()) {
			DiagramElement de = ii.returnGlyph(c, o, o.getLabel(), o.getStereotype());
			if (!(de instanceof Connected)) {
				throw new Kite9ProcessingException(o + " already exists in the diagram as a " + de.getClass()
						+ " (Connected needed)");
			}
			stateMap.put(o, de);
		}

		for (Transition t : provider.getTransitions()) {
			if (t != null) {
				SimpleNoun o = t.getTransition();
				DiagramElement de = ii.returnArrow(c, o, o.getLabel());
				if (!(de instanceof Connected)) {
					throw new Kite9ProcessingException(o + " already exists in the diagram as a " + de.getClass()
							+ " (Connected needed)");
				}

				stateMap.put(o, de);

				for (Object from : t.getFromStates()) {
					DiagramElement deFrom = stateMap.get(from);
					ii.returnLink(deFrom, de, null, null, false, null);
				}

				for (Object to : t.getToStates()) {
					DiagramElement deTo = stateMap.get(to);
					ii.returnLink(de, deTo, null, null, true, null);
				}
			}
		}
	}

}
