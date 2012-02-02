package org.kite9.diagram.builders.java;

import java.lang.reflect.Field;
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
	

	private void traverse(Tie t, Object o, List<Tie> ties, Class<?> c, Filter<? super Object> f) {
		if (c.getSuperclass() != null)
			traverse(t, o, ties, c.getSuperclass(), f);

		for (Field field : c.getDeclaredFields()) {
			FieldValue value = null;
			try {
				field.setAccessible(true);
				Object o2 = field.get(o);
				value = new FieldValue(field, o2, o);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			if ((value!=null) && ((f == null) || (f.accept(value)))) {
				ties.add(new Tie(NounFactory.createNewSubjectNounPart(t), JavaRelationships.FIELD,
						createNoun(value)));
			}
		}
	}
	
	/**
	 * Returns objects which the builder object is composed with.
	 */
	public FieldValueBuilder withFieldValues(Filter<? super Object> f) {
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
			Object o = getRepresented(t);
			
			traverse(t, o, ties2, o.getClass(), f);
		}

		return new FieldValueBuilder(ties2, model, a);
	}
}
