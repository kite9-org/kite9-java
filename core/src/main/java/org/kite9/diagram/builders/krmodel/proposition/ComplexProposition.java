package org.kite9.diagram.builders.krmodel.proposition;

import java.util.List;


/**
 * A complex proposition can be decomposed into a number of {@link SimpleRelationship}s.
 * 
 * Even for something as simple as "A <has method> B", we can decompose into:
 * A <is a> Class.
 * B <is a> Method.
 * A <composes> B.
 */
public interface ComplexProposition extends Proposition {

	public List<? extends SimpleRelationship> decompose();
}
