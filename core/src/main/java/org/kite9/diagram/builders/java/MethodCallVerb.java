package org.kite9.diagram.builders.java;

import java.lang.reflect.Method;

import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.diagram.position.Direction;

/**
 * Represents a relationship formed by a method call, where the method name is
 * the verb.
 * 
 * @author moffatr
 *
 */
public class MethodCallVerb extends AbstractVerb {

	public MethodCallVerb(Method m) {
		super(m.getName());
	}

	public MethodCallVerb(Method m, VerbType active) {
		super("called", new MethodCallVerb(m));
	}

	public MethodCallVerb(String name, Direction d) {
		super(name, d);
	}

	public MethodCallVerb(String name, AbstractVerb active) {
		super(name, active);
	}

	public MethodCallVerb(String name) {
		super(name);
	}

}
