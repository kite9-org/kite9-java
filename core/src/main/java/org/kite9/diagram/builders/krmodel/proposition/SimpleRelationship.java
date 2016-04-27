/**
 * 
 */
package org.kite9.diagram.builders.krmodel.proposition;

import org.kite9.diagram.builders.krmodel.verb.Verb;

/**
 * Used to hold a piece of relationship information.  E.g. John eats fish.  Where John is 
 * the subject, eats is the relationship and fish is the object.  i.e. Simple Subject-Verb-Object 
 * propositions.
 * 
 * @author robmoffat
 *
 */
public interface SimpleRelationship extends Proposition {
	
	public Verb getVerb();

	public VerbObjectPair createVerbObjectPair();
}