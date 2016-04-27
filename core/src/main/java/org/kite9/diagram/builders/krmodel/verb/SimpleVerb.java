package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.position.Direction;

/**
 * Used for handling simple, untyped relationships between objects.
 * 
 * e.g. A eats B.  Where the types of A and B are not known from the relationship, 
 * and also can't be inferred.  e.g. inferring B is edible.
 * 
 * @author robmoffat
 *
 */
public class SimpleVerb extends AbstractVerb {

	public SimpleVerb(String id, String name) {
		super(id, name);
	}

	public SimpleVerb(String id, String label, Direction d) {
		super(id, label, d);
	}

	public SimpleVerb(String label, Verb active) {
		super(label, active);
	}

	public Address extend(Address in) {
		return in.extend(VERB_COMPOUND_COMPONENT, id);
	}

	public String getLabel() {
		return name;
	}

}
