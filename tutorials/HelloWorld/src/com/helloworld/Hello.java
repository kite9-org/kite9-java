package com.helloworld;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.framework.Kite9Item;

public class Hello {

	@Kite9Item
	public Diagram helloWorldClassDiagram(DiagramBuilder db) {
		db.withPackages(Hello.class) // picks up the package containing Hello.class
			.show(db.asContexts()) // shows the package as a context
			.withMemberClasses(null) // picks up the contents of the package
			.show(db.asGlyphs());

		return db.getDiagram();
	}

}
