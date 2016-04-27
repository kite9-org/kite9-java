package org.kite9.diagram.builders.java.krmodel;

import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.AnnotatedNounPart;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.OwnedNoun;
import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;
import org.kite9.diagram.builders.krmodel.proposition.ComplexProposition;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;
import org.kite9.framework.common.Kite9ProcessingException;

public abstract class AbstractNounFactory implements NounFactory {

	/**
	 * Removes the annotation part from the noun.
	 */
	public static SimpleNoun getRawSimpleNoun(Knowledge in) {
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
	
	public NounPart extractObject(Proposition t) {
		if (t == null)
			return null;

		SimpleNoun originalSubject = getRawSimpleNoun(t.getSubject());
		
		Knowledge originalObject = t.getObject();
		if (originalObject instanceof NounPart) {
			return (NounPart) originalObject;
		}
			
		if (originalSubject != null) {
			return originalSubject;
		}
		
		
		throw new Kite9ProcessingException("Couldn't find object for proposition");
//		
//
//
//		if (t.getVerb() instanceof ComposesVerb) {
//			return new OwnedNounImpl(originalSubject, originalObject);
//		} else {
//			return originalObject;
//		}
	}
	
//	public abstract <X extends SimpleRelationship> List<X> createTies(Collection<X> old, Verb r);
	
}
