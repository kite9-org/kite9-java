package org.kite9.tool.model;

import org.kite9.tool.model.SubClassing.SomeSubClass;



public class CallerCalling {

    public String methodA() {
	methodB();
	return null;
    }
    
    public String methodB() {
	sub = null;
	return null;
    }
    
    SomeSubClass sub;
}
