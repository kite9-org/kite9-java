package org.kite9.diagram.builders;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.ProjectModel;

public class MethodBuilder extends AnnotatedElementBuilder<Method> {

	public MethodBuilder(List<Tie> ties, ProjectModel model, Aliaser a) {
		super(ties, model, a);
	}

	public MethodBuilder show(Format f) {
		return (MethodBuilder) super.show(f);
	}

	public TypeBuilder withReturns(Filter<? super Method> f) {
		List<Tie> ties2 = new ArrayList<Tie>();

		for (Tie t : ties) {
		    Method m = getRepresented(t);
		    if (f==null || f.accept(m)) {
			ties2.add(new Tie(createNewSubjectNounPart(t), Relationship.RETURNS, createNoun(m.getGenericReturnType())));
		    }
		}
		return new TypeBuilder(ties2, model, a);
	}


	public TypeBuilder withParameters(Filter<? super Type> f) {
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
		    Method m = getRepresented(t);
		    NounPart subject = createNewSubjectNounPart(t);
			for (Type ty : m.getGenericParameterTypes()) {
			    if (f==null || f.accept(ty)) {
				ties2.add(new Tie(subject, HasRelationship.PARAMETER, createNoun(ty)));
			    }
			}
		}
		return new TypeBuilder(ties2, model, a);
	}

	public MethodBuilder showVisibility(Format f) {
		for (Tie t : ties) {
		    Method m = getRepresented(t);
		    NounPart sub = createNewSubjectNounPart(t);
			if (Modifier.isPublic(m.getModifiers())) {
				f.write(sub, HasRelationship.VISIBILITY, createNoun(new JavaModifier("public")));
			} else if (Modifier.isPrivate(m.getModifiers())) {
				f.write(sub, HasRelationship.VISIBILITY, createNoun(new JavaModifier("private")));
			} else if (Modifier.isProtected(m.getModifiers())) {
				f.write(sub, HasRelationship.VISIBILITY, createNoun(new JavaModifier("protected")));
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

		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
		    Method m = getRepresented(t);
			for (MemberHandle mh : model.getCalledBy(new MethodHandle(m))) {
			    if (mh instanceof MethodHandle) {
				Method m2 = ((MethodHandle)mh).hydrate(cl);
				if ((f == null) || (f.accept(m2))) {
					ties2.add(new Tie(createNewSubjectNounPart(t), Relationship.CALLED_BY, createNoun(m2)));
				}
			    }
			}
		}

		return new MethodBuilder(ties2, model, a);
	}

	/**
	 * Returns classes of methods which call this one
	 */
	public ClassBuilder withCallingClasses(Filter<? super Class<?>> f) {
		final ClassLoader cl = getCurrentClassLoader();
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
		    Method m = getRepresented(t);
			for (MemberHandle mh : model.getCalledBy(new MethodHandle(m))) {
			    if (mh instanceof MethodHandle) {
				Method m2 = ((MethodHandle)mh).hydrate(cl);
				Class<?> c2 = m2.getDeclaringClass();
				if ((f == null) || (f.accept(c2))) {
					ties2.add(new Tie(createNewSubjectNounPart(t), Relationship.CALLED_BY, createNoun(c2)));
				}
			    }
			}
		}

		return new ClassBuilder(ties2, model,a);
	}

	/**
	 * Returns methods which are called by this one
	 */
	public MethodBuilder withCalledMethods(Filter<? super Method> f) {
		final ClassLoader cl = getCurrentClassLoader();
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
		    Method m = getRepresented(t);
			for (MemberHandle mh : model.getCalls(new MethodHandle(m))) {
			    	if (mh instanceof MethodHandle) {
        				Method m2 = ((MethodHandle)mh).hydrate(cl);
        				if ((f == null) || (f.accept(m2))) {
        					ties2.add(new Tie(createNewSubjectNounPart(t), Relationship.CALLS, createNoun(m2)));
        				}
			    	}
			}
		}

		return new MethodBuilder(ties2, model, a);
	}
	
	/**
	 * Returns classes which are called by this one
	 */
	public ClassBuilder withCalledClasses(Filter<? super Class<?>> f) {
		final ClassLoader cl = getCurrentClassLoader();
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
		    Method m = getRepresented(t);
			for (MemberHandle mh : model.getCalls(new MethodHandle(m))) {
			    	if (mh instanceof MethodHandle) {
        				Method m2 = ((MethodHandle)mh).hydrate(cl);
        				Class<?> dc = ((MethodHandle)mh).hydrateClass(cl);
        				if ((f == null) || (f.accept(dc))) {
        					ties2.add(new Tie(createNewSubjectNounPart(t), new MethodCallRelationship(m2), createNoun(dc)));
        				}
			    	}
			}
		}

		return new ClassBuilder(ties2, model, a);
	}

	/**
	 * Returns classes that declare methods
	 */
	public ClassBuilder withDeclaringClasses(Filter<? super Class<?>> f) {
		List<Tie> ties2 = new ArrayList<Tie>();
		for (Tie t : ties) {
		    Method m = getRepresented(t);
			Class<?> c2 = m.getDeclaringClass();
			if ((f == null) || (f.accept(c2))) {
				ties2.add(new Tie(createNewSubjectNounPart(t), HasRelationship.METHOD_OF, createNoun(c2)));
			}

		}

		return new ClassBuilder(ties2, model, a);
	}

	@Override
	public MethodBuilder reduce(Filter<? super Method> f) {
		return new MethodBuilder(reduceInner(f), model, a);
	}
}
