package org.kite9.diagram.builders.wizards.fsm;

import org.kite9.diagram.builders.krmodel.SimpleNoun;

public interface FSMDataProvider {

	public SimpleNoun[] getStates();
	
	public Transition[] getTransitions();
	
}
