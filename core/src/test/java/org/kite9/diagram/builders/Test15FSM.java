package org.kite9.diagram.builders;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.builders.wizards.fsm.EnumWithAnnotationFSMDataProvider;
import org.kite9.diagram.builders.wizards.fsm.FiniteStateMachineWizard;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.model.FieldHandle;
import org.kite9.framework.model.MethodHandle;

public class Test15FSM extends AbstractBuilderTest {

	static enum State {
		ONE, TWO, THREE
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface BeforeState {

		public State[] value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface AfterState {

		public State[] value();

	}

	static class StateMovingClass {

		State current;

		@BeforeState( { State.ONE })
		@AfterState( { State.TWO })
		public void moveToTwo() {
			current = State.TWO;
		}

		@BeforeState( { State.ONE, State.TWO })
		@AfterState( { State.THREE })
		public void moveToThree() {
			current = State.THREE;
		}

		@BeforeState( { State.TWO, State.THREE })
		@AfterState( { State.ONE })
		public void reset() {
			current = State.ONE;
		}
	}

	@Test
	@Kite9Item
	public void test_15_1_CreateFSMModel() throws Exception {
		DiagramBuilder db = createBuilder();
		FiniteStateMachineWizard fsmf = new FiniteStateMachineWizard(db);
		Field f = StateMovingClass.class.getDeclaredField("current");
		EnumWithAnnotationFSMDataProvider provider 
			= new EnumWithAnnotationFSMDataProvider(db.getNounFactory(), db.getProjectModel(), f , State.class, State.class.getClassLoader(), BeforeState.class, "value", AfterState.class, "value");
		fsmf.write(provider, db.getDiagram());
		renderDiagram(db.getDiagram());
	}
	
	@Before
	public void setUpModel() throws Exception {
		pmi.addClass(convertClassName(State.class));
		pmi.addClass(convertClassName(StateMovingClass.class));
		pmi.addClass(convertClassName(BeforeState.class));
		pmi.addClass(convertClassName(AfterState.class));
		MethodHandle a = new MethodHandle(StateMovingClass.class.getDeclaredMethod("moveToTwo"));
		MethodHandle b = new MethodHandle(StateMovingClass.class.getDeclaredMethod("moveToThree"));
		MethodHandle c = new MethodHandle(StateMovingClass.class.getDeclaredMethod("reset"));
		FieldHandle f = new FieldHandle(StateMovingClass.class.getDeclaredField("current"));
		pmi.addCalls(a, f);
		pmi.addCalls(b, f);
		pmi.addCalls(c, f);
	}
}
