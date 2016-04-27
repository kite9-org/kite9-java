package org.kite9.diagram.builders.java;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.ProjectModel;

public class MethodBuilder extends AnnotatedElementBuilder<Method> {

	public MethodBuilder(List<Proposition> ties, ProjectModel model, NounFactory a) {
		super(ties, model, a);
	}

	public MethodBuilder show(Format f) {
		return (MethodBuilder) super.show(f);
	}

	public TypeBuilder withReturns(Filter<? super Type> f) {
		return new TypeBuilder(
			new BasicObjectExtractor() {
				public List<?> createObjects(Object from) {
					return Arrays.asList(((Method) from).getGenericReturnType());
				}
	
				@Override
				public Verb getVerb(Object subject, Object object) {
					return JavaRelationships.RETURNS;
				}
			}.generatePropositions(Type.class, f), model, nf);
	}


	public TypeBuilder withParameters(Filter<? super Type> f) {
		return new TypeBuilder(new BasicObjectExtractor() {
			public List<?> createObjects(Object from) {
				return Arrays.asList(((Method)from).getGenericParameterTypes());
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.PARAMETER;
			}
			
		}.generatePropositions(Type.class, f), model, nf);
	}

	public MethodBuilder showVisibility(Format f) {
		for (Proposition t : ties) {
		    Method m = getRepresented(t);
		    NounPart sub = getNounFactory().createNewSubjectNounPart(t);
			if (Modifier.isPublic(m.getModifiers())) {
				show(f, new JavaPropositionBinding(sub, JavaRelationships.IS_VISIBILITY, createNoun(new JavaModifier("public"))));
			} else if (Modifier.isPrivate(m.getModifiers())) {
				show(f, new JavaPropositionBinding(sub, JavaRelationships.IS_VISIBILITY, createNoun(new JavaModifier("private"))));
			} else if (Modifier.isProtected(m.getModifiers())) {
				show(f, new JavaPropositionBinding(sub, JavaRelationships.IS_VISIBILITY, createNoun(new JavaModifier("protected"))));
			}
		}
		return this;
	}

	/**
	 * Returns the methods which call this one.
	 * 
	 * @param f
	 * @return
	 */
	public MethodBuilder withCallingMethods(Filter<? super Method> f) {
		final ClassLoader cl = getCurrentClassLoader();

		return new MethodBuilder(new BasicObjectExtractor() {
			public List<?> createObjects(Object from) {
				Set<MemberHandle> handles = model.getCalledBy(new MethodHandle((Method) from));
				return handlesToMethods(cl, handles);
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.METHOD_CALLED_BY_METHOD;
			}

		}.generatePropositions(Method.class, f), model, nf);
	}

	private List<Method> handlesToMethods(final ClassLoader cl, Collection<MemberHandle> handles) {
		List<Method> out = new ArrayList<Method>(handles.size());
		for (MemberHandle mh : handles) {
			if (mh instanceof MethodHandle) {
				out.add(((MethodHandle) mh).hydrate(cl));
			}
		}
		return out;
	}

	
	/**
	 * Returns classes of methods which call this one
	 */
	public ClassBuilder withCallingClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f) {
		final ClassLoader cl = getCurrentClassLoader();
		return new ClassBuilder(
			new BasicObjectExtractor() {

				public List<?> createObjects(Object from) {
					Set<MemberHandle> handles = model.getCalledBy(new MethodHandle((Method) from));
					return handlesToClasses(cl, handles);
				}

				public Verb getVerb(Object subject, Object object) {
					return JavaRelationships.METHOD_CALLED_BY_CLASS;
				}

		}.generatePropositions(Class.class, f), model,nf);
	}
	
	private List<Class<?>> handlesToClasses(final ClassLoader cl, Set<MemberHandle> handles) {
		List<Class<?>> out = new ArrayList<Class<?>>(handles.size());
		for (MemberHandle mh : handles) {
			if (mh instanceof MethodHandle) {
				out.add(((MethodHandle) mh).hydrate(cl).getDeclaringClass());
			}
		}
		return out;
	}


	/**
	 * Returns methods which are called by this one
	 */
	public MethodBuilder withCalledMethods(Filter<? super Method> f) {
		final ClassLoader cl = getCurrentClassLoader();
		return new MethodBuilder(new BasicObjectExtractor() {
			public List<?> createObjects(Object from) {
				List<MemberHandle> mh = model.getCalls(new MethodHandle(((Method)from)));
				return handlesToMethods(cl, mh);
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.METHOD_CALLS_METHOD;
			}
		}.generatePropositions(Method.class, f), model, nf);
	}
	
	/**
	 * Returns classes which are called by this one
	 */
	public ClassBuilder withCalledClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f) {
		final ClassLoader cl = getCurrentClassLoader();

		return new ClassBuilder(new BasicObjectExtractor() {
			
			@Override
			public Object getFilterObject(Object from) {
				return ((MethodHandle) from).hydrateClass(cl);
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				Method m = ((MethodHandle) object).hydrate(cl);
				return new MethodCallVerb(m);
			}
			
			@Override
			public List<?> createObjects(Object from) {
				Method m = (Method) from;
				List<Object> out = new ArrayList<Object>();
				for (MemberHandle mh : model.getCalls(new MethodHandle(m))) {
					out.add(mh);
				}
				return out;
			}
		}.generatePropositions(Class.class, f), model, nf);
	}

	/**
	 * Returns classes that declare methods
	 */
	public ClassBuilder withDeclaringClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f) {
		List<Proposition> ties2 = new ArrayList<Proposition>();
		for (Proposition t : ties) {
		    Method m = getRepresented(t);
			Class<?> c2 = m.getDeclaringClass();
			if ((f == null) || (f.accept(c2))) {
				ties2.add(new JavaPropositionBinding(getNounFactory().createNewSubjectNounPart(t), JavaRelationships.METHOD_OF, createNoun(c2)));
			}

		}

		return new ClassBuilder(new BasicObjectExtractor() {
			@SuppressWarnings("unchecked")
			public List<?> createObjects(Object from) {
				return Arrays.asList(((Method)from).getDeclaringClass());
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.METHOD_OF;
			}
		}.generatePropositions(Class.class, f), model, nf);
	}

	@Override
	public MethodBuilder reduce(Filter<? super Method> f) {
		return new MethodBuilder(reduceInner(f), model, nf);
	}
}
