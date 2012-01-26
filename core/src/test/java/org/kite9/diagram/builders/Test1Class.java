package org.kite9.diagram.builders;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.kite9.framework.Kite9Item;

/**
 * Tests for representing classes and their properties within the Kite9 Diagram.
 * 
 * @author moffatr
 * 
 */
public class Test1Class extends AbstractBuilderTest {

    static class DummyClass implements Serializable, Cloneable {
	private static final long serialVersionUID = -5513266470240682794L;
    }

    static class DummyClass2 extends DummyClass {
	private static final long serialVersionUID = 1L;

    }

    @Test
    @Kite9Item
    public void test_1_1_ClassName() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test1Class.class).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Test
    @Kite9Item
    public void test_1_2_ClassExtendsAsSymbol() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test1Class.class).show(db.asGlyphs()).withSuperClasses(null).show(db.asSymbols());
	renderDiagram(db.getDiagram());
    }

    @Test
    @Kite9Item
    public void test_1_3_ClassVisibilityAsSymbol() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test1Class.class, DummyClass.class).show(db.asGlyphs()).showVisibility(db.asSymbols());
	renderDiagram(db.getDiagram());
    }

    @Test
    @Kite9Item
    public void test_1_4_ClassInterfacesAsArrows() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test1Class.class, DummyClass.class).show(db.asGlyphs()).withInterfaces(null, false).show(
		db.asGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Test
    @Kite9Item
    public void test_1_5_InnerClassAsGlyph() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test1Class.class).show(db.asGlyphs()).withInnerClasses(null, false).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Test
    @Kite9Item
    public void test_1_6_InnerClassAsGlyphsInContext() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test1Class.class).show(db.asContexts()).withInnerClasses(null, false).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Test
    @Kite9Item
    public void test_1_7_SubClassesAsExtends() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(DummyClass.class).show(db.asGlyphs()).withSubClasses(null, false).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }

    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(DummyClass.class));
	pmi.addClass(convertClassName(DummyClass2.class));
	pmi.addClass(convertClassName(AbstractBuilderTest.class));
	pmi.addClass(convertClassName(Test1Class.class));

	pmi.addSubclass(convertClassName(AbstractBuilderTest.class), convertClassName(Test1Class.class));
	pmi.addSubclass(convertClassName(DummyClass.class), convertClassName(DummyClass2.class));
    }
}
