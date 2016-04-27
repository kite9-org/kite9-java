package org.kite9.diagram.builders.formats;

import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;
import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.diagram.builders.representation.ADLInsertionInterface;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Container;

/**
 * This handles the different types of proposition
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractFormat implements Format {

	public void write(Container c, Proposition p) {
		if (p instanceof SimpleRelationship) {
			write(p.getSubject(), ((SimpleRelationship) p).getVerb(), ((SimpleRelationship) p).getObject());
		}
	}

	protected abstract void write(NounPart subject, Verb verb, Knowledge object);

	public Connected returnElement(Container c, SimpleNoun representing) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
