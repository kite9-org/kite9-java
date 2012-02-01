package org.kite9.diagram.builders.wizards.sequence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.LinkLineStyle;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.WithHelperMethodsDiagramBuilder;
import org.kite9.diagram.builders.formats.InsertionInterface;
import org.kite9.diagram.builders.java.ProjectStaticSimpleNoun;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.IdentifiableDiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * This format shows the contexts aligned from left-to-right, and the glyphs
 * within the contexts aligned top-to-bottom in call order.
 * 
 * @author robmoffat
 * 
 */
public class ColumnSequenceDiagramWizard extends AbstractSequenceDiagramWizard {

	public ColumnSequenceDiagramWizard(WithHelperMethodsDiagramBuilder db) {
		super(db);
	}

	public ColumnSequenceDiagramWizard(InsertionInterface ii, NounFactory nf, Aliaser a) {
		super(ii, nf, a);
	}

	protected Layout getGlyphLayout() {
		return Layout.DOWN;
	}

	protected Layout getArrowDirection() {
		return Layout.RIGHT;
	}

	/**
	 * Formats the information in the provider, putting all of the created
	 * elements into the container.
	 */
	public void write(SequenceDiagramDataProvider provider, Container c) {
		Map<Object, DiagramElement> stateMap = new HashMap<Object, DiagramElement>();

		c.setLayoutDirection(Layout.HORIZONTAL);

		Map<DiagramElement, DiagramElement> lastArrows = new HashMap<DiagramElement, DiagramElement>();
		Map<DiagramElement, Integer> lengths = new HashMap<DiagramElement, Integer>();
		Set<DiagramElement> headerGlyphs = new HashSet<DiagramElement>();
		Map<DiagramElement, DiagramElement> rootElements = new HashMap<DiagramElement, DiagramElement>();
		Map<DiagramElement, Integer> elementDepth = new HashMap<DiagramElement, Integer>();
		Stack<DiagramElement> elementStack = new Stack<DiagramElement>();

		for (Step s : provider.getSteps()) {
			System.out.println(s);
			if (s instanceof CallStep) {
				CallStep cs = (CallStep) s;
				DiagramElement fromg = elementStack.size()==0 ? null : elementStack.peek();
				DiagramElement from = fromg == null ? null : getExtensionElement(((Contained)fromg).getContainer(), fromg, lastArrows, lengths, true);
				DiagramElement tog = buildGlyph(c, cs.getTo(), cs.getToGroup(), stateMap, headerGlyphs, rootElements, elementStack, elementDepth);
				
				if (from != null) {
					DiagramElement to  = getExtensionElement(((Contained)tog).getContainer(), tog, lastArrows, lengths, false);
					Direction d = getDirectionFor(getArrowDirection());
					boolean fromBefore = isFromBefore2(rootElements, elementDepth, fromg, tog);
					if (!fromBefore) {
						d = Direction.reverse(d);
					}
					createLink(s, from, to, d);
				}
				elementStack.push(tog);
			} else if (s instanceof ReturnStep) {
				DiagramElement fromg = elementStack.pop();
				if (((ReturnStep)s).isShow()) {
					DiagramElement from = getExtensionElement(((Contained)fromg).getContainer(), fromg, lastArrows, lengths, true);
					DiagramElement tog = elementStack.peek();
					DiagramElement to  = getExtensionElement(((Contained)tog).getContainer(), tog, lastArrows, lengths, false);
					Direction d = getDirectionFor(getArrowDirection());
					
					boolean fromBefore = isFromBefore2(rootElements, elementDepth, fromg, tog);
					if (!fromBefore) {
						d = Direction.reverse(d);
					}
	
					createLink(s, from, to, d);
				}
			}
		}
		
		// join up container contents
		Connected last = null;
		for (Contained c2 : c.getContents()) {
			if (c2 instanceof Container) {
				for (Contained c3 : ((Container) c2).getContents()) {
					if ((c3 instanceof Connected) && (headerGlyphs.contains(c3))) {
						if (last != null) {
							Link l = new Link(last, (Connected) c3, null, null, null, null, getDirectionFor(getArrowDirection()));
							l.setStyle(LinkLineStyle.INVISIBLE);
						}
						last = (Connected) c3;
					}
				}
			}
		}
	}

	private boolean isFromBefore2(Map<DiagramElement, DiagramElement> rootElements, Map<DiagramElement, Integer> elementDepth, DiagramElement fromg,
			DiagramElement tog) {
		DiagramElement fromr = rootElements.get(fromg);
		DiagramElement tor = rootElements.get(tog);
		if (fromr==tor) {
			// sub-call
			Integer fromd = elementDepth.get(fromg);
			fromd = fromd == null ? 0 : fromd;
			Integer tod = elementDepth.get(tog);
			tod = tod == null ? 0 : tod;
			return fromd.compareTo(tod)==-1;
			
		} else {
			boolean fromBefore = isFromBefore(fromr, depth(fromr), tor, depth(tor));
			return fromBefore;
		}
	}


	private int depth(DiagramElement de) {
		if (de instanceof Contained) {
			Contained c = (Contained) de;
			Container cc = c.getContainer();
			if (cc == null) { 
				return 0;
			} else {
				return depth(cc)+1;
			}
		} else {
			return 0;
		}
	}
	
	private boolean isFromBefore(DiagramElement from, int dFrom, DiagramElement to, int dTo) {
		while (dFrom > dTo) {
			from = ((Contained)from).getContainer() ;
			dFrom --;
		}
		
		while (dTo > dFrom) {
			to = ((Contained)to).getContainer() ;
			dTo --;
		}
		
		Container cf = ((Contained)from).getContainer();
		Container ct = ((Contained)to).getContainer();
		if (cf == ct) {
			return cf.getContents().indexOf(from) < cf.getContents().indexOf(to);
		} else {
			return isFromBefore(cf, dFrom-1, ct, dTo-1);
		}
	}
	
	int arrowId = 0;
	int ownsId = 0;

	private DiagramElement getExtensionElement(Container c, DiagramElement glyph,
			Map<DiagramElement, DiagramElement> lastArrows,
			Map<DiagramElement, Integer> lengths, boolean active) {

		DiagramElement existing = lastArrows.get(glyph) == null ? glyph : lastArrows.get(glyph);
		if ((existing instanceof Arrow) && (((Arrow)existing).getLinks().size()==0)) {
			// this one will do anyway
			return existing;
		}
		int newLength = lengths.get(glyph) == null ? 0 : lengths.get(glyph);

		DiagramElement extension = ii.returnConnectionBody(c, new ProjectStaticSimpleNoun(((IdentifiableDiagramElement)glyph).getID()+newLength, a), "");
		lengths.put(glyph, newLength + 1);
		lastArrows.put(glyph, extension);
		DiagramElement de = ii.returnConnection(existing, extension, null, null, null, false, getDirectionFor(getGlyphLayout()));

		if ((!active) && (de instanceof Link)) {
			((Link)de).setStyle(LinkLineStyle.DOTTED);
		}
		
		return extension;
	}

	private DiagramElement buildGlyph(Container c, SimpleNoun from, SimpleNoun fromGroup, Map<Object, DiagramElement> stateMap, Set<DiagramElement> headerGlyphs, Map<DiagramElement, DiagramElement> rootElements, Stack<DiagramElement> elementStack, Map<DiagramElement, Integer> elementDepth) {
		DiagramElement existing = stateMap.get(from);
		Container container = (Container) stateMap.get(fromGroup);
		
		if (container==null) {
			DiagramElement de = ii.returnContext(c, fromGroup, new TextLine(fromGroup.getLabel()), true, null);
			if (!(de instanceof Context)) {
				throw new Kite9ProcessingException(fromGroup + " already exists in the diagram as a " + de.getClass()
						+ " (Context needed)");
			}
			stateMap.put(fromGroup, de);
			container = (Container) de;
		}

		if (existing != null) {
			if (elementStack.contains(existing)) {
				// need to create an arrow, as this is a
				// second/nth call
				DiagramElement out = ii.returnConnectionBody(container, new ProjectStaticSimpleNoun(from.getRepresented()+" SL="+elementStack.size(), a), "");

				// ensure ordering makes some kind of sense
				rootElements.put(out, existing);
				elementDepth.put(out, elementStack.size());		
				return out;
			} else {
				return existing;
			}
		} else {
			DiagramElement out = ii.returnGlyph(container, from, from.getLabel(), from.getStereotype());
			stateMap.put(from, out);
			headerGlyphs.add(out);
			rootElements.put(out, out);
			return out;
		}
	}

	protected void createLink(Step s, DiagramElement from, DiagramElement to, Direction d) {
		DiagramElement out = ii.returnConnection(from, to, s, null, null, true, d);
		System.out.println("Link from " + from + " to " + to + " in " + d);
		
		if (out instanceof Link) {
			Link l = (Link) out;
			Label fromLabel = buildFromLabel(s, l.getFromLabel());
			Label toLabel = buildToLabel(s, l.getToLabel());
			l.setFromLabel(fromLabel);
			l.setToLabel(toLabel);
		}

	}
}
