package org.kite9.diagram.builders.krmodel.noun;


/**
 * e.g. a "smelly" cat, "my" Audi, "a collection of" List(s).
 * 
 * Where the annotation is the part in quotes
 * 
 * @author moffatr
 *
 */
public interface AnnotatedNounPart extends NounPart {

    public String getPrefixAnnotation();
    
    public NounPart getNounPart();
    
}
