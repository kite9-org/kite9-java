package org.kite9.diagram.builders.noun;

import java.util.List;

public interface SimpleNoun extends NounPart {

	/**
	 * The basic name itself: e.g. . John, Cat, Spruce
	 */
    public String getLabel();
    
    /**
     * Some type information: e.g. Person, Animal, Tree
     */
    public String getStereotype();
    
    /**
     * Any other necessary identification. e.g. "from Colchester", "with the missing tail", "in my garden",
     * so that it doesn't get confused with another same-named noun on the page.
     */
    public List<NounPart> getDisambiguation();
    
}