package org.kite9.diagram.builders.java.krmodel;

import java.util.List;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.RepresentingNoun;
import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;

/**
 * a simple class, a method etc.
 * 
 * @author moffatr
 *
 */
public class ProjectStaticSimpleNoun implements SimpleNoun, RepresentingNoun {

	public ProjectStaticSimpleNoun(String id, Object represented, String label) {
		this(id, represented, label, null);
	}

	public ProjectStaticSimpleNoun(String id, Object represented, String label, List<NounPart> disambiguation) {
		super();
		this.represented = represented;
		this.label = label;
		this.id = id;
		this.disambiguation = disambiguation;
	}

	private final String id;
	private final String label;
	private Object represented;
	private List<NounPart> disambiguation;

	public Object getRepresented() {
		return represented;
	}

	public String getLabel() {
		return label;
	}

	public String toString() {
		return getLabel() + " (" + getID() + ")";
	}

	public List<NounPart> getDisambiguation() {
		return disambiguation;
	}

	public Address getID() {
		return new AddressImpl(id);
	}

	public Address extend(Address in) {
		return in.merge(getID());
	}
	
	public String getStringID() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProjectStaticSimpleNoun other = (ProjectStaticSimpleNoun) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
