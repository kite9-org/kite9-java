package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.krmodel.noun.NounPart;

/**
 * Identifies that some element possesses an attribute of a certain value.
 * Attributes can only be set once (e.g. Age) 
 * 
 * @author moffatr
 * 
 */
public class AttributeVerb extends AbstractVerb implements TypedVerb {
	
	public static final String ATTRIBUTE_VERB = "attribute";
	public static final String ATTRIBUTE_PART = "part";

	public AttributeVerb(String id, String name, NounPart objectType) {
		super(createID(id), name);
		this.objectType = objectType;
	}

	public AttributeVerb(AttributeVerb active) {
		super(active.id, active);
		this.objectType = null;
	}

	public String getLabel() {
		return name;
	}

	private static String createID(String name) {
		return new AddressImpl(Verb.VERB_COMPOUND_COMPONENT, ATTRIBUTE_VERB).extend(ATTRIBUTE_PART, name).toString();
	}


	public Address extend(Address in) {
		return in.extend(Verb.VERB_COMPOUND_COMPONENT, ATTRIBUTE_VERB).extend(ATTRIBUTE_PART, name);
	}

	@Override
	public NounPart getSubjectType() {
		return null;
	}

	private final NounPart objectType;
	
	
	@Override
	public NounPart getObjectType() {
		return objectType;
	}

}
