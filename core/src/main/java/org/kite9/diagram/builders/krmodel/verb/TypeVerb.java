package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.position.Direction;

/**
 * Marker class for Type, which is kind of generic and therefore should not be Java-specific.
 * @author robmoffat
 *
 */
public class TypeVerb extends SimpleVerb {

	public TypeVerb(String id, String name) {
		super(id, name);
	}

	public TypeVerb(String id, String label, Direction d) {
		super(id, label, d);
	}

	public TypeVerb(String label, Verb active) {
		super(label, active);
	}

}
