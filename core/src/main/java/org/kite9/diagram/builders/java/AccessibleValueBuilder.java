package org.kite9.diagram.builders.java;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.kite9.diagram.builders.AbstractElementBuilder;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.ProjectModel;

/**
 * A builder for either a field or a method on a specific object instance.
 * 
 * @author robmoffat
 *
 */
public class AccessibleValueBuilder extends AbstractElementBuilder<AccessibleObjectValue> {

	public AccessibleValueBuilder(List<Proposition> ties, ProjectModel model, NounFactory a) {
		super(ties, model, a);
	}

	public AccessibleValueBuilder show(Format f) {
		return (AccessibleValueBuilder) super.show(f);
	}

	public TypeBuilder withType(Filter<? super Type> f) {
		return new TypeBuilder(
			new BasicObjectExtractor() {

				public List<?> createObjects(Object from) {
					AccessibleObject m = ((AccessibleObjectValue) from).getAccessibleObject();
					Type returnType = getGenericReturnType(m);
					return Arrays.asList(returnType);
				}

				public Verb getVerb(Object subject, Object object) {
					return JavaRelationships.HAS_TYPE;
				}

		}.generatePropositions(Type.class, f), model, nf);
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
		return new ObjectBuilder( 
			new BasicObjectExtractor() {
				
				public List<?> createObjects(Object from) {
					Object o = ((AccessibleObjectValue) from).getValue();
					return Arrays.asList(o);
				}

				public Verb getVerb(Object subject, Object object) {
					return JavaRelationships.REFERENCES;
				}
				
		}.generatePropositions(Object.class, f), model, nf);
	}

	@Override
	public AccessibleValueBuilder reduce(Filter<? super AccessibleObjectValue> f) {
		return new AccessibleValueBuilder(reduceInner(f), model, nf);
	}
}
