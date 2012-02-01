/**
 * 
 */
package org.kite9.diagram.builders;

import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Relationship;
import org.kite9.framework.alias.AliasEnabled;

/**
 * A Tie is used to hold a piece of relationship information.  E.g. John eats fish.  Where John is 
 * the subject, eats is the relationship and fish is the object.
 * 
 * @author robmoffat
 *
 */
public class Tie implements AliasEnabled {

    public Tie(NounPart subject, Relationship r, NounPart object) {
        super();
        this.cl = subject;
        this.name = object;
        this.r = r;
    }
    NounPart cl;
    NounPart name;
    Relationship r;
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((cl == null) ? 0 : cl.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	Tie other = (Tie) obj;
	if (cl == null) {
	    if (other.cl != null)
		return false;
	} else if (!cl.equals(other.cl))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }
    public NounPart getSubject() {
        return cl;
    }
    public NounPart getObject() {
        return name;
    }
    public Object getObjectForAlias() {
	return name;
    }
    public Relationship getRelationship() {
	return r;
    }
    
    public String toString() {
	return "[TIE: "+cl+"/"+r+"/"+name+"]";
    }
    
}