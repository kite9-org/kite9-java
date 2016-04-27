package org.kite9.diagram.builders;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.framework.Kite9Item;

/**
 * Tests that the XML Knowledge representation works.
 * 
 * @author moffatr
 * 
 */
public class Test19ClassAsXML extends AbstractBuilderTest {

	static class DummyClass implements Serializable, Cloneable {
		private static final long serialVersionUID = -5513266470240682794L;
	}

	static class DummyClass2 extends DummyClass {
		private static final long serialVersionUID = 1L;

	}

	@Test
	@Kite9Item
	public void test_19_1_ClassName() throws IOException {
		XMLRepresentationBuilder db = createXMLBuilder();
		db.withClasses(Test19ClassAsXML.class).show(db.asXML());
		renderXML(db.getXML());
	}

	@Test
	@Kite9Item
	public void test_1_2_ClassExtendsAsSymbol() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Test19ClassAsXML.class).show(db.asConnectedGlyphs()).withSuperClasses(null).show(db.asSymbols());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_1_3_ClassVisibilityAsSymbol() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Test19ClassAsXML.class, DummyClass.class).show(db.asConnectedGlyphs()).showVisibility(db.asSymbols());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_1_4_ClassInterfacesAsArrows() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Test19ClassAsXML.class, DummyClass.class).show(db.asConnectedGlyphs()).withInterfaces(null, false).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_1_5_InnerClassAsGlyph() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Test19ClassAsXML.class).show(db.asConnectedGlyphs()).withInnerClasses(null, false).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_1_6_InnerClassAsGlyphsInContext() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Test19ClassAsXML.class).show(db.asConnectedContexts()).withInnerClasses(null, false).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_1_7_SubClassesAsExtends() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(DummyClass.class).show(db.asConnectedGlyphs()).withSubClasses(null, false).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Before
	public void setUpModel() {
		pmi.addClass(convertClassName(DummyClass.class));
		pmi.addClass(convertClassName(DummyClass2.class));
		pmi.addClass(convertClassName(AbstractBuilderTest.class));
		pmi.addClass(convertClassName(Test19ClassAsXML.class));

		pmi.addSubclass(convertClassName(AbstractBuilderTest.class), convertClassName(Test19ClassAsXML.class));
		pmi.addSubclass(convertClassName(DummyClass.class), convertClassName(DummyClass2.class));
	}
}
