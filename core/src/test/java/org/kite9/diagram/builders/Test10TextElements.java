package org.kite9.diagram.builders;

import java.io.IOException;

import org.junit.Test;
import org.kite9.framework.Kite9Item;

public class Test10TextElements extends AbstractBuilderTest{

    class SomeClassA {}
    class SomeClassB {}
    
	@Test
	@Kite9Item
	public void test_10_1_StringGlyph() throws IOException {
	    DiagramBuilder db = createBuilder();
	    db.withStrings("a", "b", "c").show(db.asConnectedGlyphs());
	    renderDiagram(db.getDiagram());
	}
	
	@Test
	@Kite9Item
	public void test_10_1_StringContext() throws IOException {
	    DiagramBuilder db = createBuilder();
	    db.withStrings("m context")
	    	.show(db.asConnectedContexts())
	    	.withClasses(HasRelationship.CLASS, SomeClassA.class)
	    		.show(db.asConnectedGlyphs());
	    renderDiagram(db.getDiagram());
	}
	
	@Test
	@Kite9Item
	public void test_10_3_Note() throws IOException {
	    DiagramBuilder db = createBuilder();
	    db.withClasses(SomeClassA.class, SomeClassB.class)
	    	.show(db.asConnectedGlyphs())
	    	.withStrings(new Relationship("observes"), "The 10 second rule")
	    	.show(db.asConnectedGlyphs("note"));
	    renderDiagram(db.getDiagram());
	}
}
