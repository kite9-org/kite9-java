package org.kite9.diagram.builders.wizards.sequence;

import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.primitives.Label;

public abstract class Step implements NounPart {
	
	public Label getFromLabel() {
		return fromLabel;
	}
	public Label getToLabel() {
		return toLabel;
	}

	
	public Step(Label fromLabel, Label toLabel) {
		super();
		this.fromLabel = fromLabel;
		this.toLabel = toLabel;
	}

	private Label fromLabel;
	private Label toLabel;
	
	


	public Step() {
		super();
	}

}