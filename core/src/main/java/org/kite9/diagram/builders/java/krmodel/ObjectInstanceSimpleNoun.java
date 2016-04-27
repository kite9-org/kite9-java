package org.kite9.diagram.builders.java.krmodel;

import java.util.List;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.IdHelper;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.RepresentingNoun;
import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;
import org.kite9.framework.alias.Aliaser;

/**
 * an object intance, e.g. the string "John", etc.
 * 
 * @author moffatr
 *
 */
public class ObjectInstanceSimpleNoun implements SimpleNoun, RepresentingNoun {

	public ObjectInstanceSimpleNoun(Object represented, String id, String label) {
		this(represented, a, null);
	}

	public ObjectInstanceSimpleNoun(Object represented, Aliaser a, List<NounPart> disambiguation) {
		super();
		this.represented = represented;
		this.a = a;
		this.disambiguation = disambiguation;
	}

	private Aliaser a;
	private Object represented;
	private List<NounPart> disambiguation;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((disambiguation == null) ? 0 : disambiguation.hashCode());
		result = prime * result + ((represented == null) ? 0 : represented.hashCode());
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
		ObjectInstanceSimpleNoun other = (ObjectInstanceSimpleNoun) obj;
		if (disambiguation == null) {
			if (other.disambiguation != null)
				return false;
		} else if (!disambiguation.equals(other.disambiguation))
			return false;
		if (represented == null) {
			if (other.represented != null)
				return false;
		} else if (!represented.equals(other.represented))
			return false;
		return true;
	}

	public Object getRepresented() {
		return represented;
	}

	public String getLabel() {
		return a.getObjectAlias(getRepresented());
	}

	public String getStereotype() {
		return a.getObjectStereotype(getRepresented());
	}

	public String toString() {
		return getLabel() + " (" + getStereotype() + ")";
	}

	public List<NounPart> getDisambiguation() {
		return disambiguation;
	}

	public Address getID(IdHelper helper) {
		Address compoundId = helper.getCompoundId(represented);
		if (disambiguation != null) {
			compoundId = compoundId.extend("disambiguation", ""+disambiguation.hashCode());
		}
		return compoundId;
	}

	public Address extend(Address in, IdHelper helper) {
		return in.merge(getID(helper));
	}
}
