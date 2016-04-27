package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.position.Direction;

public interface Verb extends Knowledge {

	public static final String VERB_COMPOUND_COMPONENT = "verb";

	public enum VerbType {
		ACTIVE, PASSIVE, BIDIRECTIONAL
	};
	
	public VerbType getType();

	public Direction getDirection();
	
	public Verb getActiveRelationship();

}