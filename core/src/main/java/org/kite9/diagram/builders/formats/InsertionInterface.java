package org.kite9.diagram.builders.formats;

import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;


/**
 * This interface is exposed by the diagram builder for formats to use.
 * 
 * It exposes the different ways in which knowledge parts can be converted into diagram elements.
 * 
 * In each case, there is an object being represented.  If this is left as null, there is no object being represented, so return a 
 * new diagram element.  If there is an object being represented, don't create a new one if it's already there on the diagram - return
 * that existing element no matter what it's type.
 * 
 * @author moffatr
 *
 */
public interface InsertionInterface {
       
    public DiagramElement returnExisting(Object representing);
    
    public void mapExisting(Object representing, DiagramElement de);
       
    public DiagramElement returnConnectionBody(Container container,  Object representing, String overrideLabel);
        
    public DiagramElement returnGlyph(Container container, Object representing, String overrideLabel, String overrideStereotype);

    public DiagramElement returnContext(Container container, Object representing, Label overrideLabel, boolean border, Layout l);
    
    public DiagramElement returnTextLine(Glyph container, Object representing, String text);
    
    public DiagramElement extendTextLine(TextLine container, Object representing, String text);
        
    public DiagramElement returnConnection(DiagramElement from, DiagramElement to, Object representing, Label fromLabel, Label toLabel, boolean arrowHead, Direction d);
    
    public DiagramElement returnSymbol(DiagramElement container, Object representing, String text, String preferredChars);
}
