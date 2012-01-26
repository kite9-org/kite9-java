package org.kite9.framework.alias;


/**
 * Provides the label and stereotype aliases for various parts of the 
 * java project being reviewed.
 * 
 * @author moffatr
 *
 */
public interface Aliaser {

    public String getAlias(String fullName);
  
    public String getObjectAlias(Object represented);
    
    public String getObjectStereotype(Object represented);
}

