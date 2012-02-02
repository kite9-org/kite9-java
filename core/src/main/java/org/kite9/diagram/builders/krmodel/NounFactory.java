package org.kite9.diagram.builders.krmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kite9.framework.common.Kite9ProcessingException;

public abstract class NounFactory {

	public abstract NounPart createNoun(Object o);
	

	/**
	 * Removes the annotation part from the noun.
	 */
	public static SimpleNoun getRawSimpleNoun(NounPart in) {
		if (in == null)
			return null;

		if (in instanceof SimpleNoun) {
			return (SimpleNoun) in;
		}

		if (in instanceof OwnedNoun) {
			return getRawSimpleNoun(((OwnedNoun) in).getOwned());
		}

		if (in instanceof AnnotatedNounPart) {
			return getRawSimpleNoun(((AnnotatedNounPart) in)
					.getNounPart());
		}

		throw new Kite9ProcessingException("Can't process this noun" + in);

	}
	
	public static NounPart createNewSubjectNounPart(Tie t) {
		if (t == null)
			return null;

		SimpleNoun originalSubject = getRawSimpleNoun(t.getSubject());
		SimpleNoun originalObject = getRawSimpleNoun(t.getObject());
		if (originalSubject == null) {
			return originalObject;
		}

		if (t.getRelationship() instanceof HasRelationship) {
			return new OwnedNounImpl(originalSubject, originalObject);
		} else {
			return originalObject;
		}
	}
	
	public static List<Tie> createTies(Collection<Tie> old, Relationship r, NounFactory nf,
			Object... items) {
		List<Tie> ties = new ArrayList<Tie>(items.length * old.size());
		for (Tie item : old) {
			for (int i = 0; i < items.length; i++) {
				ties.add(new Tie(createNewSubjectNounPart(item), r,
						nf.createNoun(items[i])));
			}
		}

		return ties;
	}
	
}
