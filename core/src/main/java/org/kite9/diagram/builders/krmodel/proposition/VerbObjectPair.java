package org.kite9.diagram.builders.krmodel.proposition;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.verb.Verb;

public class VerbObjectPair implements Knowledge {

	private final NounPart object;

	private final Verb verb;

	public VerbObjectPair(Verb verb, NounPart object) {
		super();
		this.object = object;
		this.verb = verb;
	}
	
	public NounPart getObject() {
		return object;
	}

	public Verb getVerb() {
		return verb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((verb == null) ? 0 : verb.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerbObjectPair other = (VerbObjectPair) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (verb == null) {
			if (other.verb != null)
				return false;
		} else if (!verb.equals(other.verb))
			return false;
		return true;
	}

	@Override
	public Address getID() {
		return JavaPropositionBinding.createRelationId(new AddressImpl(true), null, object, verb);
	}

	@Override
	public String getStringID() {
		return getID().toString();
	}

	@Override
	public Address extend(Address in) {
		return in.merge(getID());
	}

	@Override
	public String getLabel() {
		return verb.getLabel()+": "+object.getLabel();
	}

}
