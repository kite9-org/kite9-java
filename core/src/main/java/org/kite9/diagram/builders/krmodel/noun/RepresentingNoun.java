package org.kite9.diagram.builders.krmodel.noun;

/**
 * Used for when the Noun represents some java object.
 * 
 * @author robmoffat
 *
 */
public interface RepresentingNoun extends NounPart {

	public Object getRepresented();
	
}
