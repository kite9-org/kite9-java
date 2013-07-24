package org.kite9.diagram.builders;

import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.model.MethodHandle;

/**
 * Tests for representing methods within the kite9 diagram.
 * @author moffatr
 *
 */
public class Test2Method extends AbstractBuilderTest {
    
    class DummyClass {
	
    }
    
    @K9OnDiagram
    public DummyClass someTestMethod(String[] someArg, Collection<Integer> someArg2) {
	return null;
    }
    
    @K9OnDiagram
    public DummyClass someOtherMethod(String[] x) {
	return null;
    }
    
    class CallerClass {
	
	@K9OnDiagram
	public void someMethod() {
	    someOtherMethod(new String[] { "hfh" } );
	}
	
    }
       

    @Kite9Item
    @Test
    public void test_2_1_MethodsAsGlyphs() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class).show(db.asConnectedGlyphs())
		.withMethods(db.onlyAnnotated(), false).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_2_2_MethodsAsTextLine() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class).show(db.asConnectedGlyphs())
		.withMethods(db.onlyAnnotated(), false).show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }
       
    @Kite9Item
    @Test
    public void test_2_3_MethodAsLabelWithReturn() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class)
		.show(db.asConnectedGlyphs())
		.withMethods(db.onlyAnnotated(), false)
			.withReturns(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_2_4_MethodsInContext() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class)
		.show(db.asConnectedContexts())
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asConnectedGlyphs("method"));
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_2_5_MethodParams() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class)
		.show(db.asConnectedGlyphs())
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asConnectedGlyphs())
			.withParameters(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_2_6_MethodVisibilityAndParams() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class)
		.show(db.asConnectedContexts())
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asConnectedGlyphs())
			.showVisibility(db.asTextLines())
			.withParameters(null).show(db.asTextLines());
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_2_7_MethodMethodCaller() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class).show(db.asConnectedGlyphs())
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asConnectedGlyphs())
			.withCallingMethods(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
	
    }
    
    @Kite9Item
    @Test
    public void test_2_8_MethodClassCaller() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(Test2Method.class).show(db.asConnectedGlyphs())
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asConnectedGlyphs())
			.withCallingClasses(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
	
    }
    
    @Kite9Item
    @Test
    public void test_2_10_MethodCalled() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(CallerClass.class)
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asConnectedGlyphs())
			.withCalledMethods(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
	
    }
    
    @Kite9Item
    @Test
    public void test_2_11_MethodDeclaring() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withClasses(CallerClass.class)
		.withMethods(db.onlyAnnotated(), false)
			.show(db.asConnectedGlyphs())
			.withCalledMethods(null).show(db.asConnectedGlyphs())
				.withDeclaringClasses(null).show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
	
    }
    
    
    
    @Before
    public void setUpModel() throws SecurityException, NoSuchMethodException {
	pmi.addClass(convertClassName(Test2Method.class));
	pmi.addClass(convertClassName(DummyClass.class));
	pmi.addClass(convertClassName(CallerClass.class));
	pmi.addCalls(new MethodHandle(CallerClass.class.getDeclaredMethod("someMethod")), new MethodHandle(Test2Method.class.getDeclaredMethod("someOtherMethod", String[].class)));
    }
}

