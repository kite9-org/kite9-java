package org.kite9.diagram.builders.wizards.hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.java.ClassBuilder;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;
import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.diagram.builders.representation.ADLInsertionInterface;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.ProjectModel;

/**
 * This class is used to quickly generate a class hierarchy, provided the top
 * class it will do the rest.
 * 
 * @author moffatr
 * 
 */
public class HierarchyWizard  {

	protected Set<Class<?>> classes = new HashSet<Class<?>>();
	protected List<List<Class<?>>> sortedClasses;
	protected Object container;
	protected boolean sorted = false;
	protected NounFactory nf;
	protected Set<DiagramElement> hierarchyContainers = new HashSet<DiagramElement>();
	protected DiagramBuilder db;
	protected ProjectModel model;
	protected Filter<? super Class<?>> classFilter = null;
	
	public Filter<? super Class<?>> getClassFilter() {
		return classFilter;
	}


	public void setClassFilter(Filter<? super Class<?>> classFilter) {
		this.classFilter = classFilter;
	}


	public HierarchyWizard(Object container, DiagramBuilder db) {
		this.db = db;
		this.nf = db.getNounFactory();
		this.container = container;
		this.model = db.getProjectModel();
	}


	public void add(boolean traverseDownwards, Class<?>... classesToAdd) {
		ClassLoader cl = db.getCurrentClassLoader();
		for (Class<?> class1 : classesToAdd) {
			addClass(traverseDownwards, class1, cl);
		}
	}

	private void addClass(boolean traverseDownwards, Class<?> class1,
			ClassLoader cl) {
		classes.add(class1);
		if (traverseDownwards) {
			for (String name : model.getSubclasses(MemberHandle
					.convertClassName(class1))) {
				Class<?> class2 = MemberHandle.hydrateClass(name, cl);
				if ((classFilter == null) || (classFilter.accept(class2))) {
					addClass(traverseDownwards, class2, cl);
				}
			}
		}
		sorted = false;
	}
	
	protected NounPart getNoun(Object o) {
		return nf.createNoun(o);
	}

	/**
	 * Convenience method to get a basic hierarchy drawn
	 */
	public void show(ClassBuilder cb) {
		for (SimpleRelationship t : cb.getTies()) {
			Class<?> c = cb.getRepresented(t);
			add(true, c);
		}
		
		
		showClasses(db.asConnectedContexts(false, Layout.HORIZONTAL), db.asConnectedGlyphs(""));
		showInheritance(db.asConnectedGlyphs(""));
	}
	
	/**
	 * Adds the classes to the diagram, as well as level-groups, so that classes
	 * with the hierarchical depth are shown on the same level.
	 */
	public void showClasses(Format containerFormat, Format classFormat) {
		List<List<Class<?>>> sortedClasses = sortClassesByLevel();
		// create the containers
		int i = 1;
		for (List<Class<?>> list : sortedClasses) {
			String name = "level " + i++;
			NounPart containerNoun = getNoun(name);
			containerFormat.write(getNoun(container), JavaRelationships.CLASS_GROUP, containerNoun);
			hierarchyContainers.add(db.getInsertionInterface().returnExisting(containerNoun));
			for (Class<?> class1 : list) {
				classFormat.write(containerNoun, JavaRelationships.IS_CLASS, getNoun(class1));
			}
		}
	}

	/**
	 * Shows the EXTENDS/IMPLEMENTS relationships between the classes. Will add
	 * any classes that are not already on the diagram.
	 */
	public void showInheritance(Format classFormat) {
		// ensure classes are on diagram somewhere
		for (Class<?> class1 : classes) {
			classFormat.write(getNoun(container), JavaRelationships.IS_CLASS, getNoun(class1));
		}

		// add the inheritance / extension relationships
		for (Class<?> c : classes) {
			Class<?> super1 = c.getSuperclass();
			if (classes.contains(super1)) {
				classFormat.write(getNoun(c), JavaRelationships.EXTENDS, getNoun(super1));
			}

			for (Class<?> if1 : c.getInterfaces()) {
				if (classes.contains(if1)) {
					classFormat.write(getNoun(c), JavaRelationships.IMPLEMENTS, getNoun(if1));
				}
			}
		}
		
		Filter<Object> includedArrows = new Filter<Object>() {

			public boolean accept(Object o) {
				DiagramElement de = db.getInsertionInterface().returnExisting(o);
				if ((de instanceof Arrow) && (hierarchyContainers.contains(((Arrow)de).getContainer()))) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		Filter<Object> includedGlyphs = new Filter<Object>() {
			public boolean accept(Object o) {
				DiagramElement de = db.getInsertionInterface().returnExisting(o);
				if ((de instanceof Glyph) && (hierarchyContainers.contains(((Glyph)de).getContainer()))) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		db.introduceContexts(includedArrows, new DiagramBuilder.ContextFactory() {

			public Context createContextFor(Container subdivisionOf, List<Contained> contents, ADLInsertionInterface ii) {
				Layout l = (subdivisionOf instanceof Context) ? ((Context)subdivisionOf).getLayoutDirection() :  null;
				return new Context(subdivisionOf.getID()+"-arrows", contents, false, null, l);
			}
			
		});
		
		db.introduceContexts(includedGlyphs, new DiagramBuilder.ContextFactory() {

			public Context createContextFor(Container subdivisionOf, List<Contained> contents, ADLInsertionInterface ii) {
				Layout l = null;
				if (subdivisionOf instanceof Context) {
					// clear layout direction as it is inherited by the child contexts now
					l = ((Context)subdivisionOf).getLayoutDirection();
					((Context)subdivisionOf).setLayoutDirection(null);
				}
				
				return new Context(subdivisionOf.getID()+"-glyphs", contents, false, null, l);
			}
			
		});
		
	}

	private List<List<Class<?>>> sortClassesByLevel() {
		if (sorted)
			return sortedClasses;

		int maxLevels = 1;
		Map<Class<?>, Integer> levels = new HashMap<Class<?>, Integer>();

		for (Class<?> class1 : classes) {
			maxLevels = Math.max(getLevel(levels, class1, classes), maxLevels);
		}

		List<List<Class<?>>> out = new ArrayList<List<Class<?>>>(maxLevels);

		for (int i = 0; i < maxLevels; i++) {
			out.add(new ArrayList<Class<?>>());
		}

		for (Entry<Class<?>, Integer> me : levels.entrySet()) {
			out.get(me.getValue() - 1).add(me.getKey());
		}

		sortedClasses = out;

		return out;
	}

	private int getLevel(Map<Class<?>, Integer> levels, Class<?> class1,
			Set<Class<?>> classes) {
		Integer level = levels.get(class1);
		if (level == null) {
			if (classes.contains(class1)) {
				// need to calc it
				int currentLevel = 1;
				Class<?> super1 = class1.getSuperclass();
				currentLevel = Math.max(currentLevel,
						getLevel(levels, super1, classes) + 1);
				for (Class<?> if1 : class1.getInterfaces()) {
					currentLevel = Math.max(currentLevel,
							getLevel(levels, if1, classes) + 1);
				}
				levels.put(class1, currentLevel);

				return currentLevel;
			} else {
				return 0;
			}
		} else {
			return level;
		}
	}

}
