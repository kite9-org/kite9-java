package org.kite9.diagram.builders.wizards.sequence;

import org.kite9.diagram.builders.noun.SimpleNoun;
import org.kite9.diagram.primitives.Label;

public class CallStep extends Step {

	public SimpleNoun getTo() {
		return to;
	}
	public SimpleNoun getToGroup() {
		return toGroup;
	}
	
	public CallStep(SimpleNoun to, SimpleNoun toGroup, Label fromLabel, Label toLabel) {
		super(fromLabel, toLabel);
		this.to = to;
		this.toGroup = toGroup;
	}

	private SimpleNoun to;
	private SimpleNoun toGroup;
	
	@Override
	public String toString() {
		return "CallStep [to=" + to + ", toGroup=" + toGroup + "]";
	}
	
	

}
