package org.kite9.diagram.builders.java;

import java.lang.reflect.Field;

import org.kite9.framework.alias.AliasEnabled;

/**
 * Value object for storing the instance value of a field.
 */
class FieldValue implements AliasEnabled {
	
	private Field f;
	
	public Field getField() {
		return f;
	}

	public Object getValue() {
		return value;
	}

	public Object getFrom() {
		return from;
	}

	public FieldValue(Field f, Object value, Object from) {
		super();
		this.f = f;
		this.value = value;
		this.from = from;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		FieldValue other = (FieldValue) obj;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	private Object value;
	
	private Object from;

	public Object getObjectForAlias() {
		return f;
	}
	
}
