package org.kite9.diagram.builders;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.model.AnnotationHandle;
import org.kite9.framework.model.ClassHandle;

public class Test5Annotation extends AbstractBuilderTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface Reference {
	
	Class<?>[] refs() default {};
	
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface Useless {
	
    }
    
    
    @Useless
    @Reference(refs={SomeReferencedClassB.class, SomeReferencedClassC.class})
    static class SomeClassA {}
    
    static class SomeReferencedClassB {}
    static class SomeReferencedClassC {}

    @Reference()
    static class SomeClassD {}
    
    @Useless
    static class SomeClassE {}
	
    
    
    @Kite9Item
    @K9OnDiagram
    public void someTestMethod() {
    }
    
    
    
    @Kite9Item
    @Test
    public void test_5_1_AnnotationAsTextLine() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test5Annotation.class)
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asGlyphs())
			.withAnnotations(null)
				.show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_5_2_ReferencedByAnnotation() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeReferencedClassB.class, SomeReferencedClassC.class)
		.show(db.asGlyphs())
			.withReferencingAnnotations(null)
			.show(db.asGlyphs());
	
	renderDiagram(db.getDiagram());
    }
    
    
    @Kite9Item
    @Test
    public void test_5_3_AnnotationAsSubject() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Reference.class, Useless.class)
		.show(db.asGlyphs("annotation"))
		.withAnnotatedClasses(null)
			.show(db.asGlyphs());
	
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_5_4_ReferencedByAnnotationElement() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(SomeReferencedClassB.class, SomeReferencedClassC.class)
		.show(db.asGlyphs())
			.withReferencingAnnotatedElements(null)
			.show(db.asGlyphs());
	
	renderDiagram(db.getDiagram());
    }
    
    
    
    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(Test5Annotation.class));  
	pmi.addClass(convertClassName(SomeClassA.class));  
	pmi.addClass(convertClassName(SomeClassE.class));  
	pmi.addClass(convertClassName(SomeClassD.class));  
	pmi.addClass(convertClassName(Reference.class));  
	pmi.addClass(convertClassName(Useless.class));  
	pmi.addClass(convertClassName(SomeReferencedClassB.class));  
	pmi.addClass(convertClassName(SomeReferencedClassC.class));  
	pmi.addClassAnnotation(convertClassName(Reference.class), convertClassName(SomeClassA.class));
	pmi.addClassAnnotation(convertClassName(Reference.class), convertClassName(SomeClassD.class));
	pmi.addClassAnnotation(convertClassName(Useless.class), convertClassName(SomeClassA.class));
	pmi.addClassAnnotation(convertClassName(Useless.class), convertClassName(SomeClassE.class));
    
	Reference refAnn = SomeClassA.class.getAnnotation(Reference.class);
	pmi.addAnnotationReference(convertClassName(SomeReferencedClassB.class), new AnnotationHandle(refAnn, new ClassHandle(SomeClassA.class), "refs"));
	pmi.addAnnotationReference(convertClassName(SomeReferencedClassC.class), new AnnotationHandle(refAnn, new ClassHandle(SomeClassA.class), "refs"));
    }
}
