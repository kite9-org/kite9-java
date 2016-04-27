package org.kite9.diagram.builders.formats;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.AnnotatedNounPart;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.OwnedNoun;
import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;
import org.kite9.diagram.builders.krmodel.proposition.VerbObjectPair;
import org.kite9.diagram.builders.krmodel.verb.AbstractVerb;
import org.kite9.diagram.builders.krmodel.verb.ComposesVerb;
import org.kite9.diagram.builders.krmodel.verb.TypeVerb;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.diagram.builders.krmodel.verb.Verb.VerbType;
import org.kite9.diagram.builders.representation.ADLInsertionInterface;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.Label;
import org.kite9.diagram.primitives.StyledText;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * Various formatters for converting from KR to ADL.  These take a proposition and render it
 * onto the diagram in a given container.   If the elements already exist, then the information
 * is not represented again, but reused so that each element exists only once on the diagram.
 * 
 * 
 * @author robmoffat
 *
 */
public enum ADLFormat {

	CONNECTION_WITH_BODY(new AbstractFormat() {

		@Override
		public void write(SimpleRelationship p) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void write(NounPart np) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void write(NounPart subject, Verb verb, Knowledge object) {
			// TODO Auto-generated method stub
			
		}
		
			
	}), CONTEXT(new AbstractFormat() {

		@Override
		public void write(SimpleRelationship p) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void write(NounPart np) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void write(NounPart subject, Verb verb, Knowledge object) {
			// TODO Auto-generated method stub
			
		}
		
			
	}), GLYPH(new AbstractFormat() {

		@Override
		public void write(SimpleRelationship p) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void write(NounPart np) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void write(NounPart subject, Verb verb, Knowledge object) {
			// TODO Auto-generated method stub
			
		}
		
		
		
		
	});
	
	
	//, STEREOTYPE, TEXT_LINE, SYMBOL;

	
	ADLFormat(Format f) {
		this.f = f;
	}

	Format f;

	
	public Format getFormat(ADLInsertionInterface ii) {
		return f;
	}
	

	public static String getLabel(NounPart in, NounPart from, ADLInsertionInterface ii) {
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
	
	public static Format typeIgnored(ADLInsertionInterface ii, Format next) {
		return new Format() {
			
			@Override
			public void write(SimpleRelationship p) {
				if (p.getVerb() instanceof TypeVerb) {
					// ignore
				} else {
					next.write(p);
				}
				
			}
		};
	}
	
	public static Format typeAsStereotype(ADLInsertionInterface ii, Format next) {
		return new Format() {
			
			@Override
			public void write(SimpleRelationship p) {
				if (p.getVerb() instanceof TypeVerb) {
					NounPart subject = p.getSubject();
					DiagramElement c = ii.returnExisting(subject);
					
					if (c instanceof Glyph) {
						Knowledge object = p.getObject();
						((Glyph)c).setStereotype(new StyledText(object.getLabel()));
					} else {
						throw new Kite9ProcessingException(c+" is not a glyph, so I can't set stereotype on it");
					}
				} else if (next != null) {
					next.write(p);
				}
			}
		};
	}

	public static Format composesAsContainedGlyph(ADLInsertionInterface ii, Format next) {
		return new Format() {
			
			@Override
			public void write(SimpleRelationship p) {
				if (p.getVerb() instanceof ComposesVerb) {
					NounPart subject = p.getSubject();
					DiagramElement c = ii.returnExisting(subject);
					
					if (c instanceof Container) {
						Knowledge object = p.getObject();
						DiagramElement de = ii.returnGlyph((Container) c, object, object.getLabel(), null);
						return;
					}
				}
				
				next.write(p);
			}
		};
	}
	
	public static Format composesAsContainedContext(ADLInsertionInterface ii, Format next, Layout l) {
		return new Format() {
			
			@Override
			public void write(SimpleRelationship p) {
				if (p.getVerb() instanceof ComposesVerb) {
					NounPart subject = p.getSubject();
					DiagramElement c = ii.returnExisting(subject);
					
					if (c instanceof Container) {
						Knowledge object = p.getObject();
						DiagramElement de = ii.returnContext((Container) c, object, new TextLine(object.getLabel()), true, l);
						return;
					}
				}
				
				next.write(p);
			}
		};
	}
	
	public static Format subjectAsGlyph(final ADLInsertionInterface ii, Format next) {
		
		return new Format() {
			
			@Override
			public void write(SimpleRelationship p) {
				
				
				
			}
		}
	}
	
	public static Format relationshipsAsConnectionWithBodyBetweenGlyphs(final ADLInsertionInterface ii, Format next) {
		return new Format() {
			
			@Override
			public void write(SimpleRelationship p) {
				Verb v = p.getVerb();
				DiagramElement 
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

	public static Format asContext(final boolean border,
			final Layout d, final Label l) {
		return new Format() {
			public Connected returnElement(Container c, SimpleNoun to, ADLInsertionInterface ii) {
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
	public static Format asConnectionWithBody(final ADLInsertionInterface ii, final Format toElementFormat, final Direction d, final Container c) {
		return new AbstractFormat() {
			public void write(NounPart subject, AbstractVerb verb,
					NounPart object) {

				// these are the knowledge items that will be represented in the format.
				SimpleNoun from = getExistingNounOnDiagram(subject, ii);
				JavaPropositionBinding sr = new JavaPropositionBinding(subject, verb);
				JavaPropositionBinding or = new JavaPropositionBinding(subject, verb, object);
				

				SimpleNoun to = NounFactory.getRawSimpleNoun(object);
				Container cont = c == null ? getContainerFor(from, verb, ii) : c;
				DiagramElement toEl = toElementFormat.returnElement(cont, to,
						ii);

				if ((from != null) && (verb != null)) {
					DiagramElement fromEl = ii.returnExisting(from);

					if ((fromEl instanceof Container)
							&& (contains(toEl, fromEl))) {
						return;
					}

					if (fromEl == null)
						return;

					AbstractVerb activeVerb = verb.getActiveRelationship();
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
					if (verb.getType() == VerbType.PASSIVE) {
						ii.returnConnection(toEl, arrowEl, or, toLabelTL, null, false, direction);
						if (!arrowPreExists) {
							ii.returnConnection(arrowEl, fromEl, null, null, fromLabelTL, true, direction);
						}
					} else if (verb.getType() == VerbType.ACTIVE) {
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

			private boolean contains(DiagramElement toEl, DiagramElement fromEl) {
				for (Contained c : ((Container)fromEl).getContents()) {
					if (c==toEl) {
						return true;
					} else if ((c instanceof Container) && (contains(toEl, c))) {
						return true;
					}
				}
				
				return false;
			}

		};

	}
	

	private static SimpleNoun getExistingNounOnDiagram(NounPart subject, ADLInsertionInterface ii) {
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

	public static Format asSymbols(final ADLInsertionInterface ii) {
		return new Format() {

			public void write(SimpleRelationship sr) {
				NounPart on = sr.getSubject();
				Knowledge object = sr.getObject();
				DiagramElement de = ii.returnExisting(on);
				VerbObjectPair info = sr.createVerbObjectPair();
				
				ii.returnSymbol(de, info, info.getLabel(), object.getLabel());
			}

		};
	}

	public static Format asTextLines(final ADLInsertionInterface ii) {
		return new AbstractFormat() {
			public void write(NounPart context, AbstractVerb key, NounPart value) {
				JavaPropositionBinding pb = new JavaPropositionBinding(context, key, value);
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
	public static Container getContainerFor(Object o, Verb rel, ADLInsertionInterface ii) {
		if (o == null)
			return null;

		DiagramElement within = ii.returnExisting(o);
		if (within == null) {
			// no context to place the element in, so put in the
			// diagram.
			return null;
		}

		if (rel instanceof ComposesVerb) {
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
