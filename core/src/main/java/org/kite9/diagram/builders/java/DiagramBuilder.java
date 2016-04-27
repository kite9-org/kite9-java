package org.kite9.diagram.builders.java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Key;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.ADLFormat;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.id.IdHelper;
import org.kite9.diagram.builders.java.krmodel.BasicJavaNounFactory;
import org.kite9.diagram.builders.java.krmodel.JavaIdHelper;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.diagram.builders.representation.ADLInsertionInterface;
import org.kite9.diagram.builders.representation.ADLRepresentation;
import org.kite9.diagram.builders.representation.Representation;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.alias.PropertyAliaser;
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
public class DiagramBuilder extends AbstractJavaRepresentationBuilder<Diagram> {

	final ProjectModel model;
	
	public DiagramBuilder(NounPart theDiagram, ProjectModel pm, NounFactory nf) {
		super(pm, theDiagram, nf);
		this.model = pm;
	}

	public DiagramBuilder(Method creator, ProjectModel pm, NounFactory nf) {
		this(nf.createNoun(creator), pm, nf);
	}
	
	public DiagramBuilder(Method creator, ProjectModel pm, Aliaser a, IdHelper helper) {
		this(creator, pm, new BasicJavaNounFactory(a, helper));
	}

	public DiagramBuilder(NounPart theDiagram, ProjectModel pm, Aliaser a, IdHelper helper) {
		this(theDiagram, pm, new BasicJavaNounFactory(a, helper));
	}
	
	public DiagramBuilder(Method creator, ProjectModel pm) {
		this(creator, pm, new PropertyAliaser(), new JavaIdHelper());
	}
	
	public DiagramBuilder(NounPart theDiagram, ProjectModel pm) {
		this(theDiagram, pm, new PropertyAliaser(), new JavaIdHelper());
	}

	public ProjectModel getProjectModel() {
		return model;
	}

	public ObjectBuilder withObjects(Object... objects) {
		List<Proposition> ties = new ArrayList<Proposition>(objects.length);
		for (Object s : objects) {
			ties.add(new JavaPropositionBinding(null, null, createNoun(s)));
		}
		ObjectBuilder sb = new ObjectBuilder(ties, model, nf);
		return sb;
	}

	public ClassBuilder withClasses(Class<?>... forClasses) {
		return new ClassBuilder(createPropositions(getDiagramProposition(), JavaRelationships.GROUP_CLASS,
				(Object[]) forClasses), model, nf);
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
		return new PackageBuilder(createPropositions(getDiagramProposition(), JavaRelationships.GROUP_PACKAGE,
				(Object[]) packages), model, nf);
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

	public ClassLoader getCurrentClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public DiagramElement getNounElement(Object o) {
		NounPart np = getNounFactory().createNoun(o);
		return contents.get(np);
	}

	public DiagramElement getRelationshipElement(Object o,
			Verb r) {
		JavaPropositionBinding nrb = new JavaPropositionBinding(getNounFactory().createNoun(o), r);
		return contents.get(nrb);
	}

	@Override
	public DiagramBuilder withKeyText(String boldtext, String body) {
		super.withKeyText(boldtext, body);
		return this;
	}
	
	protected BasicDiagramBuilder withKeyText(String boldtext, String body) {
		Key k = new Key(boldtext, body, null);
		d.setKey(k);
		return this;
	}

	public Format asConnectedContexts() {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asContext(true, null, null), null, null);
	}

	public Format asConnectedContexts(boolean border, Layout l) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asContext(border, l, null), null, null);
	}

	public Format asConnectedContexts(boolean border, Layout l, Direction d) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asContext(border, l, null), d, null);
	}

	public Format asConnectedGlyphs() {
		return ADLBasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asGlyph(null), null, null);
	}

	public Format asConnectedGlyphs(String stereotypeOverride) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asGlyph(stereotypeOverride), null, null);
	}

	public Format asConnectedGlyphs(String stereotypeOverride, Direction d) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asGlyph(stereotypeOverride), d, null);
	}

	private void considerSubdivision(Container c, Set<Object> included, ContextFactory cf) {
		Set<Contained> itemsToSubdivide = new LinkedHashSet<Contained>(c.getContents().size());
		Integer firstIndex = null;
		for (int i = 0; i < c.getContents().size(); i++) {
			Contained contained = c.getContents().get(i);
			if (included.contains(contained)) {
				itemsToSubdivide.add(contained);
				if (firstIndex == null) {
					firstIndex = i;
				}
			}
						
			// handle recursion
			if (contained instanceof Container) {
				considerSubdivision((Container)contained, included, cf);
			}
		}
		
		if (itemsToSubdivide.size() > 0) {
			// ok, we need to introduce a context
			Context c2 = cf.createContextFor(c, new ArrayList<Contained>(itemsToSubdivide), getInsertionInterface());
			c.getContents().add(firstIndex, c2);
			c.getContents().removeAll(itemsToSubdivide);
		}
	}

	/**
	 * For each container in the diagram (including the diagram itself) containing elements N, create a context containing F elements out of N, 
	 * where F is the filtered set of elements.  If the size of F is zero, no subcontext is created.
	 */
	public void introduceContexts(final Filter<Object> f, ContextFactory cf) {
		Set<Object> included = new HashSet<Object>(contents.size());
		for (Entry<Object, DiagramElement> cont : contents.entrySet()) {
			if (f.accept(cont.getKey())) {
				included.add(cont.getValue());
			}
		}
		
		considerSubdivision(d, included, cf);
	}
	

	@Override
	public DiagramBuilder withKeyText(String boldtext, String body) {
		super.withKeyText(boldtext, body);
		return this;
	}

	protected BasicDiagramBuilder withKeyText(String boldtext, String body) {
		Key k = new Key(boldtext, body, null);
		d.setKey(k);
		return this;
	}
	
	/**
	 * Helper interface for introduceContexts method, so you can describe exactly the style of context you want to create.
	 */
	public static interface ContextFactory {
		
		public Context createContextFor(Container subdivisionOf, List<Contained> contents, ADLInsertionInterface ii);
		
	}

	

	private void considerSubdivision(Container c, Set<Object> included, ContextFactory cf) {
		Set<Contained> itemsToSubdivide = new LinkedHashSet<Contained>(c.getContents().size());
		Integer firstIndex = null;
		for (int i = 0; i < c.getContents().size(); i++) {
			Contained contained = c.getContents().get(i);
			if (included.contains(contained)) {
				itemsToSubdivide.add(contained);
				if (firstIndex == null) {
					firstIndex = i;
				}
			}
						
			// handle recursion
			if (contained instanceof Container) {
				considerSubdivision((Container)contained, included, cf);
			}
		}
		
		if (itemsToSubdivide.size() > 0) {
			// ok, we need to introduce a context
			Context c2 = cf.createContextFor(c, new ArrayList<Contained>(itemsToSubdivide), getInsertionInterface());
			c.getContents().add(firstIndex, c2);
			c.getContents().removeAll(itemsToSubdivide);
		}
	}

	@Override
	protected Representation<Diagram> createRepresentation(NounPart id) {
		return new ADLRepresentation(id);
	}

	public Diagram getDiagram() {
		return representation.render();
	}

	@Override
	public ADLRepresentation getRepresentation() {
		return (ADLRepresentation) super.getRepresentation();
	}

	public Format asSymbols() {
		return ADLFormat.typeIgnored(getRepresentation(), 
				  ADLFormat.asSymbols(getRepresentation()));
	}
	
	

}
