package org.kite9.diagram.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.kite9.diagram.builders.formats.InsertionInterface;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.diagram.primitives.StyledText;
import org.kite9.diagram.primitives.SymbolTarget;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * Contains functionality to put together the diagram object model.
 * 
 * It keeps track of what items of knowledge have been added to the diagram, and
 * the diagram element representing that knowledge. The insertion interface is
 * used to specify this.
 * 
 * Note that this extends AbstractBuilder just for convenience later on.  It doesn't use 
 * any of its methods.
 */
public class BasicDiagramBuilder {

	public static final Object THE_DIAGRAM = new Object();
	protected IdHelper idHelper;
	protected KeyHelper kh = new KeyHelper();
	protected Diagram d;
	protected Map<Object, DiagramElement> contents = new HashMap<Object, DiagramElement>();
	private Map<String, Symbol> symbols = new HashMap<String, Symbol>();

	public BasicDiagramBuilder(String diagramId, IdHelper helper) {
		this.idHelper = helper;
		this.d = createRepresentation(diagramId);
	}

	public Diagram getDiagram() {
		addKey();
		return d;
	}

	private Diagram createRepresentation(String id) {
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
				d.getKey().setSymbols(Key.convert(ordered));
			}

		}
	}

	protected BasicDiagramBuilder withKeyText(String boldtext, String body) {
		Key k = new Key(boldtext, body, null);
		d.setKey(k);
		return this;
	}

	public String getId(Object o) {
		return idHelper.getId(o);
	}

	public InsertionInterface getInsertionInterface() {
		return new InsertionInterface() {

			public DiagramElement returnExisting(Object represented) {
				return contents.get(represented);
			}
			
			public void mapExisting(Object represented, DiagramElement de) {
				contents.put(represented, de);
			}

			public DiagramElement returnConnection(DiagramElement from,
					DiagramElement to, Object representing, Label fromLabel,
					Label toLabel, boolean arrowHead, Direction d) {
				DiagramElement out = representing == null ? null : contents
						.get(representing);

				if (out == null) {
					if ((from instanceof Connected)
							&& (to instanceof Connected)) {
						Connected cfrom = (Connected) from;
						Connected cto = (Connected) to;
						out = new Link(cfrom, cto, null, fromLabel,
								arrowHead ? LinkEndStyle.ARROW : null, toLabel,
								d);
					} else {
						throw new Kite9ProcessingException(
								"Could not link between: " + from + " and "
										+ to);
					}
				}

				contents.put(representing, out);
				return out;
			}

			/**
			 * Looks for an existing diagram element, or returns a new glyph
			 */
			public DiagramElement returnGlyph(Container within,
					Object representing, String label, String stereo) {
				DiagramElement out = representing == null ? null : contents
						.get(representing);
				if (out == null) {
					within = within == null ? getDiagram() : within;
					String id = idHelper.getId(representing);
					out = new Glyph(id, stereo, label, null, null);
					contents.put(representing, out);
					within.getContents().add((Glyph) out);
					((Glyph) out).setContainer(within);
				}
				return out;
			}

			public DiagramElement returnContext(Container within,
					Object representing, Label overrideLabel, boolean border,
					Layout d) {
				DiagramElement out = representing == null ? null : contents
						.get(representing);
				if (out == null) {
					within = within == null ? getDiagram() : within;
					String id = idHelper.getId(representing);
					out = new Context(id, null, border, border ? overrideLabel
							: null, d);
					contents.put(representing, out);
					within.getContents().add((Context) out);
					((Context) out).setContainer(within);
				}
				return out;
			}

			public DiagramElement returnConnectionBody(Container within,
					Object representing, String label) {
				DiagramElement out = representing == null ? null : contents
						.get(representing);
				if (out == null) {
					within = within == null ? getDiagram() : within;
					String id = idHelper.getId(representing);
					out = new Arrow(id, label);
					contents.put(representing, out);
					within.getContents().add((Arrow) out);
					((Arrow) out).setContainer(within);
				}
				return out;
			}

			public DiagramElement returnSymbol(DiagramElement container,
					Object representing, String text, String prefs) {
				DiagramElement out = representing == null ? null : contents
						.get(representing);
				if (out == null) {
					out = symbols.get(text);

					if (out == null) {
						Symbol s = kh.createSymbol(text, prefs);
						symbols.put(text, s);
						out = s;
					}

					SymbolTarget st = null;
					if (container instanceof Context) {
						st = (TextLine) ((Context) container).getLabel();
						if (st == null) {
							st = new TextLine();
							((Context) container).setLabel((TextLine) st);
						}
					} else if (container instanceof SymbolTarget) {
						st = (Glyph) container;
					} else if (container instanceof Link) {
						// add to a label, eventually.
					}

					// add the symbol to the container
					if ((st != null) && (!st.getSymbols().contains(out))) {
						st.getSymbols().add((Symbol) out);
					}
				}

				return out;
			}

			public DiagramElement returnTextLine(Glyph container,
					Object representing, String text) {
				DiagramElement out = representing == null ? null : contents
						.get(representing);
				if (out == null) {
					TextLine tl = new TextLine(text);
					contents.put(representing, tl);
					container.getText().add(tl);
					return tl;
				}
				return out;
			}

			public DiagramElement extendTextLine(TextLine container,
					Object representing, String text) {
				DiagramElement out = representing == null ? null : contents
						.get(representing);
				if (out == null) {
					StyledText st = container.getText();
					container.setText(new StyledText(st.getText() + text, st.getStyle()));
					contents.put(representing, container);
					return container;
				}
				return out;
			}

		};
	}

	public boolean isOnDiagram(Object o) {
		return contents.get(o) != null;
	}
	
}