package org.kite9.diagram.builders.krmodel;

import java.util.Collection;
import java.util.List;

import org.kite9.framework.alias.Aliaser;

/**
 * This class contains basic functionality for managing nouns and ties, that all
 * builders will need.
 * 
 * @author robmoffat
 * 
 */
public abstract class AbstractBuilder {

	protected Aliaser a;

	public AbstractBuilder(Aliaser a) {
		this.a = a;
	}

	public List<Tie> createTies(Collection<Tie> old, Relationship r,
			Object... items) {
		return NounFactory.createTies(old, r, getNounFactory(), items);
	}

	public NounPart createNoun(Object o) {
		return getNounFactory().createNoun(o);
	}



	protected abstract NounFactory getNounFactory();

	public Aliaser getAliaser() {
		return a;
	}
}