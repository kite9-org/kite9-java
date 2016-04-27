package org.kite9.diagram.builders.representation;

import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
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
 */
public interface ADLInsertionInterface {
       
    public DiagramElement returnExisting(Knowledge representing);
    
    public void mapExisting(Knowledge representing, DiagramElement de);
       
    public DiagramElement returnConnectionBody(Container container,  Knowledge representing, String overrideLabel);
        
    public DiagramElement returnGlyph(Container container, Knowledge representing, String overrideLabel, String overrideStereotype);

    public DiagramElement returnContext(Container container, Knowledge representing, Label overrideLabel, boolean border, Layout l);
    
    public DiagramElement returnTextLine(Glyph container, Knowledge representing, String text);
    
    public DiagramElement extendTextLine(TextLine container, Knowledge representing, String text);
        
    public DiagramElement returnConnection(DiagramElement from, DiagramElement to, Knowledge representing, Label fromLabel, Label toLabel, boolean arrowHead, Direction d);
    
    public DiagramElement returnSymbol(DiagramElement container, Knowledge representing, String text, String preferredChars);
}
