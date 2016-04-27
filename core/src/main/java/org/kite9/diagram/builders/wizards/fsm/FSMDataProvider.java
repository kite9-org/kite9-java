package org.kite9.diagram.builders.wizards.fsm;

import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;

public interface FSMDataProvider {

	public SimpleNoun[] getStates();
	
	public Transition[] getTransitions();
	
}
