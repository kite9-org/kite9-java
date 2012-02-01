package org.kite9.diagram.builders.java;

import java.util.List;

import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.framework.alias.Aliaser;

/**
 * an object intance, e.g. the string "John", etc.
 * 
 * @author moffatr
 *
 */
public class ObjectInstanceSimpleNoun implements SimpleNoun {

	public ObjectInstanceSimpleNoun(Object represented, Aliaser a) {
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
	return getLabel()+" ("+getStereotype()+")";
    }

	public List<NounPart> getDisambiguation() {
		return disambiguation;
	}
}
