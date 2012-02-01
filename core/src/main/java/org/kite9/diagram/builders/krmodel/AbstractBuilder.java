package org.kite9.diagram.builders.krmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kite9.diagram.builders.Tie;
import org.kite9.diagram.builders.java.BasicNounFactory;
import org.kite9.framework.alias.Aliaser;

/**
 * This class contains basic functionality for managing nouns and ties, that all
 * builders will need.
 * 
 * @author robmoffat
 * 
 */
public class AbstractBuilder {

	protected Aliaser a;
	protected NounFactory nf;

	public AbstractBuilder(Aliaser a) {
		this.a = a;
	}

	protected List<Tie> createTies(Collection<Tie> old, Relationship r,
			Object... items) {
		List<Tie> ties = new ArrayList<Tie>(items.length * old.size());
		for (Tie item : old) {
			for (int i = 0; i < items.length; i++) {
				ties.add(new Tie(createNewSubjectNounPart(item), r,
						createNoun(items[i])));
			}
		}

		return ties;
	}

	public NounPart createNoun(Object o) {
		return getNounFactory().createNoun(o);
	}

	protected NounPart createNewSubjectNounPart(Tie t) {
		if (t == null)
			return null;

		SimpleNoun originalSubject = NounTools.getRawSimpleNoun(t.getSubject());
		SimpleNoun originalObject = NounTools.getRawSimpleNoun(t.getObject());
		if (originalSubject == null) {
			return originalObject;
		}

		if (t.getRelationship() instanceof HasRelationship) {
			return new OwnedNounImpl(originalSubject, originalObject);
		} else {
			return originalObject;
		}
	}

	public NounFactory getNounFactory() {
		if (nf == null) {
			nf = new BasicNounFactory(a);
		}

		return nf;
	}

	public Aliaser getAliaser() {
		return a;
	}
}