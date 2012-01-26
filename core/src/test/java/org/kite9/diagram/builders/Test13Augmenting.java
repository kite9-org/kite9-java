package org.kite9.diagram.builders;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.Direction;
import org.kite9.framework.Kite9Item;

public class Test13Augmenting extends AbstractBuilderTest {

    interface SomeInterface {}
    class SomeClass implements SomeInterface {}
    
    
    @Kite9Item
    @Test
    public void test_13_1_AugmentGlyph() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class).show(db.asGlyphs());
	Glyph g = (Glyph) db.getElement(SomeClass.class);
	g.getText().add(new TextLine("This is a favourite of John's"));
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_13_2_DirectedArrow() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withClasses(new Relationship("inherits", Direction.LEFT), SomeInterface.class)
		.show(db.asGlyphs());
	
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_13_3_AugmentLinks() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withInterfaces(null, false)
		.show(db.asGlyphs());
	
	Arrow a = (Arrow) db.getRelationshipElement(SomeClass.class, Relationship.IMPLEMENTS);
	Glyph sc = (Glyph) db.getElement(SomeClass.class);
	Glyph si = (Glyph) db.getElement(SomeInterface.class);
	
	Link top = (Link) a.getConnectionTo(si);
	Link bottom = (Link) a.getConnectionTo(sc);
	
	top.setDrawDirection(Direction.RIGHT);
	bottom.setDrawDirection(Direction.DOWN);
	
	top.setToLabel(new TextLine("some text here"));
	bottom.setFromLabel(new TextLine("some text there"));
	
	renderDiagram(db.getDiagram());
    }
    
    
    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(SomeClass.class));
	pmi.addClass(convertClassName(SomeInterface.class));
    }

}
