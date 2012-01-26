package org.kite9.diagram.builders.wizards.fsm;

import org.kite9.diagram.builders.noun.SimpleNoun;

public class Transition {
	
	SimpleNoun[] from;
	SimpleNoun[] to;
	SimpleNoun transition;
	
	public Transition(SimpleNoun[] fromStates, SimpleNoun[] toStates, SimpleNoun transition) {
		this.from = fromStates;
		this.to = toStates;
		this.transition = transition;
	}

	public SimpleNoun[] getFromStates() {
		return from;
	}
	
	public SimpleNoun[] getToStates() {
		return to;
	}
	
	public SimpleNoun getTransition() {
		return transition;
	}
}
