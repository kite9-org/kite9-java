package org.kite9.diagram.builders;

import java.lang.reflect.Method;

import org.kite9.diagram.position.Direction;

/**
 * Represents a relationship formed by a method call, where the method name is the verb.
 * 
 * @author moffatr
 *
 */
public class MethodCallRelationship extends Relationship {
    
    public MethodCallRelationship(Method m) {
	super(m.getName());
    }

    public MethodCallRelationship(Method m, RelationshipType active) {
	super("called", new MethodCallRelationship(m));
    }

    
    public MethodCallRelationship(String name, Direction d) {
	super(name, d);
    }

    public MethodCallRelationship(String name, Relationship active) {
	super(name, active);
    }

    public MethodCallRelationship(String name) {
	super(name);
    }

}
