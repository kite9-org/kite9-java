package org.kite9.diagram.builders.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.AbstractRepresentationBuilder;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.framework.model.AnnotationHandle;
import org.kite9.framework.model.ClassHandle;
import org.kite9.framework.model.ProjectModel;

/**
 * This includes some as... methods which provide some simple functionality to help you create basic layouts.
 * 
 * Also includes some functionality to allow you to subdivide the diagram up with contexts.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractJavaRepresentationBuilder<K> extends AbstractRepresentationBuilder<K> {

	public AbstractJavaRepresentationBuilder(ProjectModel pm, NounPart theDiagram, NounFactory nf) {
		super(pm, theDiagram, nf);
	}

	

	/**
	 * Returns the set of classes which have an in-scope {@link K9OnDiagram}.
	 */
	public ClassBuilder withAnnotatedClasses() {
		Set<String> onDiagramClassNames = model
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

	public ClassBuilder withClasses(Class<?>... forClasses) {
		return new ClassBuilder(createPropositions(getDiagramProposition(), JavaRelationships.GROUP_CLASS,
				(Object[]) forClasses), model, nf);
	}

	protected Collection<Proposition> getDiagramProposition() {
		return Collections.singletonList(new JavaPropositionBinding(null, null, theDiagramNoun));
	}



	public ObjectBuilder withObjects(Object... objects) {
		List<Proposition> ties = new ArrayList<Proposition>(objects.length);
		for (Object s : objects) {
			ties.add(new JavaPropositionBinding(null, null, createNoun(s)));
		}
		ObjectBuilder sb = new ObjectBuilder(ties, model, nf);
		return sb;
	}

	public PackageBuilder withPackages(Class<?>... packagesForClasses) {
		return withPackages(packagesOf(packagesForClasses));
	}

	public PackageBuilder withPackages(Package... packages) {
		return new PackageBuilder(createPropositions(getDiagramProposition(), JavaRelationships.GROUP_PACKAGE,
				(Object[]) packages), model, nf);
	}

}