package org.kite9.diagram.builders.wizards.sequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.WithHelperMethodsDiagramBuilder;
import org.kite9.diagram.builders.formats.InsertionInterface;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.alias.Aliaser;

public class NoLayoutSequenceDiagramWizard extends AbstractSequenceDiagramWizard {

	public NoLayoutSequenceDiagramWizard(WithHelperMethodsDiagramBuilder db) {
		super(db);
	}

	private int stepNumber;

	/**
	 * Formats the information in the provider, putting all of the created
	 * elements into the container.
	 */
	public void write(SequenceDiagramDataProvider provider, Container c) {
		Map<Object, DiagramElement> stateMap = new HashMap<Object, DiagramElement>();
		Stack<DiagramElement> elementStack = new Stack<DiagramElement>();

		for (Step s : provider.getSteps()) {
			System.out.println(s);
			if (s instanceof CallStep) {
				CallStep cs = (CallStep) s;
				DiagramElement from = elementStack.size() == 0 ? null : elementStack.peek();
				DiagramElement to = buildGlyph(c, cs.getTo(), cs.getToGroup(), stateMap, elementStack);

				if (from != null) {
					createLink(s, from, to, null);
				}
				elementStack.push(to);
			} else if (s instanceof ReturnStep) {
				DiagramElement from = elementStack.pop();
				if (((ReturnStep) s).isShow()) {
					DiagramElement to = elementStack.peek();
					createLink(s, from, to, null);
				}
			}
		}
	}

	@Override
	protected Label buildFromLabel(Step s, Label existing) {
		if (s instanceof CallStep) {
			Label out = super.buildFromLabel(s, existing);
			String stepNoText = "" + stepNumber + ", ";
			if (out == null) {
				out = new TextLine(stepNoText);
			} else if (out instanceof TextLine) {
				TextLine tl = (TextLine) out;
				tl.setText(stepNoText + tl.getText());
			}
			stepNumber++;
			return out;
		} else {
			return existing;
		}
	}

	public NoLayoutSequenceDiagramWizard(InsertionInterface ii, NounFactory nf, Aliaser a) {
		super(ii, nf, a);
	}

	private DiagramElement buildGlyph(Container c, SimpleNoun from, SimpleNoun fromGroup,
			Map<Object, DiagramElement> stateMap, Stack<DiagramElement> elementStack) {
		DiagramElement existing = stateMap.get(from);
		if (existing != null) {
			return existing;
		}

		Container container = c;
		if (fromGroup != null) {
			container = (Container) stateMap.get(fromGroup);
			if (container == null) {
				container = (Container) ii.returnContext(c, fromGroup, new TextLine(fromGroup.getLabel()), true, null);
				stateMap.put(fromGroup, container);
			}
		}

		if (existing == null) {
			DiagramElement out = ii.returnGlyph(container, from, from.getLabel(), from.getStereotype());
			existing = out;
		} 
		
		return existing;
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
