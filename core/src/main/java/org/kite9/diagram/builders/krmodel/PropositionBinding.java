/**
 * 
 */
package org.kite9.diagram.builders.krmodel;

import org.kite9.framework.alias.AliasEnabled;

/**
 * Connections are often bound to a whole propostion. e.g. John Eats Cake is a
 * proposition. The connection between John and Cake (with label "eats")
 * represents this whole proposition.
 * 
 * @author robmoffat
 * 
 */
public class PropositionBinding implements AliasEnabled {

	public PropositionBinding(Object subject, Relationship r, Object object) {
		super();
		this.s = subject;
		this.r = r;
		this.o = object;
	}

	Object s, o;

	Relationship r;

	public Object getSubject() {
		return s;
	}

	public Object getObject() {
		return o;
	}

	public Object getObjectForAlias() {
		return r;
	}

	public Relationship getRelationship() {
		return r;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((o == null) ? 0 : o.hashCode());
		result = prime * result + ((r == null) ? 0 : r.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
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
		PropositionBinding other = (PropositionBinding) obj;
		if (o == null) {
			if (other.o != null)
				return false;
		} else if (!o.equals(other.o))
			return false;
		if (r == null) {
			if (other.r != null)
				return false;
		} else if (!r.equals(other.r))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

}