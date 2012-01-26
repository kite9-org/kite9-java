package org.kite9.diagram.builders;

/**
 * Allows you to filter methods, fields, classes, etc. 
 * 
 * @author moffatr
 *
 */
public interface Filter<X> {

    public boolean accept(X o);
    
}
