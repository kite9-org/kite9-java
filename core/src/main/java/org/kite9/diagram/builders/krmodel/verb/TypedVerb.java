package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.krmodel.noun.NounPart;

/**
 * Use this where the types of the subject and object involved are known.
 * 
 * @author robmoffat
 *
 */
public interface TypedVerb extends Verb {

	public NounPart getSubjectType();
	
	public NounPart getObjectType();
	
	
}
