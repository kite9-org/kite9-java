package org.kite9.tool.model.sub2;

import org.kite9.tool.model.Test1TestModelBuilder;

public class SubPackageDependency2 {

	@interface Reference {
		
		public Class<?>[] refs(); 
		
	    }
	
	@Reference(refs={Test1TestModelBuilder.class})
	public void someMethod() {
		
	}
}
