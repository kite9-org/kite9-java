package org.kite9.diagram.builders;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.alias.PropertyAliaser;
import org.kite9.framework.common.StackHelp;

public class Test9Aliases extends AbstractBuilderTest {

    @K9OnDiagram(alias="alias annotation")
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SomeAnnotation {
    }

    @K9OnDiagram(alias="aliased_some_class")
    @SomeAnnotation
    class SomeClass {

	@K9OnDiagram(alias="aliased some method")
	public void someMethod() {

	}

	@K9OnDiagram(alias="aliased Field")
	String field;
    }

    

    class SomeOtherClass {
	
	
	
    }
    
    
    @Test
    @Kite9Item
    public void test_9_1_GlyphAlias() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Test
    @Kite9Item
    public void test_9_2_ContextAlias() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class).show(db.asContexts());
	renderDiagram(db.getDiagram());
    }
    
    @Test
    @Kite9Item
    public void test_9_3_MethodTextLineAlias() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withMethods(db.onlyAnnotated(), false).show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }

    
    @Test
    @Kite9Item
    public void test_9_4_FieldTextLineAlias() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withFields(db.onlyAnnotated(), false).show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }
    
    @Test
    @Kite9Item
    public void test_9_5_AnnotationGlyphs() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withAnnotations(null).show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }
    
    @Test
    @Kite9Item
    public void test_9_6_AnnotationText() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withAnnotations(null).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Test
    @Kite9Item
    public void test_9_7_PropertyAlias() throws IOException {
	Method m = StackHelp.getKite9Item();
	Properties p = new Properties();
	p.setProperty(SomeOtherClass.class.getSimpleName(), "aliased some_other");
	PropertyAliaser a = new PropertyAliaser();
	a.setProperties(p);
	DiagramBuilder db = new DiagramBuilder(a, m, pmi);
	db.withClasses(SomeOtherClass.class).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(Test9Aliases.class));
	pmi.addClass(convertClassName(Test9Aliases.SomeClass.class));
	pmi.addClass(convertClassName(Test9Aliases.SomeOtherClass.class));
	pmi.addClass(convertClassName(Test9Aliases.SomeAnnotation.class));
    }
}
