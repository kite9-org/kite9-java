package org.kite9.diagram.builders;

import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.Verb;

/**
 * This class contains basic functionality for managing nouns that all builders will need.
 * 
 * @author robmoffat
 * 
 */
public abstract class AbstractBuilder {

	public AbstractBuilder() {
	}

	public abstract NounFactory getNounFactory();
	
	public NounPart createNoun(Object o) {
		return getNounFactory().createNoun(o);
	}

	protected abstract Proposition createProposition(NounPart subject, Verb v, NounPart object);

	/**
	 * Returns only elements matching both criteria
	 */
	public <X> Filter<X> and(final Filter<X> a, final Filter<X> b) {
		return new Filter<X>() {
			public boolean accept(X o) {
				return a.accept(o) && b.accept(o);
			}
		};
	}

	/**
	 * Returns the opposite filter to the one entered
	 */
	public <X> Filter<X> not(final Filter<X> only) {
		return new Filter<X>() {
			public boolean accept(X o) {
				return !only.accept(o);
			}
		};
	}

}