package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.position.Direction;

/**
 * In a has-relationship, the name specifies the type of thing that is had,
 * rather than the type of the relationship.  This indicates that an element is 
 * part of another, larger element.  
 * 
 * @author moffatr
 * 
 */
public class ComposesVerb extends AbstractTypedVerb {
	
	public static final String COMPOSES_VERB = "composes";
	public static final String COMPOSES_PART = "part";
	
	public ComposesVerb(ComposesVerb active) {
		super(active.name, active);
	}

	public ComposesVerb(NounPart name, Direction d, NounPart subjectType, NounPart objectType) {
		super(createID(name), name.getLabel(), d, subjectType, objectType);
	}

	private static String createID(NounPart name) {
		return name.extend(new AddressImpl(Verb.VERB_COMPOUND_COMPONENT, COMPOSES_VERB)).toString();
	}

	public ComposesVerb(NounPart name, NounPart subjectType, NounPart objectType) {
		super(createID(name), name.getLabel(), subjectType, objectType);
	}
	
	public ComposesVerb(NounPart name, NounPart subjectType) {
		super(createID(name), name.getLabel(), subjectType, name);
	}

	public Address extend(Address in) {
		return in.extend(Verb.VERB_COMPOUND_COMPONENT, COMPOSES_VERB).extend(COMPOSES_PART, name);
	}

	public String getLabel() {
		switch (getType()) {
		case ACTIVE:
		case BIDIRECTIONAL:
			return "has-"+name;
			
		case PASSIVE:
		default:
			return name+"-of";
		}
	}

	
	
}
