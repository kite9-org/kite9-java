package org.kite9.diagram.builders.java;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.ProjectModel;

/**
 * A builder for either a field or a method on a specific object instance.
 * 
 * @author robmoffat
 *
 */
public class AccessibleValueBuilder extends AbstractElementBuilder<AccessibleObjectValue> {

	public AccessibleValueBuilder(List<Tie> ties, ProjectModel model, Aliaser a) {
		super(ties, model, a);
	}

	public AccessibleValueBuilder show(PropositionFormat f) {
		return (AccessibleValueBuilder) super.show(f);
	}

	public TypeBuilder withType(Filter<? super AccessibleObject> f) {
		List<Tie> ties2 = new ArrayList<Tie>();

		for (Tie t : ties) {
			AccessibleObjectValue fv = getRepresented(t);
			AccessibleObject m = fv.getAccessibleObject();
			Type returnType = getGenericReturnType(m);
			if (f == null || f.accept(m)) {
				ties2.add(new Tie(NounFactory.createNewSubjectNounPart(t),
						JavaRelationships.HAS_TYPE, createNoun(returnType)));
			}
		}
		return new TypeBuilder(ties2, model, a);
	}

	private Type getGenericReturnType(AccessibleObject m) {
		if (m instanceof Field) {
			return (((Field)m).getGenericType());
		} else if (m instanceof Method) {
			return (((Method)m).getGenericReturnType());
		} else {
			throw new Kite9ProcessingException("AccessibleValue return type only works with field or method: "+m);
		}
	}

	public ObjectBuilder withValues(Filter<? super Object> f) {
		List<Tie> ties2 = new ArrayList<Tie>();

		for (Tie t : ties) {
			AccessibleObjectValue fv = getRepresented(t);
			Object o = fv.getValue();
				Set<NounPart> nouns = new ObjectNounHelper().generateNouns(o, getAliaser());
				for (NounPart nounPart : nouns) {
					if ((f == null || f.accept(nounPart.getRepresented()))) {
					ties2.add(new Tie(NounFactory.createNewSubjectNounPart(t),
							JavaRelationships.REFERENCES, nounPart));
					}
			}	
		}
		return new ObjectBuilder(ties2, model, a);
	}

	@Override
	public AccessibleValueBuilder reduce(Filter<? super AccessibleObjectValue> f) {
		return new AccessibleValueBuilder(reduceInner(f), model, a);
	}
}
