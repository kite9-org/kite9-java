package org.kite9.diagram.builders.java;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public class ObjectBuilder extends AbstractElementBuilder<Object> {

	public ObjectBuilder(List<Tie> ties2, ProjectModel model, Aliaser a) {
		super(ties2, model, a);
	}

	@Override
	public ObjectBuilder reduce(Filter<? super Object> f) {
		return new ObjectBuilder(reduceInner(f), model, a);
	}

	public ObjectBuilder show(PropositionFormat f) {
		return (ObjectBuilder) super.show(f);
	}
	

	private void traverseFields(Tie t, Object o, List<Tie> ties, Class<?> c, Filter<? super Field> f) {
		if (c.getSuperclass() != null)
			traverseFields(t, o, ties, c.getSuperclass(), f);

		for (Field field : c.getDeclaredFields()) {
			AccessibleObjectValue value = null;
			try {
				field.setAccessible(true);
				Object o2 = field.get(o);
				value = new AccessibleObjectValue(field, o2, o);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			if ((value!=null) && ((f == null) || (f.accept(field)))) {
				ties.add(new Tie(NounFactory.createNewSubjectNounPart(t), JavaRelationships.FIELD,
						createNoun(value)));
			}
		}
	}
	
	private void traverseMethods(Tie t, Object o, List<Tie> ties, Class<?> c, Filter<? super Method> f) {
		if (c.getSuperclass() != null)
			traverseMethods(t, o, ties, c.getSuperclass(), f);

		for (Method method : c.getDeclaredMethods()) {
			AccessibleObjectValue value = null;
			try {
				method.setAccessible(true);
				Object o2 = null;
				if (f.accept(method)) {
					 o2 = method.invoke(o);
				}
				value = new AccessibleObjectValue(method, o2, o);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			if ((value!=null) && ((f == null) || (f.accept(method)))) {
				ties.add(new Tie(NounFactory.createNewSubjectNounPart(t), JavaRelationships.FIELD,
						createNoun(value)));
			}
		}
	}
	
	/**
	 * Returns objects which the builder object is composed with.
	 */
	public AccessibleValueBuilder withFieldValues(Filter<? super Field> f) {
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
			Object o = getRepresented(t);
			
			traverseFields(t, o, ties2, o.getClass(), f);
		}

		return new AccessibleValueBuilder(ties2, model, a);
	}

	public AccessibleValueBuilder withMethodReturnValues(Filter<? super Method> f) {
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
			Object o = getRepresented(t);
			
			traverseMethods(t, o, ties2, o.getClass(), f);
		}

		return new AccessibleValueBuilder(ties2, model, a);
	}
}
