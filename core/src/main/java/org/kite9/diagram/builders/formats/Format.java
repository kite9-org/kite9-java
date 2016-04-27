package org.kite9.diagram.builders.formats;

import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;

/**
 * Formats are used to turn knowledge into a representation.  For example, "Cathy goes shopping" could be
 * represented on in ADL as glyphs for Cathy and Shopping, and an Arrow for goes.  Alternatively, 
 * a single glyph for cathy, with a symbol for "goes shopping".   Alternatively, the format could 
 * just represent as text.
 * 
 * The format manages the choice of this conversion.  
 * 
 * Not all formats are suitable for all Propositions.  An error will be returned if something invalid
 * is attempted.
 * 
 * @author moffatr
 *
 */
public interface Format {

    public void write(SimpleRelationship p);

}
