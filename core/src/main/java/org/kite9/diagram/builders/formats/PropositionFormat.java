package org.kite9.diagram.builders.formats;

import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Relationship;

/**
 * Formats are used to turn sentences into diagram objects.  For example, "Cathy goes shopping" could be
 * represented on in ADL as glyphs for Cathy and Shopping, and an Arrow for goes.  The format manages 
 * this conversion.
 * 
 * @author moffatr
 *
 */
public interface PropositionFormat {

    /**
     * Writes a sentence into the ADL diagram.
     * 
     * @param subject  The subject of a sentence.  e.g. "Cathy" in "Cathy goes shopping"
     * @param verb The vert of a sentence.  e.g. "goes" in "Cathy goes shopping"
     * @param object The object of a sentence.  e.g. "shopping" in "Cathy goes shopping"
     */
    public void write(NounPart subject, Relationship verb, NounPart object);

}
