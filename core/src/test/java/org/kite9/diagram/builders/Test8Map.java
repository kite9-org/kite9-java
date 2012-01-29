package org.kite9.diagram.builders;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;

public class Test8Map extends AbstractBuilderTest {

	class A {
	}

	class B {
	}

	class C {
	}

	/**
	 * Concrete generic classes
	 */
	class Example1 {

		@K9OnDiagram
		Map<A, B> someMap = new HashMap<A, B>();

		@K9OnDiagram
		Map<A, C> someOtherMap = new HashMap<A, C>();
	}

	/**
	 * Generic array classes
	 */
	class Example2 {
		@K9OnDiagram
		Map<A, C[]> someOtherMap = new HashMap<A, C[]>();

	}

	/**
	 * Non-concrete type variables
	 */
	class Example3<W, V> {

		@SuppressWarnings("unused")
		@K9OnDiagram
		private Map<W, V> someMap = new HashMap<W, V>();

	}

	/**
	 * Concrete type variables
	 */
	class Example4 extends Example3<URL, URL> {

	}
	
	class Example5 {
		@K9OnDiagram
		@SuppressWarnings("rawtypes")
		Map a = new HashMap();
		
	}

	public static void main(String[] args) {
		Type t2 = Example4.class.getGenericSuperclass();
		ParameterizedType t = (ParameterizedType) Example4.class.getFields()[0].getGenericType();

		System.out.println(t);
		System.out.println(t2);
	}

	@Kite9Item
	@Test
	public void test_8_1_MapExample1() throws IOException {
		 DiagramBuilder db = createBuilder();
		 db.withClasses(Example1.class)
		 .show(db.asConnectedGlyphs())
		 .withFields(db.onlyAnnotated(), false)
		 .show(db.asConnectedGlyphs())
		 .withType(null)
		 .show(db.asConnectedGlyphs());
		 renderDiagram(db.getDiagram());
	}

	@Kite9Item
	@Test
	public void test_8_2_MapExample2() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Example2.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), false).show(db.asConnectedGlyphs())
				.withType(null).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_8_3_MapExample3() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Example3.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), false).show(db.asConnectedGlyphs())
				.withType(null).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}

	@Test
	@Kite9Item
	public void test_8_4_MapExample4() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Example4.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), true).show(db.asConnectedGlyphs())
				.withType(null).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}
	
	@Test
	@Kite9Item
	public void test_8_5_MapExample5() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Example5.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), true).show(db.asConnectedGlyphs())
				.withType(null).show(db.asConnectedGlyphs());
		renderDiagram(db.getDiagram());
	}
	
	@Test
	@Kite9Item
	public void test_8_6_MapExample4AsText() throws IOException {
		DiagramBuilder db = createBuilder();
		db.withClasses(Example4.class).show(db.asConnectedGlyphs()).withFields(db.onlyAnnotated(), true).show(db.asTextLines())
				.withType(null).show(db.asTextLines());
		renderDiagram(db.getDiagram());
	}
	

	@Before
	public void setUpModel() {
		pmi.addClass(convertClassName(Test8Map.class));
		pmi.addClass(convertClassName(A.class));
		pmi.addClass(convertClassName(B.class));
		pmi.addClass(convertClassName(C.class));
		pmi.addClass(convertClassName(Example1.class));
		pmi.addClass(convertClassName(Example2.class));
		pmi.addClass(convertClassName(Example3.class));
		pmi.addClass(convertClassName(Example4.class));
		pmi.addClass(convertClassName(Example5.class));
	}

}
