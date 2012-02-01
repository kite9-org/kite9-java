package org.kite9.diagram.builders.krmodel;

import org.kite9.framework.common.Kite9ProcessingException;

public class NounTools {

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
}
