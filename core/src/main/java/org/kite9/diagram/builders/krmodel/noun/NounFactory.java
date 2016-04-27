package org.kite9.diagram.builders.krmodel.noun;

import org.kite9.diagram.builders.krmodel.proposition.Proposition;


public interface NounFactory {

	/**
	 * Creates a NounPart for some underlying object about which we want to store knowledge.
	 */
	public NounPart createNoun(Object o);

	/**
	 * Extracts the object of the proposition, for the purpose of using it in a new proposition.
	 */
	public NounPart extractObject(Proposition t);
	
	public NounPart createNewSubjectNounPart(Proposition t);
}
