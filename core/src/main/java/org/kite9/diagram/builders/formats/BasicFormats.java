package org.kite9.diagram.builders.formats;

import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.InsertionInterface;
import org.kite9.diagram.builders.NounRelationshipBinding;
import org.kite9.diagram.builders.Relationship;
import org.kite9.diagram.builders.Relationship.RelationshipType;
import org.kite9.diagram.builders.noun.AnnotatedNounPart;
import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.diagram.builders.noun.OwnedNoun;
import org.kite9.diagram.builders.noun.SimpleNoun;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.common.HelpMethods;
import org.kite9.framework.common.Kite9ProcessingException;

public class BasicFormats {

	public static SimpleNoun getUnderlyingSimpleNoun(NounPart in,
			InsertionInterface ii) {
		if (in == null)
			return null;

		if (in instanceof SimpleNoun) {
			return (SimpleNoun) in;
		}

		if (in instanceof OwnedNoun) {
			if (ii.returnExisting(((OwnedNoun) in).getOwned()) != null) {
				return ((OwnedNoun) in).getOwned();
			}

			return ((OwnedNoun) in).getOwner();
		}

		if (in instanceof AnnotatedNounPart) {
			return getUnderlyingSimpleNoun(
					((AnnotatedNounPart) in).getNounPart(), ii);
		}

		throw new Kite9ProcessingException("Can't process this noun" + in);

	}

	public static String getLabel(NounPart in, NounPart from,
			InsertionInterface ii) {
		if (in == from) {
			return "";
		}

		if (in instanceof OwnedNoun) {
			OwnedNoun on = (OwnedNoun) in;
			if (on.getOwner() == from) {
				return on.getOwned().getLabel();
			} else {
				return "";
			}
		}

		if (in instanceof AnnotatedNounPart) {
			AnnotatedNounPart annotatedNounPart = (AnnotatedNounPart) in;
			return annotatedNounPart.getPrefixAnnotation()
					+ getLabel(annotatedNounPart.getNounPart(), from, ii);
		}

		if (in instanceof SimpleNoun) {
			return ((SimpleNoun) in).getLabel();
		}

		throw new Kite9ProcessingException("Can't process this noun" + in);

	}

	/**
	 * Allows you to specify the style of a created connected element
	 */
	public interface ConnectedFormat {
		public Connected returnElement(Container c, SimpleNoun representing,
				InsertionInterface ii);
	}

	public static ConnectedFormat asGlyph(final String stereotypeOverride) {
		return new ConnectedFormat() {
			public Connected returnElement(Container c, SimpleNoun to,
					InsertionInterface ii) {
				Glyph de = (Glyph) ii.returnGlyph(c, to, to.getLabel(),
						stereotypeOverride == null ? to.getStereotype()
								: stereotypeOverride);

				if (to.getDisambiguation() != null) {
					for (NounPart item : to.getDisambiguation()) {
						asTextLines(ii).write(to, null, item);
					}

				}

				if (de instanceof Connected) {
					return (Connected) de;
				} else {
					throw new Kite9ProcessingException(
							"Was expecting a connected: " + de);
				}
			}
		};
	}

	public static ConnectedFormat asContext(final boolean border,
			final Layout d, final Label l) {
		return new ConnectedFormat() {
			public Connected returnElement(Container c, SimpleNoun to,
					InsertionInterface ii) {
				Label toUse = (l == null) ? (to.getLabel() == null ? null
						: new TextLine(to.getLabel())) : l;
				DiagramElement de = ii.returnContext(c, to, border ? toUse
						: null, border, d);
				if (de instanceof Connected) {
					return (Connected) de;
				} else {
					throw new Kite9ProcessingException(
							"Was expecting a connected: " + de);
				}
			}
		};
	}

	/**
	 * Creates a connection to represent the relationship to some connected item
	 */
	public static Format asConnected(final InsertionInterface ii,
			final ConnectedFormat toElementFormat, final Direction d) {
		return new Format() {
			public void write(NounPart subject, Relationship verb,
					NounPart object) {

				SimpleNoun from = getUnderlyingSimpleNoun(subject, ii);
				NounRelationshipBinding sr = new NounRelationshipBinding(
						subject, verb);

				SimpleNoun to = getUnderlyingSimpleNoun(object, ii);
				Container cont = ii.getContainerFor(from, verb);
				DiagramElement toEl = toElementFormat.returnElement(cont, to,
						ii);

				if ((from != null) && (verb != null)) {
					DiagramElement fromEl = ii.returnExisting(from);

					if ((fromEl instanceof Container)
							&& (((Container) fromEl).getContents()
									.contains(toEl))) {
						return;
					}

					if (fromEl == null)
						return;

					Relationship activeVerb = verb.getActiveRelationship();
					DiagramElement arrowEl = ii.returnArrow(cont, sr,
							(String) activeVerb.getObjectForAlias());
					String fromLabel = getLabel(subject, from, ii);
					TextLine fromLabelTL = fromLabel.length() == 0 ? null
							: new TextLine(fromLabel);
					String toLabel = getLabel(object, to, ii);
					TextLine toLabelTL = toLabel.length() == 0 ? null
							: new TextLine(toLabel);

					Direction direction = d == null ? activeVerb.getDirection()
							: d;
					if (verb.getType() == RelationshipType.PASSIVE) {
						ii.returnLink(toEl, arrowEl, toLabelTL, null, false,
								direction);
						ii.returnLink(arrowEl, fromEl, null, fromLabelTL, true,
								direction);
					} else if (verb.getType() == RelationshipType.ACTIVE) {
						ii.returnLink(fromEl, arrowEl, fromLabelTL, null,
								false, direction);
						ii.returnLink(arrowEl, toEl, null, toLabelTL, true,
								direction);
					} else {
						ii.returnLink(fromEl, arrowEl, fromLabelTL, null,
								false, direction);
						ii.returnLink(arrowEl, toEl, null, toLabelTL, false,
								direction);
					}
				}
			}
		};

	}

	/**
	 * Creates an arrow to represent the relationship
	 */
	public static Format asLink(final InsertionInterface ii,
			final ConnectedFormat toElementFormat) {
		return new Format() {
			public void write(NounPart subject, Relationship verb,
					NounPart object) {

				SimpleNoun from = getUnderlyingSimpleNoun(subject, ii);
				NounRelationshipBinding sr = new NounRelationshipBinding(from,
						verb);

				SimpleNoun to = getUnderlyingSimpleNoun(object, ii);
				Container cont = ii.getContainerFor(from, verb);
				DiagramElement toEl = toElementFormat.returnElement(cont, to,
						ii);

				if ((from != null) && (verb != null)) {
					DiagramElement fromEl = ii.returnExisting(from);

					if ((fromEl instanceof Container)
							&& (((Container) fromEl).getContents()
									.contains(toEl))) {
						return;
					}

					DiagramElement arrowEl = ii.returnArrow(cont, sr, verb
							.getActiveRelationship().getName());
					String fromLabel = getLabel(subject, from, ii);
					TextLine fromLabelTL = fromLabel == null ? null
							: new TextLine(fromLabel);
					String toLabel = getLabel(object, to, ii);
					TextLine toLabelTL = toLabel == null ? null : new TextLine(
							toLabel);

					if (verb.getType() == RelationshipType.PASSIVE) {
						ii.returnLink(toEl, arrowEl, toLabelTL, null, false,
								Direction.reverse(verb.getDirection()));
						ii.returnLink(arrowEl, fromEl, null, fromLabelTL, true,
								Direction.reverse(verb.getDirection()));
					} else if (verb.getType() == RelationshipType.ACTIVE) {
						ii.returnLink(fromEl, arrowEl, fromLabelTL, null,
								false, verb.getDirection());
						ii.returnLink(arrowEl, toEl, null, toLabelTL, true,
								verb.getDirection());
					} else {
						ii.returnLink(fromEl, arrowEl, fromLabelTL, null,
								false, verb.getDirection());
						ii.returnLink(arrowEl, toEl, null, toLabelTL, false,
								verb.getDirection());
					}
				}
			}
		};

	}

	public static Format asSymbols(final InsertionInterface ii) {
		return new Format() {

			public void write(NounPart context, Relationship key, NounPart value) {
				SimpleNoun from = getUnderlyingSimpleNoun(context, ii);
				DiagramElement de = ii.returnExisting(from);

				String fromLabel = getLabel(context, from, ii);
				String toLabel = getLabel(value, null, ii);
				String text = (fromLabel.length() == 0 ? "" : (fromLabel + " "))
						+ key.getName() + ": " + toLabel;
				NounRelationshipBinding srb = new NounRelationshipBinding(
						value, key);

				Symbol out = ii.returnSymbol(srb, text, toLabel);

				if (de instanceof TextLine) {
					((TextLine) de).getSymbols().add(out);
				} else if (de instanceof Glyph) {
					((Glyph) de).getSymbols().add(out);
				} else if (de instanceof Context) {
					Context ctx = (Context) de;
					if (ctx.getLabel() == null) {
						ctx.setLabel(new TextLine("", HelpMethods
								.createList(out)));
					} else {
						((TextLine) ctx.getLabel()).getSymbols().add(out);
					}
				} else {
					throw new Kite9ProcessingException(
							"No glyph/text line/context to add the symbol to");
				}
			}

		};
	}

	public static Format asTextLines(final InsertionInterface ii) {
		return new Format() {
			public void write(NounPart context, Relationship key, NounPart value) {
				SimpleNoun from = getUnderlyingSimpleNoun(context, ii);
				DiagramElement de = ii.returnExisting(context);
				if (de == null) {
					de = ii.returnExisting(from);
				}

				String fromLabel = getLabel(context, from, ii);
				String toLabel = getLabel(value, null, ii);
				String text = (fromLabel.length() == 0 ? "" : (fromLabel + " "))
						+ (key == null ? "" : key.getName() + ": ") + toLabel;

				if (de instanceof Glyph) {
					// add a text line to the glyph
					TextLine tl = ii.returnTextLine((Glyph) de, value, text);
					((Glyph) de).getText().add(tl);
				} else if (de instanceof TextLine) {
					// add further information to the text line
					TextLine tl = (TextLine) de;
					String text2 = tl.getText();
					text2 += ", " + text;
					tl.setText(text2);

					// contents.put(value, tl);
				} else {
					throw new Kite9ProcessingException(
							"Text line can only be added to existing text lines or glyphs: "
									+ de);
				}
			}
		};
	}

}
