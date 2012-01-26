package org.kite9.diagram.builders.wizards.sequence;

import org.kite9.diagram.primitives.Label;

public class ReturnStep extends Step {
	
	boolean show;
	
	public boolean isShow() {
		return show;
	}
	
	public ReturnStep(Label fromLabel, Label toLabel, boolean show) {
		super(fromLabel, toLabel);
		this.show = show;
	}

	@Override
	public String toString() {
		return "ReturnStep [show=" + show + "]";
	}
	
	

}
