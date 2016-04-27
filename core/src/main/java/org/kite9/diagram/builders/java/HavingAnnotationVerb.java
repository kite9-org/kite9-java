package org.kite9.diagram.builders.java;

import java.lang.annotation.Annotation;

import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.diagram.position.Direction;

/**
 * Represents a relationship formed by a Annotation call, where the Annotation name is
 * the verb.
 * 
 * @author moffatr
 *
 */
public class HavingAnnotationVerb extends AbstractVerb {

	public HavingAnnotationVerb(Annotation m) {
		super(m.getName());
	}

	public HavingAnnotationVerb(Annotation m, VerbType active) {
		super("called", new HavingAnnotationVerb(m));
	}

	public HavingAnnotationVerb(String name, Direction d) {
		super(name, d);
	}

	public HavingAnnotationVerb(String name, AbstractVerb active) {
		super(name, active);
	}

	public HavingAnnotationVerb(String name) {
		super(name);
	}

}
