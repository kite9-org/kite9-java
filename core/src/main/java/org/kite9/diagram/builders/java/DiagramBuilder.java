package org.kite9.diagram.builders.java;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.Tie;
import org.kite9.diagram.builders.WithHelperMethodsDiagramBuilder;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.NounRelationshipBinding;
import org.kite9.diagram.builders.krmodel.Relationship;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.HelpMethods;
import org.kite9.framework.model.AnnotationHandle;
import org.kite9.framework.model.ClassHandle;
import org.kite9.framework.model.ProjectModel;

/**
 * Builds a diagram from a method. Each object from the java project may only be
 * represented once in the diagram.
 * 
 * @author robmoffat
 * 
 */
public class DiagramBuilder extends WithHelperMethodsDiagramBuilder {

	ProjectModel model;

	public DiagramBuilder(Aliaser a, Method creator, ProjectModel pm) {
		super(a);
		this.model = pm;
		this.idHelper = new JavaIdHelper(pm);
		this.d = createRepresentation(getId(creator));
		this.creator = creator;
	}

	public DiagramBuilder(Aliaser a, String id, ProjectModel pm) {
		super(a);
		this.model = pm;
		this.d = createRepresentation(id);
		this.creator = id;
	}

	public ProjectModel getProjectModel() {
		return model;
	}

	public ObjectBuilder withObjects(Object... objects) {
		List<Tie> ties = new ArrayList<Tie>(objects.length);
		for (Object s : objects) {
			ties.add(new Tie(null, null, createNoun(s)));
		}
		ObjectBuilder sb = new ObjectBuilder(ties, model, a);
		return sb;
	}

	private static final Set<Tie> SET_OF_NULL = HelpMethods
			.createSet(new Tie[] { null });

	public ClassBuilder withClasses(Class<?>... forClasses) {
		return new ClassBuilder(createTies(SET_OF_NULL, null,
				(Object[]) forClasses), model, a);
	}

	/**
	 * Returns the set of classes which have an in-scope {@link K9OnDiagram}.
	 */
	public ClassBuilder withAnnotatedClasses() {
		ProjectModel pm = getProjectModel();
		Set<String> onDiagramClassNames = pm
				.getClassesWithAnnotation(AnnotationHandle
						.convertClassName(K9OnDiagram.class));
		Set<Class<?>> classes = ClassHandle.hydrateClasses(onDiagramClassNames,
				getCurrentClassLoader());
		for (Iterator<Class<?>> iterator = classes.iterator(); iterator
				.hasNext();) {
			Class<?> class1 = (Class<?>) iterator.next();
			if (!isAnnotated(class1)) {
				iterator.remove();
			}

		}
		Class<?>[] classArray = (Class<?>[]) classes
				.toArray(new Class<?>[classes.size()]);

		// show the on-diagram classes
		ClassBuilder classBuilder = withClasses(classArray);
		return classBuilder;
	}

	public PackageBuilder withPackages(Package... packages) {
		return new PackageBuilder(createTies(SET_OF_NULL, null,
				(Object[]) packages), model, a);
	}

	public PackageBuilder withPackages(Class<?>... packagesForClasses) {
		return withPackages(packagesOf(packagesForClasses));
	}

	protected Package[] packagesOf(Class<?>... packagesForClasses) {
		Set<Package> packages = new LinkedHashSet<Package>();
		for (Class<?> c : packagesForClasses) {
			packages.add(c.getPackage());
		}
		return (Package[]) packages.toArray(new Package[packages.size()]);
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
	 * Filters to just the items mentioned in the arguments
	 */
	public Filter<Object> only(final Object... items) {
		return new Filter<Object>() {

			public boolean accept(Object o) {
				for (Object class1 : items) {
					if (class1.equals(o))
						return true;
				}

				return false;
			}
		};
	}

	public ClassLoader getCurrentClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public DiagramElement getNounElement(Object o) {
		NounPart np = getNounFactory().createNoun(o);
		return contents.get(np);
	}

	public DiagramElement getRelationshipElement(Object o,
			Relationship r) {
		NounRelationshipBinding nrb = new NounRelationshipBinding(getNounFactory().createNoun(o), r);
		return contents.get(nrb);
	}
}
