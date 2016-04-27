package org.kite9.diagram.builders.krmodel.noun;

import java.util.List;

/**
 * SimpleNouns are the basic items of information on the diagram.  No ownership information is expressed in them,
 * therefore they have to have their own uniqueness.
 * 
 * @author robmoffat
 *
 */
public interface SimpleNoun extends NounPart {

	/**
	 * The basic name itself: e.g. . John, Cat, Spruce
	 */
    public String getLabel();
    
    /**
     * Any other necessary identification. e.g. "from Colchester", "with the missing tail", "in my garden",
     * so that it doesn't get confused with another same-named noun on the page.
     */
    public List<NounPart> getDisambiguation();
    
}