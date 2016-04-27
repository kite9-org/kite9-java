package org.kite9.tool.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Refs {

    @Retention(RetentionPolicy.RUNTIME)
    @interface Reference {
	
	public Class<?>[] refs(); 
	
    }
    
    @Reference(refs={Referenced.class})
    public void referencer() {
	
	
	
	
    }
    
    public class Referenced {
	
	
	
    }
}
