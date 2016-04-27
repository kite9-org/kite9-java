package org.kite9.tool.model;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;

@Kite9Item
public class Annotated {

    @Kite9Item
    public void someMethod() {
	
    }
    
    @K9OnDiagram(alias="bob")
    public String someField;
}
