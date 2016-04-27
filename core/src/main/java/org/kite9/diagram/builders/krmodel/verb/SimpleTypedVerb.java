package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.position.Direction;

public class SimpleTypedVerb extends AbstractTypedVerb {

	public SimpleTypedVerb(String name, AbstractTypedVerb active) {
		super(name, active);
	}

	public SimpleTypedVerb(String name, Direction d, NounPart subjectType, NounPart objectType) {
		super(name, name, d, subjectType, objectType);
	}

	public SimpleTypedVerb(String name, NounPart subjectType, NounPart objectType) {
		super(name, name, subjectType, objectType);
	}

	public String getLabel() {
		return name;
	}

	public Address extend(Address in) {
		return in.extend(VERB_COMPOUND_COMPONENT, id);
	}

}
