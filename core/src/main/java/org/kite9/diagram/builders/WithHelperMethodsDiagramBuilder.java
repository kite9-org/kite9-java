package org.kite9.diagram.builders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.kite9.diagram.adl.Context;
import org.kite9.diagram.builders.formats.BasicFormats;
import org.kite9.diagram.builders.formats.InsertionInterface;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;

/**
 * This includes some as... methods which provide some simple functionality to help you create basic layouts.
 * 
 * Also includes some functionality to allow you to subdivide the diagram up with contexts.
 * 
 * @author robmoffat
 *
 */
public class WithHelperMethodsDiagramBuilder extends BasicDiagramBuilder {

	public WithHelperMethodsDiagramBuilder(String diagramId, IdHelper helper) {
		super(diagramId, helper);
	}

	/**
	 * Helper interface for introduceContexts method, so you can describe exactly the style of context you want to create.
	 */
	public static interface ContextFactory {
		
		public Context createContextFor(Container subdivisionOf, List<Contained> contents, InsertionInterface ii);
		
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
	 * Returns only elements matching both criteria
	 */
	public <X> Filter<X> and(final Filter<X> a, final Filter<X> b) {
		return new Filter<X>() {
			public boolean accept(X o) {
				return a.accept(o) && b.accept(o);
			}
		};
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

	public PropositionFormat asConnectedGlyphs() {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asGlyph(null), null, null);
	}

	public PropositionFormat asConnectedGlyphs(String stereotypeOverride) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asGlyph(stereotypeOverride), null, null);
	}

	public PropositionFormat asConnectedGlyphs(String stereotypeOverride, Direction d) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asGlyph(stereotypeOverride), d, null);
	}

	public PropositionFormat asConnectedContexts() {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asContext(true, null, null), null, null);
	}

	public PropositionFormat asConnectedContexts(boolean border, Layout l) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asContext(border, l, null), null, null);
	}

	public PropositionFormat asConnectedContexts(boolean border, Layout l, Direction d) {
		return BasicFormats.asConnectionWithBody(getInsertionInterface(), BasicFormats.asContext(border, l, null), d, null);
	}

	public PropositionFormat asSymbols() {
		return BasicFormats.asSymbols(getInsertionInterface());
	}

	public PropositionFormat asTextLines() {
		return BasicFormats.asTextLines(getInsertionInterface());
	}

}