package org.kite9.diagram.builders;

import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;


/**
 * This interface is exposed by the diagram builder for formats to use.
 * Each method attempts to create a diagram element with the given parameters.
 * If an element already exists, then that is returned.
 * 
 * @author moffatr
 *
 */
public interface InsertionInterface {
       
    public DiagramElement returnExisting(NounPart np);
    
    public DiagramElement returnExisting(NounRelationshipBinding sr);
    
    public DiagramElement returnArrow(Container container, Relationship r, String overrideLabel);
    
    public DiagramElement returnArrow(Container container, NounRelationshipBinding sr, String overrideLabel);
        
    public DiagramElement returnArrow(Container container, NounPart referred, String overrideLabel);

    public DiagramElement returnGlyph(Container container, NounPart referred, String overrideLabel, String overrideStereotype);

    public DiagramElement returnContext(Container container, NounPart referred, Label overrideLabel, boolean border, Layout l);
    
    public TextLine returnTextLine(Glyph container, NounPart referred, String text);
    
    public Link returnLink(DiagramElement from, DiagramElement to, Label fromLabel, Label toLabel, boolean arrowHead, Direction d);

    public Container getContainerFor(Object existing, Relationship rel);
    
    public Symbol returnSymbol(NounRelationshipBinding sbr, String text, String preferredChars);
}
