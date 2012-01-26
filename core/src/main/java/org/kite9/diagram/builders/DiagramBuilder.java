package org.kite9.diagram.builders;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Key;
import org.kite9.diagram.adl.KeyHelper;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.LinkEndStyle;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.formats.BasicFormats;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Connection;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.HelpMethods;
import org.kite9.framework.common.Kite9ProcessingException;
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
public class DiagramBuilder extends AbstractBuilder {

	public static final Object THE_DIAGRAM = new Object();

	protected IdHelper idHelper;
	protected TypeNounHelper typeHelper = new TypeNounHelper();
	protected KeyHelper kh = new KeyHelper();

	private Method creator;
	private Diagram d;
	private Map<Object, DiagramElement> contents = new HashMap<Object, DiagramElement>();
	private Map<Object, Symbol> symbols = new HashMap<Object, Symbol>();

	public DiagramBuilder(Aliaser a, Method creator, ProjectModel pm) {
		super(pm, a);
		this.idHelper = new IdHelper(pm);
		this.d = createRepresentation(getId(creator));
		this.creator = creator;
	}

	public ProjectModel getProjectModel() {
		return model;
	}

	public Diagram getDiagram() {
		addKey();
		return d;
	}

	public Diagram createRepresentation(String id) {
		return new Diagram(id, new ArrayList<Contained>(), null);
	}

	private void addKey() {
		Collection<Symbol> syms = kh.getUsedSymbols();
		if (syms.size() > 0) {
			List<Symbol> ordered = new ArrayList<Symbol>(syms);
			Collections.sort(ordered);

			if (d.getKey() == null) {
				Key k = new Key(null, null, ordered);
				d.setKey(k);
			} else {
				d.getKey().setSymbols(ordered);
			}

		}
	}

	public DiagramBuilder withKeyText(String boldtext, String body) {
		Key k = new Key(boldtext, body, null);
		d.setKey(k);
		return this;
	}

	public String getId(Object o) {
		return idHelper.getId(o);
	}

	public InsertionInterface getInsertionInterface() {
		return new InsertionInterface() {

			public DiagramElement returnExisting(NounPart np) {
				return contents.get(np);
			}

			public DiagramElement returnExisting(NounRelationshipBinding sr) {
				return contents.get(sr);
			}

			public Link returnLink(DiagramElement from, DiagramElement to, Label fromLabel, Label toLabel,
					boolean arrowHead, Direction d) {
				if ((from instanceof Connected) && (to instanceof Connected)) {
					Connected cfrom = (Connected) from;
					Connected cto = (Connected) to;
					Connection out = cfrom.getConnectionTo(cto);
					if (out == null) {
						return new Link(cfrom, cto, null, fromLabel, arrowHead ? LinkEndStyle.ARROW : null, toLabel, d);
					} else {
						return (Link) out;
					}
				} else {
					throw new Kite9ProcessingException("Could not link between: " + from + " and " + to);
				}
			}

			/**
			 * Returns the most suitable container for a new object in a
			 * particular relationship.
			 * 
			 * @param o
			 *            the subject of the relationship
			 * @param rel
			 * @return
			 */
			public Container getContainerFor(Object o, Relationship rel) {
				if (o == null)
					return d;

				DiagramElement within = contents.get(o);
				if (within == null) {
					// no context to place the element in, so put in the
					// diagram.
					return d;
				}

				if (rel instanceof HasRelationship) {
					if (within instanceof Container) {
						return (Container) within;
					} else if (within instanceof Contained) {
						return ((Contained) within).getContainer();
					} else {
						throw new Kite9ProcessingException("Cannot find container for " + within);
					}
				} else {
					// object must exist outside context
					if (within instanceof Contained) {
						return ((Contained) within).getContainer();
					} else {
						throw new Kite9ProcessingException("Cannot find container for " + within);
					}
				}
			}

			/**
			 * Looks for an existing diagram element, or returns a new glyph
			 */
			public DiagramElement returnGlyph(Container within, NounPart value, String label, String stereo) {
				DiagramElement out = contents.get(value);
				if (out == null) {
					String id = idHelper.getId(value);
					out = new Glyph(id, stereo, label, null, null);
					contents.put(value, out);
					within.getContents().add((Glyph) out);
					((Glyph) out).setContainer(within);
				}
				return out;
			}

			public DiagramElement returnContext(Container within, NounPart value, Label overrideLabel, boolean border,
					Layout d) {
				DiagramElement out = contents.get(value);
				if (out == null) {
					String id = idHelper.getId(value);
					out = new Context(id, null, border, border ? overrideLabel : null, d);
					contents.put(value, out);
					within.getContents().add((Context) out);
					((Context) out).setContainer(within);
				}
				return out;
			}

			public DiagramElement returnArrow(Container container, NounRelationshipBinding sr, String overrideLabel) {
				return returnArrowInner(container, sr, overrideLabel);
			}

			public DiagramElement returnArrow(Container within, Relationship r, String label) {
				return returnArrowInner(within, r, label);
			}

			public DiagramElement returnArrow(Container within, NounPart np, String label) {
				return returnArrowInner(within, np, label);
			}

			private DiagramElement returnArrowInner(Container within, Object r, String label) {
				DiagramElement out = contents.get(r);
				if (out == null) {
					String id = idHelper.getId(r);
					out = new Arrow(id, label);
					contents.put(r, out);
					within.getContents().add((Arrow) out);
					((Arrow) out).setContainer(within);
				}
				return out;
			}

			public Symbol returnSymbol(NounRelationshipBinding sbr, String text, String prefs) {
				Symbol out = symbols.get(sbr);
				if (out != null) {
					return out;
				}

				Symbol s = kh.createSymbol(text, prefs);
				symbols.put(sbr, s);
				return s;
			}

			public TextLine returnTextLine(Glyph container, NounPart referred, String text) {
				DiagramElement de = contents.get(referred);
				if (de instanceof TextLine) {
					return (TextLine) de;
				} else if (de == null) {
					TextLine tl = new TextLine(text);
					contents.put(referred, tl);
					return tl;
				} else {
					throw new Kite9ProcessingException(referred + " already exists in the diagram as " + de);
				}
			}

		};
	}

	public StringBuilder withStrings(String... strings) {
		List<Tie> ties = new ArrayList<Tie>(strings.length);
		for (String s : strings) {
			ties.add(new Tie(null, null, createNoun(s)));
		}
		StringBuilder sb = new StringBuilder(ties, model, a);
		return sb;
	}

	private static final Set<Tie> SET_OF_NULL = HelpMethods.createSet(new Tie[] { null });

	public ClassBuilder withClasses(Class<?>... forClasses) {
		return new ClassBuilder(createTies(SET_OF_NULL, null, (Object[]) forClasses), model, a);
	}

	/**
	 * Returns the set of classes which have an in-scope {@link K9OnDiagram}.
	 */
	public ClassBuilder withAnnotatedClasses() {
		ProjectModel pm = getProjectModel();
		Set<String> onDiagramClassNames = pm.getClassesWithAnnotation(AnnotationHandle
				.convertClassName(K9OnDiagram.class));
		Set<Class<?>> classes = ClassHandle.hydrateClasses(onDiagramClassNames, getCurrentClassLoader());
		for (Iterator<Class<?>> iterator = classes.iterator(); iterator.hasNext();) {
			Class<?> class1 = (Class<?>) iterator.next();
			if (!isAnnotated(class1)) {
				iterator.remove();
			}
			
		}
		Class<?>[] classArray = (Class<?>[]) classes.toArray(new Class<?>[classes.size()]);

		// show the on-diagram classes
		ClassBuilder classBuilder = withClasses(classArray);
		return classBuilder;
	}

	public PackageBuilder withPackages(Package... packages) {
		return new PackageBuilder(createTies(SET_OF_NULL, null, (Object[]) packages), model, a);
	}

	public PackageBuilder withPackages(Class<?>... packagesForClasses) {
		return withPackages(packagesOf(packagesForClasses));
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
	 * Returns true for fields, methods, inner classes that have an
	 * in-scope {@link K9OnDiagram} annotation.
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
				if (on1.equals(creator.getDeclaringClass())) {
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

	/**
	 * Filters to just the items already on the diagram
	 */
	public Filter<Object> onlyOnDiagram() {
		return new Filter<Object>() {

			public boolean accept(Object o) {
				return isOnDiagram(o);
			}
		};
	}

	public boolean isOnDiagram(Object o) {
		return (getElement(o) != null) || symbols.containsKey(o);
	}

	/**
	 * Returns the opposite filter to the one entered
	 */
	public <X> Filter<X> not(final Filter<X> only) {
		return new Filter<X>() {
			public boolean accept(X o) {
				return !only.accept(o);
			}
		};
	}
	
	/**
	 * Helper interface for introduceContexts method, so you can describe exactly the style of context you want to create.
	 */
	public static interface ContextFactory {
		
		public Context createContextFor(Container subdivisionOf, List<Contained> contents, InsertionInterface ii);
		
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

	public Format asGlyphs() {
		return BasicFormats.asArrows(getInsertionInterface(), BasicFormats.asGlyph(null), null);
	}

	public Format asGlyphs(String stereotypeOverride) {
		return BasicFormats.asArrows(getInsertionInterface(), BasicFormats.asGlyph(stereotypeOverride), null);
	}

	public Format asGlyphs(String stereotypeOverride, Direction d) {
		return BasicFormats.asArrows(getInsertionInterface(), BasicFormats.asGlyph(stereotypeOverride), d);
	}

	public Format asContexts() {
		return BasicFormats.asArrows(getInsertionInterface(), BasicFormats.asContext(true, null, null), null);
	}

	public Format asContexts(boolean border, Layout l) {
		return BasicFormats.asArrows(getInsertionInterface(), BasicFormats.asContext(border, l, null), null);
	}

	public Format asContexts(boolean border, Layout l, Direction d) {
		return BasicFormats.asArrows(getInsertionInterface(), BasicFormats.asContext(border, l, null), d);
	}

	public Format asSymbols() {
		return BasicFormats.asSymbols(getInsertionInterface());
	}

	public Format asTextLines() {
		return BasicFormats.asTextLines(getInsertionInterface());
	}

	/**
	 * Returns the element in the diagram representing this object
	 */
	public DiagramElement getElement(Object o) {
		if ((!(o instanceof NounPart)) && (!(o instanceof NounRelationshipBinding))) {
			o = createNoun(o);
		}

		return contents.get(o);
	}

	public DiagramElement getRelationshipElement(Object o, Relationship r) {
		if (!(o instanceof NounPart)) {
			o = createNoun(o);
		}
		NounRelationshipBinding nrb = new NounRelationshipBinding((NounPart) o, r);
		return contents.get(nrb);
	}

	public Aliaser getAliaser() {
		return a;
	}

}
