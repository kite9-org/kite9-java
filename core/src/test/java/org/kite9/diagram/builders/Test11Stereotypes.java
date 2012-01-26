package org.kite9.diagram.builders;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;

public class Test11Stereotypes extends AbstractBuilderTest {

    @K9OnDiagram(stereotype="alias annotation")
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SomeAnnotation {
    }

    @K9OnDiagram(stereotype="value object")
    class SomeClass {

	@K9OnDiagram(stereotype="guarded method")
	public void someMethod() {

	}

	@K9OnDiagram(stereotype="transient field")
	String field;
    }

    
    @K9OnDiagram(stereotype="bob")
    interface Bob {
	
    }
    
    class Intermediate implements Bob {
	
    }
    

    class SomeOtherClass extends Intermediate {
	
	
	
    }
    
    
    @Test
    @Kite9Item
    public void test_11_1_GlyphStereotype() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }
 
    @Test
    @Kite9Item
    public void test_11_2_MethodStereotype() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withMethods(db.onlyAnnotated(), false).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }

    
    @Test
    @Kite9Item
    public void test_11_3_FieldStereotype() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeClass.class)
		.show(db.asGlyphs())
		.withFields(db.onlyAnnotated(), false).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Test
    @Kite9Item
    public void test_11_4_GlyphInheritedStereotype() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeOtherClass.class).show(db.asGlyphs());
	renderDiagram(db.getDiagram());
    }
 
    
    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(Test11Stereotypes.class));
	pmi.addClass(convertClassName(Test11Stereotypes.SomeClass.class));
	pmi.addClass(convertClassName(Test11Stereotypes.SomeOtherClass.class));
	pmi.addClass(convertClassName(Test11Stereotypes.SomeAnnotation.class));
    }
}
