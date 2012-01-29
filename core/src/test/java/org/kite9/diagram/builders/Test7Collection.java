package org.kite9.diagram.builders;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;

public class Test7Collection extends AbstractBuilderTest {

    @K9OnDiagram
    public String[] stringArray;
    
    @K9OnDiagram
    public List<String> stringList;
    
    @K9OnDiagram
    public Set<String> stringSet;
    
    @K9OnDiagram
    public Collection<String> stringCollection;
    
    @K9OnDiagram
    public Set<Set<String>[]> compoundString1;
    
    @K9OnDiagram
    public Set<Set<String>>[] compoundString2;
    
    @K9OnDiagram
    public Iterable<String> someStrings;

    @Kite9Item
    @Test
    public void test_7_1_AsText() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test7Collection.class)
		.show(db.asConnectedGlyphs())
		.withFields(db.onlyAnnotated(), false).show(db.asTextLines()).withType(null).show(db.asTextLines());

	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_7_2_AsGlyphs() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test7Collection.class)
		.show(db.asConnectedGlyphs())
		.withFields(db.onlyAnnotated(), false).show(db.asConnectedGlyphs()).withType(null).show(db.asConnectedGlyphs());

	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_7_3_AsLabels() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test7Collection.class)
		.show(db.asConnectedGlyphs())
		.withFields(db.onlyAnnotated(), false).withType(null).show(db.asConnectedGlyphs());

	renderDiagram(db.getDiagram());
    }
    
    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(Test7Collection.class));
    }
}
