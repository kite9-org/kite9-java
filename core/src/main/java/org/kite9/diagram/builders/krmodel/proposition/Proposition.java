package org.kite9.diagram.builders.krmodel.proposition;

import java.util.List;

import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.NounPart;

/**
 * A proposition posits the existence of a subject, along with potentially a verb that 
 * that subject might be involved in.
 * 
 * @author robmoffat
 *
 */
public interface Proposition extends Knowledge {

	public NounPart getSubject();	
	
	public Knowledge getObject();

	public List<? extends SimpleRelationship> decompose();
}
