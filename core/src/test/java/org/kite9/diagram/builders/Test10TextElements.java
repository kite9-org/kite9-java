package org.kite9.diagram.builders;

import java.io.IOException;

import org.junit.Test;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.framework.Kite9Item;

public class Test10TextElements extends AbstractBuilderTest{

    class SomeClassA {}
    class SomeClassB {}
    
	@Test
	@Kite9Item
	public void test_10_1_StringGlyph() throws IOException {
	    DiagramBuilder db = createBuilder();
	    db.withObjects("a", "b", "c").show(db.asConnectedGlyphs());
	    renderDiagram(db.getDiagram());
	}
	
	@Test
	@Kite9Item
	public void test_10_1_StringContext() throws IOException {
	    DiagramBuilder db = createBuilder();
	    db.withObjects("m context")
	    	.show(db.asConnectedContexts())
	    	.withClasses(JavaRelationships.IS_CLASS, SomeClassA.class)
	    		.show(db.asConnectedGlyphs());
	    renderDiagram(db.getDiagram());
	}
	
	@Test
	@Kite9Item
	public void test_10_3_Note() throws IOException {
	    DiagramBuilder db = createBuilder();
	    db.withClasses(SomeClassA.class, SomeClassB.class)
	    	.show(db.asConnectedGlyphs())
	    	.withObjects(new AbstractVerb("observes"), "The 10 second rule")
	    	.show(db.asConnectedGlyphs("note"));
	    renderDiagram(db.getDiagram());
	}
}
