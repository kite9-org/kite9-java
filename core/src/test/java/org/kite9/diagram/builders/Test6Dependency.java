package org.kite9.diagram.builders;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.model.MethodHandle;

public class Test6Dependency extends AbstractBuilderTest {

	static class A {

		@K9OnDiagram
		public void someMethod(B someB) {
			someB.execute();
		}
	}

	interface B {
		@K9OnDiagram
		public void execute();
	}

	static class C implements B {
		public void execute() {
		}
	}

	@Test
	@Kite9Item
	public void test_6_1_ClassCalledClassRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(A.class).show(db.asConnectedGlyphs()).withCalledClasses(null, true).show(db.asConnectedGlyphs());

		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_6_2_MethodCalledClassRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(A.class).withMethods(db.onlyAnnotated(), true).show(db.asConnectedGlyphs()).withCalledClasses(null).show(
				db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_6_3_ClassCallerClassRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(B.class).show(db.asConnectedGlyphs()).withCallingClasses(null, true).show(db.asConnectedGlyphs());

		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_6_4_MethodCallerClassRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(B.class).withMethods(db.onlyAnnotated(), true).show(db.asConnectedGlyphs()).withCallingClasses(null)
				.show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_6_5_ClassCalledMethodRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(A.class).show(db.asConnectedGlyphs()).withCalledMethods(null, true).show(db.asConnectedGlyphs());

		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_6_6_ClassCallerMethodRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(B.class).show(db.asConnectedGlyphs()).withCallingMethods(null, true).show(db.asConnectedGlyphs());

		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_6_7_MethodCalledMethodRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(A.class).withMethods(db.onlyAnnotated(), true).show(db.asConnectedGlyphs()).withCalledMethods(null).show(
				db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_6_8_MethodCallerMethodRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(B.class).withMethods(db.onlyAnnotated(), true).show(db.asConnectedGlyphs()).withCallingMethods(null)
				.show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Before
	public void setUpModel() throws SecurityException, NoSuchMethodException {
		pmi.addClass(convertClassName(Test6Dependency.class));
		pmi.addClass(convertClassName(A.class));
		pmi.addClass(convertClassName(B.class));
		pmi.addClass(convertClassName(C.class));
		pmi.addCalls(new MethodHandle(A.class.getDeclaredMethod("someMethod", new Class[] { B.class })),
				new MethodHandle(B.class.getDeclaredMethod("execute")));
		pmi.addSubclass(convertClassName(B.class), convertClassName(C.class));
		pmi.addClassDependency(convertClassName(C.class), convertClassName(B.class));
		pmi.addClassDependency(convertClassName(A.class), convertClassName(B.class));
	}

	@Test
	@Kite9Item
	public void test_6_9_ClassDependencyRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(C.class, A.class).show(db.asConnectedGlyphs()).withDependencies(null, true).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}
	
	@Test
	@Kite9Item
	public void test_6_10_ClassDependantsRelationship() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(B.class).show(db.asConnectedGlyphs()).withDependants(null, true).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

}
