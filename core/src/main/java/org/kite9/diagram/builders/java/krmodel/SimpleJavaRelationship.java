package org.kite9.diagram.builders.java.krmodel;

import java.util.Arrays;
import java.util.List;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;
import org.kite9.diagram.builders.krmodel.proposition.VerbObjectPair;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.diagram.builders.krmodel.verb.Verb.VerbType;


public final class SimpleJavaRelationship implements SimpleRelationship {
	
	private final NounPart subject;
	private final NounPart object;
	private final Verb verb;

	public SimpleJavaRelationship(NounPart subject, Verb verb, NounPart object) {
		if (verb.getType() == VerbType.PASSIVE) {
			NounPart temp = subject;
			subject = object;
			object = temp;
			verb = verb.getActiveRelationship();
		} 
		
		this.subject = subject;
		this.object = object;
		this.verb = verb;
	}

	public Address getID() {
		return JavaPropositionBinding.createRelationId(new AddressImpl(true), getSubject(), getObject(), getVerb());
	}

	public Address extend(Address in) {
		return in.merge(getID());
	}
	
	public String getStringID() {
		return getID().toString();
	}

	public NounPart getSubject() {
		return subject;
	}

	public Verb getVerb() {
		return verb;
	}

	public Knowledge getObject() {
		return object;
	}

	public List<? extends SimpleRelationship> decompose() {
		return Arrays.asList(this);
	}

	public String getLabel() {
		return getSubject()+" "+getVerb()+" "+getObject();
	}

	@Override
	public VerbObjectPair createVerbObjectPair() {
		return new VerbObjectPair(verb, object);
	}
}