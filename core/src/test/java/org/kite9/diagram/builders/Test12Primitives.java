package org.kite9.diagram.builders;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;

public class Test12Primitives extends AbstractBuilderTest {

    @K9OnDiagram
    int a;
    
    @K9OnDiagram
    float b;
    
    @K9OnDiagram
    long c;
    
    @K9OnDiagram
    char d;
    
    @Kite9Item
    @Test
    public void test_12_1_AsText() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test12Primitives.class)
		.show(db.asGlyphs())
		.withFields(db.onlyAnnotated(), false).show(db.asTextLines()).withType(null).show(db.asTextLines());

	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_12_2_AsGlyphs() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test12Primitives.class)
		.show(db.asGlyphs())
		.withFields(db.onlyAnnotated(), false).show(db.asGlyphs()).withType(null).show(db.asGlyphs());

	renderDiagram(db.getDiagram());
    }
    

    @Kite9Item
    @Test
    public void test_12_3_AsLabels() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test12Primitives.class)
		.show(db.asGlyphs())
		.withFields(db.onlyAnnotated(), false).withType(null).show(db.asGlyphs());

	renderDiagram(db.getDiagram());
    }
    
    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(Test12Primitives.class));
    }
}
