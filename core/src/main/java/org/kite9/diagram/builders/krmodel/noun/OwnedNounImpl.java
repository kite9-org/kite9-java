package org.kite9.diagram.builders.krmodel.noun;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.IdHelper;

public class OwnedNounImpl implements OwnedNoun, RepresentingNoun {

	private SimpleNoun owner;
	private SimpleNoun owned;

	public OwnedNounImpl(SimpleNoun owner, RepresentingNoun owned) {
		this.owner = owner;
		this.owned = owned;
	}

	public SimpleNoun getOwned() {
		return owned;
	}

	public SimpleNoun getOwner() {
		return owner;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owned == null) ? 0 : owned.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		OwnedNounImpl other = (OwnedNounImpl) obj;
		if (owned == null) {
			if (other.owned != null)
				return false;
		} else if (!owned.equals(other.owned))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	public Object getRepresented() {
		return owned.getRepresented();
	}

	public String toString() {
		return owner.toString() + "'s " + owned.toString();
	}

	public Address getID() {
		return owned.extend(owner.getID());
	}

	public Address extend(Address in) {
		return in.merge(getID());
	}
}
