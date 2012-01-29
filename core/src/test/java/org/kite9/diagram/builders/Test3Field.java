package org.kite9.diagram.builders;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;

public class Test3Field extends AbstractBuilderTest {

    @K9OnDiagram
    public Object field1;

    @K9OnDiagram(on = { Test3Field.class })
    protected List<Integer> field2;

    @Kite9Item
    @Test
    public void test_3_1_FieldAliasesAsGlyphsFromClass() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test3Field.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), false).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Kite9Item
    @Test
    public void test_3_2_FieldAliasesAsTextLine() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test3Field.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), false).show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }

    @Kite9Item
    @Test
    public void test_3_3_FieldsAsLabelledEdges() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test3Field.class).show(db.asConnectedGlyphs())
		.withFields(db.onlyAnnotated(), false)
			.withType(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }


    @Kite9Item
    @Test
    public void test_3_4_FieldAsGlyphWithType() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test3Field.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), false).show(db.asConnectedGlyphs())
		.withType(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Kite9Item
    @Test
    public void test_3_5_FieldsInContext() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test3Field.class).show(db.asConnectedContexts()).withFields(db.onlyAnnotated(), false).show(
		db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Kite9Item
    @Test
    public void test_3_6_FieldVisibility() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test3Field.class).show(db.asConnectedContexts()).withFields(db.onlyAnnotated(), false).show(db.asConnectedGlyphs())
		.showVisibility(db.asTextLines());
	renderDiagram(db.getDiagram());
    }

    @Kite9Item
    @Test
    public void test_3_7_FieldReturnTypesAsTextLine() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test3Field.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), false).show(db.asTextLines())
		.withType(null).show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }

    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(Test3Field.class));
    }
}
