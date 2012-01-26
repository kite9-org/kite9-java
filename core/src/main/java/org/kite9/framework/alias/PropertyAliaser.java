package org.kite9.framework.alias;

import java.util.Properties;

/**
 * Allows aliases to be provided in a property file.
 * 
 * @author moffatr
 *
 */
public class PropertyAliaser extends AbstractAliaser {
    
    Properties p = new Properties();
    

    public PropertyAliaser() {
    }

    public void setProperties(Properties p) {
        this.p = p;
    }

    @Override
    protected String getDefinedAlias(String fullName) {
	return p.getProperty(fullName);
    }
    
  
}
