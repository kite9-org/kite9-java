/**
 * 
 */
package org.kite9.diagram.builders;

import org.kite9.framework.alias.AliasEnabled;

/**
 * Named Arrows are bound to their subjects, so a relationship and a subject form a binding which
 * is represented by a single arrow
 * 
 * @author robmoffat
 *
 */
public class NounRelationshipBinding implements AliasEnabled {

    public NounRelationshipBinding(Object subject, Relationship r) {
        super();
        this.cl = subject;
        this.r = r;
    }
    
    Object cl;
    
    Relationship r;
    
    
    public Object getSubject() {
        return cl;
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
	result = prime * result + ((cl == null) ? 0 : cl.hashCode());
	result = prime * result + ((r == null) ? 0 : r.hashCode());
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
	
	NounRelationshipBinding other = (NounRelationshipBinding) obj;
	if (cl == null) {
	    if (other.cl != null)
		return false;
	} else if (!cl.equals(other.cl))
	    return false;
	if (r == null) {
	    if (other.r != null)
		return false;
	} else if (!r.equals(other.r))
	    return false;
	return true;
    }
    
    
}