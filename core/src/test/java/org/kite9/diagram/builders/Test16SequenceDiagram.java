package org.kite9.diagram.builders;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.builders.wizards.sequence.ClassBasedSequenceDiagramDataProvider;
import org.kite9.diagram.builders.wizards.sequence.ColumnSequenceDiagramWizard;
import org.kite9.diagram.builders.wizards.sequence.MethodBasedSequenceDiagramDataProvider;
import org.kite9.diagram.builders.wizards.sequence.NoLayoutSequenceDiagramWizard;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.model.FieldHandle;
import org.kite9.framework.model.MethodHandle;

public class Test16SequenceDiagram extends AbstractBuilderTest {

	public void someMethod1() {

		new Class1().startMethod();
		new Class3().methodA();

	}

	public class Class1 {

		int value;
		int set;

		public void startMethod() {
			value = new Class2().method3();
			set = 7;
			new Class2().method4();
		}

	}

	public class Class2 {

		public int method3() {
			new Class3().methodA();
			return 6;
		}

		public void method4() {
			new Class3().methodA();
		}

	}

	public class Class3 {

		public void methodA() {

		}

	}
	
	@Kite9Item
	@Test
	public void test_16_1_CreateSequenceDiagram() throws Exception {
		DiagramBuilder db = createBuilder();
		Method m = this.getClass().getDeclaredMethod("someMethod1");
		MethodBasedSequenceDiagramDataProvider mbsddp = new MethodBasedSequenceDiagramDataProvider(db, m, 
				null);
		ColumnSequenceDiagramWizard format = new ColumnSequenceDiagramWizard(db);
		format.write(mbsddp, db.getDiagram());
		renderDiagram(db.getDiagram());
	}
	
	@Kite9Item
	@Test
	public void test_16_2_CreateNestedSequenceDiagram() throws Exception {
		DiagramBuilder db = createBuilder();
		Context c = (Context) db.getInsertionInterface().returnContext(db.getDiagram(), db.getNounFactory().createNoun("Some context"), null, true, null);
		Method m = this.getClass().getDeclaredMethod("someMethod1");
		MethodBasedSequenceDiagramDataProvider mbsddp = new MethodBasedSequenceDiagramDataProvider(db, m, 
				null);
		ColumnSequenceDiagramWizard format = new ColumnSequenceDiagramWizard(db);
		format.write(mbsddp, c);
		renderDiagram(db.getDiagram());
	}
	
	@Kite9Item
	@Test
	public void test_16_3_CreateUnformattedSequenceDiagram() throws Exception {
		DiagramBuilder db = createBuilder();
		Context c = (Context) db.getInsertionInterface().returnContext(db.getDiagram(), db.getNounFactory().createNoun("Some context"), null, true, null);
		Method m = this.getClass().getDeclaredMethod("someMethod1");
		MethodBasedSequenceDiagramDataProvider mbsddp = new MethodBasedSequenceDiagramDataProvider(db, m, 
				null);
		NoLayoutSequenceDiagramWizard format = new NoLayoutSequenceDiagramWizard(db);
		format.write(mbsddp, c);
		renderDiagram(db.getDiagram());
	}
	
	@Kite9Item
	@Test
	public void test_16_4_CreateClassSequenceDiagram() throws Exception {
		DiagramBuilder db = createBuilder();
		Context c = (Context) db.getInsertionInterface().returnContext(db.getDiagram(), db.getNounFactory().createNoun("Some context"), null, true, null);
		Method m = this.getClass().getDeclaredMethod("someMethod1");
		ClassBasedSequenceDiagramDataProvider mbsddp = new ClassBasedSequenceDiagramDataProvider(db, m, 
				null);
		ColumnSequenceDiagramWizard format = new ColumnSequenceDiagramWizard(db);
		format.write(mbsddp, c);
		renderDiagram(db.getDiagram());
	}

	@Before
	public void setUpModel() throws Exception {
		pmi.addClass(convertClassName(Test16SequenceDiagram.class));
		pmi.addClass(convertClassName(Class1.class));
		pmi.addClass(convertClassName(Class2.class));
		pmi.addClass(convertClassName(Class3.class));
		MethodHandle someMethod1 = new MethodHandle(Test16SequenceDiagram.class.getDeclaredMethod("someMethod1"));
		MethodHandle startMethod = new MethodHandle(Class1.class.getDeclaredMethod("startMethod"));
		MethodHandle method3 = new MethodHandle(Class2.class.getDeclaredMethod("method3"));
		MethodHandle method4 = new MethodHandle(Class2.class.getDeclaredMethod("method4"));
		MethodHandle methodA = new MethodHandle(Class3.class.getDeclaredMethod("methodA"));
		FieldHandle value = new FieldHandle(Class1.class.getDeclaredField("value"));
		FieldHandle set = new FieldHandle(Class1.class.getDeclaredField("set"));
		pmi.addCalls(someMethod1, startMethod);
		pmi.addCalls(someMethod1, methodA);
		pmi.addCalls(startMethod, method3);
		pmi.addCalls(startMethod, value);
		pmi.addCalls(startMethod, set);
		
		pmi.addCalls(startMethod, method4);
		pmi.addCalls(method3, methodA);
		pmi.addCalls(method4, methodA);
	}	
}
