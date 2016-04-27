package org.kite9.diagram.builders.krmodel.noun;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.IdHelper;

public class AnnotatedNounPartImpl implements AnnotatedNounPart {

	public AnnotatedNounPartImpl(NounPart underlying, String annotation) {
		super();
		this.underlying = underlying;
		this.annotation = annotation;
	}

	NounPart underlying;
	String annotation;

	public NounPart getNounPart() {
		return underlying;
	}

	public String getPrefixAnnotation() {
		return annotation;
	}

	public Object getRepresented() {
		return underlying.getRepresented();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annotation == null) ? 0 : annotation.hashCode());
		result = prime * result + ((underlying == null) ? 0 : underlying.hashCode());
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
		AnnotatedNounPartImpl other = (AnnotatedNounPartImpl) obj;
		if (annotation == null) {
			if (other.annotation != null)
				return false;
		} else if (!annotation.equals(other.annotation))
			return false;
		if (underlying == null) {
			if (other.underlying != null)
				return false;
		} else if (!underlying.equals(other.underlying))
			return false;
		return true;
	}

	public String toString() {
		return annotation + " " + underlying.toString();
	}

	public Address getID() {
		return underlying.getID().extend("annotation", annotation);
	}

	public Address extend(Address in) {
		return in.merge(getID());
	}
	
	public String getStringID() {
		return getID().toString();
	}

}
