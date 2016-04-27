package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.position.Direction;

public abstract class AbstractTypedVerb extends AbstractVerb implements TypedVerb {
	
	private final NounPart subjectType;
	private final NounPart objectType; 
	
	public AbstractTypedVerb(String id, String name, Direction d, NounPart subjectType, NounPart objectType) {
		super(id, name, d);
		this.subjectType = subjectType;
		this.objectType = objectType;
	}

	public AbstractTypedVerb(String id, String name, NounPart subjectType, NounPart objectType) {
		this(id, name, null, subjectType, objectType);
	}

	public AbstractTypedVerb(String name, AbstractTypedVerb active) {
		super(name, active);
		this.subjectType = active.subjectType;
		this.objectType = active.objectType;
	}

	public NounPart getSubjectType() {
		return subjectType;
	}
	
	public NounPart getObjectType() {
		return objectType;
	}
	
	
}
