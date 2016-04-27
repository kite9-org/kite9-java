package org.kite9.diagram.builders.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.kite9.diagram.builders.AbstractElementBuilder;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.AbstractElementBuilder.BasicObjectExtractor;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.diagram.builders.krmodel.verb.Verb.VerbType;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.AbstractHandle;
import org.kite9.framework.model.AnnotationHandle;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.ProjectModel;

public class ClassBuilder extends AnnotatedElementBuilder<Class<?>> {

	public ClassBuilder(List<Proposition> forX, ProjectModel model, NounFactory a) {
		super(forX, model, a);
	}

	public ClassBuilder showVisibility(Format f) {
		for (Proposition t : ties) {
			NounPart subject = getNounFactory().extractObject(t);
			Class<?> c = getRepresented(t);
			if (Modifier.isPublic(c.getModifiers())) {
				show(f, new JavaPropositionBinding(subject, JavaRelationships.IS_VISIBILITY, createNoun(JavaModifier.PUBLIC)));
			} else if (Modifier.isPrivate(c.getModifiers())) {
				show(f, new JavaPropositionBinding(subject, JavaRelationships.IS_VISIBILITY, createNoun(JavaModifier.PRIVATE)));
			} else if (Modifier.isProtected(c.getModifiers())) {
				show(f, new JavaPropositionBinding(subject, JavaRelationships.IS_VISIBILITY, createNoun(JavaModifier.PROTECTED)));
			}
		}
		return this;
	}

	public ClassBuilder showStatic(Format f) {
		for (Proposition t : ties) {
			Class<?> c = getRepresented(t);
			NounPart subject = getNounFactory().extractObject(t);
			if (c.getEnclosingClass() != null) {
				// inner class
				if (Modifier.isStatic(c.getModifiers())) {
					show(f, new JavaPropositionBinding(subject, JavaRelationships.IS_MODIFIER, 
						createNoun(JavaModifier.STATIC)));
				}
			}
		}
		return this;
	}

	public ClassBuilder show(Format f) {
		return (ClassBuilder) super.show(f);
	}

	public ClassBuilder showFinal(Format f) {
		for (Proposition t : ties) {
			Class<?> c = getRepresented(t);
			NounPart subject = getNounFactory().extractObject(t);
			if (Modifier.isFinal(c.getModifiers())) {
				show(f, 
					new JavaPropositionBinding(subject, JavaRelationships.IS_MODIFIER, 
							createNoun(JavaModifier.FINAL)));
			}
		}
		return this;
	}

	/**
	 * Creates a helper to allow you to manipulate the superclasses of the
	 * classes that this builder manages.
	 * 
	 * @param f
	 *            An optional filter to reduce the number of interfaces being
	 *            considered.
	 */
	public ClassBuilder withSuperClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f) {
		return new ClassBuilder(new BasicObjectExtractor() {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.EXTENDS;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public List<?> createObjects(Object from) {
				return Arrays.asList(((Class<?>)from).getSuperclass());
			}
		}.generatePropositions(Class.class, f), model, nf);
	}

	/**
	 * Creates a helper to allow you to manipulate the interfaces this class
	 * implements or extends
	 * 
	 * @param f
	 *            An optional filter to reduce the number of interfaces being
	 *            considered.
	 * 
	 * @param traverse
	 *            Set to true if you want interfaces declared by superclasses
	 *            and superinterfaces too
	 */
	public ClassBuilder withInterfaces(@SuppressWarnings("rawtypes")
	Filter<? super Class> f, boolean traverse) {
		return new ClassBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.IMPLEMENTS;
			}
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				out.addAll(Arrays.asList(in.getInterfaces()));
			}
		}.generatePropositions(Class.class, f), model,nf);
	}

	/**
	 * Creates a helper to allow you to manipulate methods on this class.
	 * 
	 * @param f
	 *            An optional filter to reduce the number of methods being
	 *            considered.
	 * 
	 * @oparam traverse Set to true if you want declarations from superclasses
	 *         and superinterfaces too
	 */
	public MethodBuilder withMethods(Filter<? super Method> f, boolean traverse) {
		return new MethodBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				out.addAll(Arrays.asList(in.getDeclaredMethods()));
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.METHOD;
			}
			
			
			
		}.generatePropositions(Method.class, f), model, nf);
	}

	/**
	 * Creates a helper to allow you to manipulate inner classes on this class.
	 * 
	 * @param f
	 *            An optional filter to reduce the number of methods being
	 *            considered.
	 * 
	 * @oparam traverse Set to true if you want declarations from superclasses
	 *         too
	 */
	public ClassBuilder withInnerClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f, boolean traverse) {
		return new ClassBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				out.addAll(Arrays.asList(in.getDeclaredClasses()));
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.INNER_CLASS;
			}
			
			
			
		}.generatePropositions(Class.class, f), model, nf);
	}

	/**
	 * Creates a helper to allow you to manipulate subclasses within the project
	 * of the current class or classes.
	 * 
	 * @param f
	 *            Optional filter to reduce the number of returned classes
	 * @param traverse
	 *            set to true if you want the entire subclass tree (i.e.
	 *            sub-sub-classes etc)s
	 * @return
	 */
	public ClassBuilder withSubClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f, boolean traverse) {
		final ClassLoader cl = getCurrentClassLoader();

		return new ClassBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.EXTENDED_BY;
			}
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				out.addAll(
					MemberHandle.hydrateClasses(model.getSubclasses(MemberHandle.convertClassName(in)), cl));
			}
		}.generatePropositions(Class.class, f), model, nf);
	}

	/**
	 * Creates a helper to allow you to manipulate fields on this class.
	 * 
	 * @param f
	 *            An optional filter to reduce the number of methods being
	 *            considered.
	 * @oparam traverse Set to true if you want declarations from superclasses
	 *         too
	 */
	public FieldBuilder withFields(Filter<? super Field> f, boolean traverse) {
		return new FieldBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.FIELD;
			}
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				out.addAll(Arrays.asList(in.getDeclaredFields()));
			}
		}.generatePropositions(Field.class, f), model,nf);
	}
	
	@Override
	public ClassBuilder reduce(Filter<? super Class<?>> f) {
		return new ClassBuilder(reduceInner(f), model,nf);
	}

	/**
	 * Assumes that the classes in this builder are annotations, and provides
	 * you with a classbuilder of classes that have declared these annotations.
	 */
	public ClassBuilder withAnnotatedClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f) {
		final ClassLoader cl = getCurrentClassLoader();

		return new ClassBuilder(new BasicObjectExtractor() {

			public List<?> createObjects(Object from) {
				Set<String> classNames = model.getClassesWithAnnotation(MemberHandle.convertClassName((Class<?>) from));
				List<Class<?>> out = new ArrayList<Class<?>>(classNames.size());
				for (String name : classNames) {
					out.add(MemberHandle.hydrateClass(name, cl));
				}
				return out;
			}

			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.ANNOTATION_OF;
			}
			
		}.generatePropositions(Class.class, f), model, nf);
	}

	/**
	 * Returns ties for all of the annotations which reference this class
	 */
	public AnnotationBuilder withReferencingAnnotations(Filter<? super Annotation> f) {
		final ClassLoader cl = getCurrentClassLoader();
		return new AnnotationBuilder(new BasicObjectExtractor() {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.REFERENCED_BY;
			}
			
			@Override
			public List<?> createObjects(Object from) {
				Set<AnnotationHandle> handles = model.getAnnotationReferences(MemberHandle.convertClassName((Class<?>) from));
				return handles.stream().map(h -> h.hydrate(cl)).collect(Collectors.toList());
			}
			
		}.generatePropositions(Annotation.class, f), model,nf);

	}

	/**
	 * Returns ties for all of the annotated elements which reference this
	 * class, via an annotation.
	 * 
	 */
	public AnnotatedElementBuilder<?> withReferencingAnnotatedElements(Filter<? super Class<? extends Annotation>> f) {
		final ClassLoader cl = getCurrentClassLoader();
		List<Proposition> out = new ArrayList<Proposition>();
		for (Proposition t : ties) {
			Class<?> c = getRepresented(t);
			NounPart subject = getNounFactory().createNewSubjectNounPart(t);
			Set<AnnotationHandle> handles = model.getAnnotationReferences(MemberHandle.convertClassName(c));
			for (AnnotationHandle h : handles) {
				AnnotatedElement object = h.getAnnotatedItem().hydrate(cl);
				Annotation relation = h.hydrate(cl);
				if ((f==null) || (f.accept(relation.annotationType()))) {
					String annAlias = a.getObjectAlias(relation);
					Verb rel = new AbstractVerb(annAlias+"(r)", new AbstractVerb(annAlias));
					Proposition newTie = new JavaPropositionBinding(subject, rel, createNoun(object), object);
					out.add(newTie);
				}
			}
		}
		
		return new AnnotatedElementBuilder<AnnotatedElement>(new BasicObjectExtractor() {

			@Override
			public List<?> createObjects(Object from) {
				Set<AnnotationHandle> handles = model.getAnnotationReferences(MemberHandle.convertClassName((Class<?>) from));
				return new ArrayList<>(handles);
			}

			@Override
			public Object getFilterObject(Object from) {
				return ((AnnotationHandle) from).getAnnotatedItem().hydrate(cl);
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				Annotation relation = ((AnnotationHandle) object).hydrate(cl);
				Verb rel = new AbstractVerb(annAlias+"(r)", new AbstractVerb(annAlias));
			}
			
		}.generatePropositions(AnnotatedElement.class, f), model,nf);
	}

	/**
	 * Returns classes calling this one
	 */
	public ClassBuilder withCallingClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f, boolean traverse) {
		final ClassLoader cl = getCurrentClassLoader();
		
		return new ClassBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				extractFromModel(in, out, WhatToExtract.HANDLES, ModelPart.CALLED_BY);
			}

			@Override
			public Object getFilterObject(Object from) {
				MethodHandle mh = (MethodHandle) from;
				return ((MethodHandle) mh).hydrateClass(cl);
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				MethodHandle mh = (MethodHandle) object;
				Method m2 = ((MethodHandle) mh).hydrate(cl);
				return new MethodCallVerb(m2, VerbType.PASSIVE);
			}
			
		}.generatePropositions(Class.class, f), model, nf);
	}

	/**
	 * Returns classes calling this one
	 */
	public MethodBuilder withCallingMethods(Filter<? super Method> f, boolean traverse) {
		return new MethodBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.CLASS_CALLED_BY_METHOD;
			}
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				extractFromModel(in, out, WhatToExtract.METHODS, ModelPart.CALLED_BY);
			}
		}.generatePropositions(Method.class, f), model,nf);

	}
	
	enum WhatToExtract { HANDLES, METHODS };
	enum ModelPart { CALLS, CALLED_BY };

	private void extractFromModel(Class<?> in, List<Object> out, WhatToExtract what, ModelPart mp) {
		final ClassLoader cl = getCurrentClassLoader();
		for (Method m : in.getDeclaredMethods()) {
			MethodHandle meth = new MethodHandle(m);
			Collection<MemberHandle> refs = (mp == ModelPart.CALLS) ? model.getCalls(meth) : model.getCalledBy(meth);
			
			for (MemberHandle mh : refs) {
				if (mh instanceof MethodHandle) {
					switch (what) {
					case HANDLES:
						out.add(mh);
						break;
					default:
					case METHODS:
						out.add(mh.hydrate(cl));
						break;
					}
				}
			}
		}
	}


	/**
	 * Returns classes which are called by this one
	 */
	public ClassBuilder withCalledClasses(@SuppressWarnings("rawtypes") Filter<? super Class> f, final boolean traverse) {
		final ClassLoader cl = getCurrentClassLoader();

		return new ClassBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				extractFromModel(in, out, WhatToExtract.HANDLES, ModelPart.CALLS);
			}

			@Override
			public Object getFilterObject(Object from) {
				MethodHandle mh = (MethodHandle) from;
				return ((MethodHandle) mh).hydrateClass(cl);
			}

			@Override
			public Verb getVerb(Object subject, Object object) {
				MethodHandle mh = (MethodHandle) object;
				Method m2 = ((MethodHandle) mh).hydrate(cl);
				return new MethodCallVerb(m2);
			}
			
			
			
		}.generatePropositions(Class.class, f), model, nf);
	}
	
	abstract class TraversingObjectExtractor extends AbstractElementBuilder<Class<?>>.BasicObjectExtractor {
		
		private final  boolean traverse;
		
		TraversingObjectExtractor(boolean traverse) {
			this.traverse = traverse;
		}
		
		private Set<Class<?>> handleSuperTraverse(Class<?> from) {
			Set<Class<?>> todo = traverse ? superTraverse(from) : new LinkedHashSet<Class<?>>();
			todo.add((Class<?>) from);
			return todo;
		}
		
		private Set<Class<?>> superTraverse(Class<?> c) {
			LinkedHashSet<Class<?>> out = new LinkedHashSet<Class<?>>();
			if (c.getSuperclass() != null)
				out.add(c.getSuperclass());
			for (int i = 0; i < c.getInterfaces().length; i++) {
				out.add(c.getInterfaces()[i]);
			}
			return out;
		}
		
		@Override
		public List<Object> createObjects(Object from) {
			Set<Class<?>> todo = handleSuperTraverse((Class<?>) from);
			List<Object> out = new ArrayList<Object>();
			for (Class<?> class1 : todo) {
				processClass(class1, out);
			}
			
			return out;
		}
		
		public abstract void processClass(Class<?> in, List<Object> out);
		
	}

	

	
	/**
	 * Returns methods which are called by this class
	 */
	public MethodBuilder withCalledMethods(Filter<? super Method> f, final boolean traverse) {
		return new MethodBuilder(new TraversingObjectExtractor(traverse) {

			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.CLASS_CALLS_METHOD;
			}

			@Override
			public void processClass(Class<?> in, List<Object> out) {
				extractFromModel(in, out, WhatToExtract.METHODS, ModelPart.CALLS);
			}
			
			
		}.generatePropositions(Method.class, f), model,nf);
	}
	
	/**
	 * Returns classes which this class depends on to work.
	 */
	public ClassBuilder withDependencies(@SuppressWarnings("rawtypes") Filter<? super Class> f, boolean traverse) {
		final ClassLoader cl = getCurrentClassLoader();
		
		return new ClassBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.REQUIRES;
			}
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				for (String depName : model.getDependedOnClasses(in.getName())) {
					Class<?> depClass = AbstractHandle.hydrateClass(depName, cl);
					out.add(depClass);
				}
			}
		}.generatePropositions(Class.class, f), model,nf);
	}
	
	/**
	 * Returns classes which require this class to work.
	 */
	public ClassBuilder withDependants(@SuppressWarnings("rawtypes") Filter<? super Class> f, boolean traverse) {
		final ClassLoader cl = getCurrentClassLoader();

		return new ClassBuilder(new TraversingObjectExtractor(traverse) {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.REQUIRED_BY;
			}
			
			@Override
			public void processClass(Class<?> in, List<Object> out) {
				for (String depName : model.getDependedOnClasses(in.getName())) {
					Class<?> depClass = AbstractHandle.hydrateClass(depName, cl);
					out.add(depClass);
				}
			}
		}.generatePropositions(Class.class, f), model,nf);
	}

}
