package org.kite9.diagram.builders.formats;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.krmodel.AnnotatedNounPart;
import org.kite9.diagram.builders.krmodel.HasRelationship;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.NounRelationshipBinding;
import org.kite9.diagram.builders.krmodel.NounTools;
import org.kite9.diagram.builders.krmodel.OwnedNoun;
import org.kite9.diagram.builders.krmodel.PropositionBinding;
import org.kite9.diagram.builders.krmodel.Relationship;
import org.kite9.diagram.builders.krmodel.Relationship.RelationshipType;
import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.common.Kite9ProcessingException;

public class BasicFormats {

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

	public static NounFormat asGlyph(final String stereotypeOverride) {
		return new NounFormat() {
			public Connected returnElement(Container c, SimpleNoun to,
					InsertionInterface ii) {
				DiagramElement de = ii.returnGlyph(c, to, to.getLabel(),
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
	
	public static NounFormat asConnectionBody( ) {
		return new NounFormat() {
			public Connected returnElement(Container c, SimpleNoun to,
					InsertionInterface ii) {
				DiagramElement de = ii.returnConnectionBody(c, to, to.getLabel());

				if (de instanceof Connected) {
					return (Connected) de;
				} else {
					throw new Kite9ProcessingException(
							"Was expecting a connected: " + de);
				}
			}
		};
	}

	public static NounFormat asContext(final boolean border,
			final Layout d, final Label l) {
		return new NounFormat() {
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
	public static PropositionFormat asConnectionWithBody(final InsertionInterface ii, final NounFormat toElementFormat, final Direction d, final Container c) {
		return new PropositionFormat() {
			public void write(NounPart subject, Relationship verb,
					NounPart object) {

				// these are the knowledge items that will be represented in the format.
				SimpleNoun from = getExistingNounOnDiagram(subject, ii);
				NounRelationshipBinding sr = new NounRelationshipBinding(
						subject, verb);
				PropositionBinding or = new PropositionBinding(subject, verb, object);
				

				SimpleNoun to = NounTools.getRawSimpleNoun(object);
				Container cont = c == null ? getContainerFor(from, verb, ii) : c;
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
					boolean arrowPreExists = ii.returnExisting(sr) instanceof Arrow;
					
					DiagramElement arrowEl = ii.returnConnectionBody(cont, sr,
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
						ii.returnConnection(toEl, arrowEl, or, toLabelTL, null, false, direction);
						if (!arrowPreExists) {
							ii.returnConnection(arrowEl, fromEl, null, null, fromLabelTL, true, direction);
						}
					} else if (verb.getType() == RelationshipType.ACTIVE) {
						if (!arrowPreExists) {
							ii.returnConnection(fromEl, arrowEl, null, fromLabelTL, null, false, direction);
						}
						ii.returnConnection(arrowEl, toEl, or, null, toLabelTL, true, direction);
					} else {
						if (!arrowPreExists) {
							ii.returnConnection(fromEl, arrowEl, null, fromLabelTL, null, false, direction);
						}
						ii.returnConnection(arrowEl, toEl, or, null,  toLabelTL, false, direction);
					}
				}
			}

		};

	}
	

	private static SimpleNoun getExistingNounOnDiagram(NounPart subject, InsertionInterface ii) {
		if (ii.returnExisting(subject)!=null) {
			return (SimpleNoun) subject;
		} else if (subject instanceof SimpleNoun) {
			return null;
		} else if (subject instanceof AnnotatedNounPart) {
			return getExistingNounOnDiagram(((AnnotatedNounPart)subject).getNounPart(), ii);
		} else if (subject instanceof OwnedNoun) {
			SimpleNoun out = getExistingNounOnDiagram(((OwnedNoun)subject).getOwned(), ii);
			if (out != null) {
				return out;
			} else {
				return getExistingNounOnDiagram(((OwnedNoun)subject).getOwner(), ii);
			}
		} else {
			return null;
		}
	}

	public static PropositionFormat asSymbols(final InsertionInterface ii) {
		return new PropositionFormat() {

			public void write(NounPart context, Relationship key, NounPart value) {
				SimpleNoun from = NounTools.getRawSimpleNoun(context);
				DiagramElement de = ii.returnExisting(from);

				String fromLabel = getLabel(context, from, ii);
				String toLabel = getLabel(value, null, ii);
				String text = (fromLabel.length() == 0 ? "" : (fromLabel + " "))
						+ key.getName() + ": " + toLabel;
				PropositionBinding srb = new PropositionBinding(context, key, value);
				ii.returnSymbol(de, srb, text, toLabel);
			}

		};
	}

	public static PropositionFormat asTextLines(final InsertionInterface ii) {
		return new PropositionFormat() {
			public void write(NounPart context, Relationship key, NounPart value) {
				PropositionBinding pb = new PropositionBinding(context, key, value);
				SimpleNoun from = getExistingNounOnDiagram(context, ii);
				DiagramElement de = ii.returnExisting(from);

				String fromLabel = getLabel(context, from, ii);
				String toLabel = getLabel(value, null, ii);
				String text = (fromLabel.length() == 0 ? "" : (fromLabel + " "))
						+ (key == null ? "" : key.getName() + ": ") + toLabel;

				DiagramElement out = null;
				
				if (de instanceof Glyph) {
					// add a text line to the glyph
					out = ii.returnTextLine((Glyph) de, pb, text);
				} else if (de instanceof TextLine) {
					// add further information to the text line
					out = ii.extendTextLine((TextLine) de, pb, ", "+text); 
				} else {
					throw new Kite9ProcessingException(
							"Text line can only be added to existing text lines or glyphs: "
									+ de);
				}
				
				ii.mapExisting(value, out);
			}
		};
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
	public static Container getContainerFor(Object o, Relationship rel, InsertionInterface ii) {
		if (o == null)
			return null;

		DiagramElement within = ii.returnExisting(o);
		if (within == null) {
			// no context to place the element in, so put in the
			// diagram.
			return null;
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

}
