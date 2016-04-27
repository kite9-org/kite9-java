package org.kite9.diagram.builders.java;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.annotation.K9Exclude;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.AbstractBuilder;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.RepresentingNoun;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.framework.model.ClassHandle;
import org.kite9.framework.model.PackageHandle;
import org.kite9.framework.model.ProjectModel;

public abstract class AbstractJavaBuilder extends AbstractBuilder {

	protected final ProjectModel model;
	protected final NounFactory nf;

	public AbstractJavaBuilder(ProjectModel model, NounFactory nf) {
		this.model = model;
		this.nf = nf;
	}

	protected Package[] packagesOf(Class<?>... packagesForClasses) {
		Set<Package> packages = new LinkedHashSet<Package>();
		for (Class<?> c : packagesForClasses) {
			packages.add(c.getPackage());
		}
		return (Package[]) packages.toArray(new Package[packages.size()]);
	}

	public ClassLoader getCurrentClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public NounFactory getNounFactory() {
		return nf;
	}

	public <Y> List<Proposition> createPropositions(Collection<Proposition> old, Verb r, Y... items) {
		return createPropositions(old, r, getNounFactory(), items);
	}
		
	public <Y> List<Proposition> createPropositions(Collection<Proposition> old, Verb r, NounFactory nf, Object... items) {
		List<Proposition> ties = new ArrayList<Proposition>(items.length * old.size());
		for (Proposition item : old) {
			for (int i = 0; i < items.length; i++) {
				ties.add(createProposition(nf.extractObject(item), r,  nf.createNoun(items[i])));
			}
		}

		return ties;
	}
	
	protected Proposition createProposition(NounPart subject, Verb v, NounPart object) {
		return new JavaPropositionBinding(subject, v, object);
	}

	/**
	 * Returns true for fields, methods, inner classes that have an in-scope
	 * {@link K9OnDiagram} annotation.
	 */
	public boolean isAnnotated(AnnotatedElement o) {
		K9OnDiagram on = null;
		if (o instanceof AnnotatedElement) {
			AnnotatedElement ae = (AnnotatedElement) o;
			on = ae.getAnnotation(K9OnDiagram.class);
		}
	
		if (on != null) {
			if ((on.on().length == 0))
				return true;
	
			for (Class<?> on1 : on.on()) {
				if (on1.equals((creator instanceof Method) ? ((Method) creator)
						.getDeclaringClass() : null)) {
					return true;
				}
			}
		}
	
		return false;
	}

	/**
	 * Returns true for fields, methods, inner classes that have an in-scope
	 * {@link K9Exclude} annotation.
	 */
	public boolean isExcluded(AnnotatedElement o) {
		K9Exclude on = null;
		if (o instanceof AnnotatedElement) {
			AnnotatedElement ae = (AnnotatedElement) o;
			on = ae.getAnnotation(K9Exclude.class);
		}
	
		if (on != null) {
			if ((on.from().length == 0))
				return true;
	
			for (Class<?> on1 : on.from()) {
				if (on1.equals((creator instanceof Method) ? ((Method) creator)
						.getDeclaringClass() : null)) {
					return true;
				}
			}
		}
	
		return false;
	}

	/**
	 * Filters to just the items mentioned in the arguments
	 */
	public Filter<Object> only(final Object... items) {
		return new Filter<Object>() {
	
			public boolean accept(Object o) {
				for (Object class1 : items) {
					if ((class1.equals(o)) || (class1.equals(o.getClass())))
						return true;
				}
	
				return false;
			}
		};
	}

	/**
	 * Filters methods, fields, inner classes to just the ones with an in-scope
	 * {@link K9OnDiagram} annotation.
	 */
	public Filter<AnnotatedElement> onlyAnnotated() {
		return new Filter<AnnotatedElement>() {
			public boolean accept(AnnotatedElement o) {
				return isAnnotated(o);
			}
		};
	}

	/**
	 * Filters methods, fields, inner classes to exclude ones with an in-scope
	 * {@link K9Exclude} annotation.
	 */
	public Filter<AnnotatedElement> onlyNotExcluded() {
		return new Filter<AnnotatedElement>() {
			public boolean accept(AnnotatedElement o) {
				return !isExcluded(o);
			}
		};
	}

	/**
	 * Filters objects, classes which are in the java model.
	 */
	public Filter<Object> onlyInModel() {
		return onlyInModel("");
	}

	/**
	 * Filters objects, classes which are in the java model and
	 * within a certain part of the package structure
	 */
	public Filter<Object> onlyInModel(Package packageRoot) {
		return onlyInModel(PackageHandle.convertPackageName(packageRoot));
	}

	/**
	 * Filters objects, classes which are in the java model and
	 * within a certain part of the package structure
	 */
	public Filter<Object> onlyInModel(final String packageRoot) {
		return new Filter<Object>() {			
			public boolean accept(Object o) {
				if (o instanceof Class) {
					String name = ClassHandle.convertClassName((Class<?>) o);
					return model.withinModel(name) && ((packageRoot==null) || (packageRoot.length() == 0) || (name.startsWith(packageRoot)));
				}
				
				if (o instanceof Field) {
					return accept ( ((Field)o).getDeclaringClass());
				}
				
				if (o instanceof Method) {
					return accept ( ((Field)o).getDeclaringClass());
				}
				
				if (o instanceof Constructor<?>) {
					return accept ( ((Constructor<?>)o).getDeclaringClass());
				}
				
				return accept(o.getClass());
			}
		};
	}


}