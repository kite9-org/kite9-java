package com.helloworld;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.framework.Kite9Item;

public class Hello {

    
    @Kite9Item
    public Diagram helloWorldClassDiagram(DiagramBuilder db) {
	db.withClasses(Hello.class, World.class).show(db.asConnectedGlyphs());
	return db.getDiagram();
    }
}
