package org.kite9.framework.serialization;

import java.awt.Dimension;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.CompositionalShape;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Key;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.LinkEndStyle;
import org.kite9.diagram.adl.Symbol;
import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.position.CostedDimension;
import org.kite9.diagram.position.DiagramRenderingInformation;
import org.kite9.diagram.position.Dimension2D;
import org.kite9.diagram.position.RectangleRenderingInformation;
import org.kite9.diagram.position.RenderingInformation;
import org.kite9.diagram.position.RouteRenderingInformation;
import org.kite9.diagram.primitives.AbstractConnectedContained;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Connection;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.primitives.HintMap;
import org.kite9.diagram.primitives.IdentifiableDiagramElement;
import org.kite9.diagram.primitives.StyledText;
import org.kite9.diagram.visitors.ContainerVisitor;
import org.kite9.framework.server.BasicWorkItem;
import org.kite9.framework.server.WorkItem;

import com.thoughtworks.xstream.MarshallingStrategy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByIdUnmarshaller;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.core.util.ObjectIdDictionary;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.path.Path;
import com.thoughtworks.xstream.io.path.PathTracker;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;
import com.thoughtworks.xstream.io.xml.AbstractPullReader;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxReader;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Utility methods for converting to and from XML in the expected format. This
 * uses XStream under the hood to do the conversion.
 * 
 * This copy exists here because we need it for testing.
 * 
 * This provides the following functionality:
 * <ul>
 * <li>Object-reference fixing so that we can omit parent/container references
 * in the xml</li>
 * <li>Use of kite9 namespace for xml generated</li>
 * <li>use of Kite9 id field in the xml, instead of Xstream generated ones.</li>
 * <li>Use of xsi:type to choose the subclass in the xml format (in accordance
 * with schema)</li>
 * 
 * @author robmoffat
 * 
 */
public class XMLHelper {

	public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String KITE9_NAMESPACE = "http://www.kite9.org/schema/adl";

	public static final Class<?>[] ADL_CLASSES = new Class[] { Arrow.class, Context.class, Diagram.class, Glyph.class,
			StyledText.class, Key.class, Link.class, TextLine.class, TextLine.class, Symbol.class, LinkEndStyle.class,
			CompositionalShape.class, BasicWorkItem.class, Dimension2D.class, RouteRenderingInformation.class,
			DiagramRenderingInformation.class, RectangleRenderingInformation.class, CostedDimension.class };

	static class Field {

		private Class<?> c;
		private String f;

		public Field(Class<?> c, String f) {
			super();
			this.c = c;
			this.f = f;
		}

	}

	public static final Field[] OMISSIONS = new Field[] { new Field(Dimension.class, "width"),
			new Field(Dimension.class, "height") };

	private boolean simplifyingXML = true;

	/**
	 * When this is set, we serialize all of the link elements as part of the
	 * Diagram element. This makes the containment structure of the ADL elements
	 * much easier to see.
	 * 
	 * @return
	 */
	public boolean isSimplifyingXML() {
		return simplifyingXML;
	}

	public void setSimplifyingXML(boolean simplifyingXML) {
		this.simplifyingXML = simplifyingXML;
	}

	public XMLHelper() {
	}

	private XStream xs;

	public synchronized XStream buildXStream() {
		if (xs == null) {
			XStream xstream = new XStream(new PureJavaReflectionProvider(), new XMLHelper.SchemaTypeUsingStaxProvider());
			xstream.processAnnotations(ADL_CLASSES);
			addOmissions(xstream);
			xstream.setMarshallingStrategy(new IDSuppliedMarshallingStrategy());
			if (isSimplifyingXML()) {
				xstream.omitField(AbstractConnectedContained.class, "links");
			}

			// handle styled text correctly
			xstream.registerConverter(new Converter() {

				public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
					return type.equals(StyledText.class);
				}

				public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
					StyledText st = (StyledText) source;
					if (st.getStyle() != null) {
						writer.addAttribute("style", st.getStyle());
					}
					writer.setValue(st.getText());
				}

				public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
					String style = reader.getAttribute("style");
					String text = reader.getValue();
					return new StyledText(text, style);
				}
			});

			// this makes it so that if the type is not specified in the xml, we
			// assume a string.
			xstream.registerConverter(new StringConverter() {

				@Override
				public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
					return super.canConvert(type) || type.equals(Object.class);
				}

			}, 10);

			// this compacts the representation of a hint map, which is
			// otherwise excessive
			xstream.registerConverter(new Converter() {

				public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
					HintMap map = (HintMap) source;
					for (Iterator<Entry<String, Float>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
						Entry<String, Float> entry = iterator.next();
						writer.addAttribute(entry.getKey().toString(), entry.getValue().toString());
					}
				}

				@SuppressWarnings("unchecked")
				public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
					HintMap properties = new HintMap();
					for (Iterator iterator = reader.getAttributeNames(); iterator.hasNext();) {
						String n = (String) iterator.next();
						String v = reader.getAttribute(n);
						Float f = Float.parseFloat(v);
						properties.put(n, f);
					}

					return properties;
				}
				
				@SuppressWarnings("unchecked") 
				public boolean canConvert(Class type) {
					return type == HintMap.class;
				}

			});

			// allow labels/stereotypes to appear in attributes.
			xstream.registerConverter(new ReflectionConverter(xstream.getMapper(), xstream.getReflectionProvider()) {

				@Override
				public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
					return type.equals(Glyph.class) || type.equals(Arrow.class);
				}

				@Override
				public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
					String attLabel = reader.getAttribute("label");
					String attStereo = reader.getAttribute("stereotype");
					Object out = super.unmarshal(reader, context);

					if (out instanceof Glyph) {
						if (((Glyph) out).getLabel() == null) {
							((Glyph) out).setLabel(new StyledText(attLabel));
						}

						if (((Glyph) out).getStereotype() == null) {
							((Glyph) out).setStereotype(new StyledText(attStereo));
						}

					} else if (out instanceof Arrow) {
						if (((Arrow) out).getLabel() == null) {
							((Arrow) out).setLabel(new StyledText(attLabel));
						}
					}

					return out;
				}

			});
			xs = xstream;
		}

		return xs;
	}

	private void addOmissions(XStream xstream) {
		for (int i = 0; i < OMISSIONS.length; i++) {
			xstream.omitField(OMISSIONS[i].c, OMISSIONS[i].f);
		}
	}

	public String toXML(Diagram d) {
		XStream xstream = buildXStream();
		handlePreProcessing(d);
		return xstream.toXML(d);
	}

	public String toXML(WorkItem item) {
		XStream xstream = buildXStream();
		handlePreProcessing(item.getDesignItem());

		return xstream.toXML(item);
	}

	public Object fromXML(String s) {
		Object out = buildXStream().fromXML(s);
		handlePostProcessing(out);
		return out;
	}

	public Object fromXML(InputStream s) {
		Object out = buildXStream().fromXML(s);
		handlePostProcessing(out);
		return out;
	}

	private void preProcess(final Diagram d) {
		d.getAllLinks().clear();

		if (isSimplifyingXML()) {

			new ContainerVisitor() {

				@Override
				protected void containerStart(Container c) {
				}

				@Override
				protected void containerEnd(Container c) {
				}

				@Override
				protected void contained(Contained c) {
					if (c instanceof Connected) {
						d.getAllLinks().addAll(((Connected) c).getLinks());
					}
				}

			}.visit(d);

		}
	}

	protected void handlePreProcessing(Object in) {
		if (in instanceof WorkItem) {
			if (((WorkItem) in).getDesignItem() instanceof Diagram) {
				preProcess((Diagram) ((WorkItem) in).getDesignItem());
			}
		} else if (in instanceof Diagram) {
			preProcess((Diagram) in);
		}
	}

	protected void handlePostProcessing(Object out) {
		if (out instanceof WorkItem) {
			if (((WorkItem) out).getDesignItem() instanceof Diagram) {
				postProcess((Diagram) ((WorkItem) out).getDesignItem(), null);
			}
		} else if (out instanceof Diagram) {
			postProcess((Diagram) out, null);
		}
	}

	/**
	 * This handles the case when an xml graph is entered sparsely, and values
	 * must be implied from the structure.
	 */
	private void postProcess(DiagramElement diag, DiagramElement parent) {
		if (diag instanceof Diagram) {
			int rank = 0;
			for (Iterator<Connection> iterator = ((Diagram) diag).getAllLinks().iterator(); iterator.hasNext();) {
				Connection l = (Connection) iterator.next();
				if ((l.getFrom() != NO_REF) && (l.getTo() != NO_REF)) {
					l.getFrom().getLinks().add(l);
					l.getTo().getLinks().add(l);
					if (l instanceof Link) {
						((Link) l).setRank(rank++);
					}
				} else {
					iterator.remove();
				}
			}
		}

		if (diag instanceof Contained) {
			((Contained) diag).setContainer((Container) parent);
		}

		if (diag instanceof CompositionalDiagramElement) {
			((CompositionalDiagramElement) diag).setParent(parent);
		}

		if (diag instanceof Link) {
			Link il = (Link) diag;
			if (il.getFrom() == null) {
				il.setFrom((Connected) parent);
			} else {
				ensureLink(il.getFrom(), il);
			}

			if (il.getTo() == null) {
				il.setTo((Connected) parent);
			} else {
				ensureLink(il.getTo(), il);
			}

			postProcess(il.getFromLabel(), il);
			postProcess(il.getToLabel(), il);
		}

		if (diag instanceof Glyph) {
			for (CompositionalDiagramElement c : ((Glyph) diag).getText()) {
				postProcess(c, diag);
			}
		}

		if (diag instanceof Container) {
			Collection<Contained> content = ((Container) diag).getContents();
			if (content != null) {
				for (Contained c : content) {
					postProcess(c, diag);
				}
			}
			postProcess(((Container) diag).getLabel(), diag);
		}
		if (diag instanceof Connected) {
			for (Iterator<Connection> lc = ((Connected) diag).getLinks().iterator(); lc.hasNext();) {
				Connection c = (Connection) lc.next();
				if ((c.getFrom() == NO_REF) || (c.getTo() == NO_REF)) {
					lc.remove();
				} else {
					postProcess(c, diag);
				}
			}
		}
	}

	private void ensureLink(Connected from, Link il) {
		for (Connection i : from.getLinks()) {
			if (i == il) {
				return;
			}
		}

		from.addLink(il);
	}

	/**
	 * Enforces the use of xsi:type to identify between different xml entities,
	 * rather than the xstream default, 'class'. Also pretty-prints the xml
	 * produced on output to improve legibility in test results.
	 * 
	 * @author moffatr
	 * 
	 */
	protected static class SchemaTypeUsingStaxProvider extends StaxDriver {

		public SchemaTypeUsingStaxProvider() {
			super();
			map = new QNameMap();
			map.setDefaultNamespace(KITE9_NAMESPACE);
		}

		private QNameMap map;

		@Override
		public StaxWriter createStaxWriter(final XMLStreamWriter out, boolean writeStartEndDocument)
				throws XMLStreamException {

			return new StaxWriter(map, out, writeStartEndDocument, isRepairingNamespace(), xmlFriendlyReplacer()) {
				int indent = 0;
				boolean justStarted = false;

				public void startNode(String name) {
					if (indent > 0)
						newLine();

					super.startNode(name);
					if (indent == 0) {
						try {
							out.writeNamespace("xsi", XML_SCHEMA_NAMESPACE);
						} catch (XMLStreamException e) {
							throw new StreamException(e);
						}
					}
					indent++;
					justStarted = true;
				}

				@Override
				public void endNode() {
					indent--;

					if (!justStarted) {
						newLine();
					}
					justStarted = false;
					super.endNode();
				}

				@Override
				public void addAttribute(String name, String value) {
					if (name.equals("class")) {
						if (!value.equals("string")) {
							super.addAttribute("xsi:type", value);
						}
					} else {
						super.addAttribute(name, value);
					}
				}

				protected void newLine() {
					try {
						out.writeCharacters("\n");
						for (int i = 0; i < indent; i++) {
							out.writeCharacters(" ");
						}
					} catch (XMLStreamException e) {
						throw new StreamException(e);
					}

				}
			};
		}

		@Override
		public AbstractPullReader createStaxReader(final XMLStreamReader in) {

			return new StaxReader(map, in) {

				@Override
				public String getAttribute(String name) {
					if (name.equals("class")) {
						return in.getAttributeValue(XML_SCHEMA_NAMESPACE, "type");
					} else {
						return super.getAttribute(name);
					}
				}
			};
		}

	}

	private static final Connected NO_REF = new AbstractConnectedContained() {

		private static final long serialVersionUID = -8145997830419925869L;

		public void setRenderingInformation(RenderingInformation ri) {
		}

		public RenderingInformation getRenderingInformation() {
			return null;
		}
	};

	protected class IDSuppliedMarshallingStrategy implements MarshallingStrategy {

		public Object unmarshal(Object root, HierarchicalStreamReader reader, DataHolder dataHolder,
				ConverterLookup converterLookup, Mapper mapper) {
			return new ReferenceByIdUnmarshaller(root, reader, converterLookup, mapper) {

				@SuppressWarnings("unchecked")
				@Override
				protected Object convert(Object parent, Class type, Converter converter) {
					try {
						return super.convert(parent, type, converter);
					} catch (ConversionException ce) {
						System.err.println("Couldn't convert: " + type);
						ce.printStackTrace();
						return NO_REF;
					}
				}

			}.start(dataHolder);
		}

		public void marshal(HierarchicalStreamWriter writer, Object obj, ConverterLookup converterLookup,
				Mapper mapper, DataHolder dataHolder) {
			new ReferenceByNameMarshaller(writer, converterLookup, mapper).start(obj, dataHolder);
		}

		/**
		 * @deprecated As of 1.2, use
		 *             {@link #unmarshal(Object, HierarchicalStreamReader, DataHolder, ConverterLookup, Mapper)}
		 */
		public Object unmarshal(Object root, HierarchicalStreamReader reader, DataHolder dataHolder,
				DefaultConverterLookup converterLookup, com.thoughtworks.xstream.alias.ClassMapper classMapper) {
			return unmarshal(root, reader, dataHolder, (ConverterLookup) converterLookup, (Mapper) classMapper);
		}

		/**
		 * @deprecated As of 1.2, use
		 *             {@link #marshal(HierarchicalStreamWriter, Object, ConverterLookup, Mapper, DataHolder)}
		 */
		public void marshal(HierarchicalStreamWriter writer, Object obj, DefaultConverterLookup converterLookup,
				com.thoughtworks.xstream.alias.ClassMapper classMapper, DataHolder dataHolder) {
			marshal(writer, obj, converterLookup, (Mapper) classMapper, dataHolder);
		}
	}

	class ReferenceByNameMarshaller extends TreeMarshaller {

		private ObjectIdDictionary references = new ObjectIdDictionary();
		private ObjectIdDictionary implicitElements = new ObjectIdDictionary();
		private PathTracker pathTracker = new PathTracker();
		private Path lastPath;
		private Set<String> usedIDs = new HashSet<String>(200);
		private int nextIntId = 0;

		public ReferenceByNameMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
			super(writer, converterLookup, mapper);
			this.writer = new PathTrackingWriter(writer, pathTracker);
		}

		public void convert(Object item, Converter converter) {
			// System.out.println("Creating reference for item= "+item);
			// System.out.println("item class loader: "+item.getClass().getClassLoader());
			// System.out.println("diagram class loader: "+Diagram.class.getClassLoader());
			//			

			if (getMapper().isImmutableValueType(item.getClass())) {
				// strings, ints, dates, etc... don't bother using references.
				converter.marshal(item, writer, this);
			} else if (!(item instanceof IdentifiableDiagramElement)) {
				// we're only going to allow references on non-identifiable
				// diagram elements
				converter.marshal(item, writer, this);
			} else {
				Path currentPath = pathTracker.getPath();
				Object existingReferenceKey = references.lookupId(item);
				if (existingReferenceKey != null) {
					writer.addAttribute(getMapper().aliasForAttribute("reference"), createReference(currentPath,
							existingReferenceKey));
				} else if (implicitElements.lookupId(item) != null) {
					throw new ReferencedImplicitElementException(item, currentPath);
				} else {
					String newReferenceKey = null;
					boolean fire = false;
					if (item instanceof IdentifiableDiagramElement) {
						newReferenceKey = ensureUniqueness((IdentifiableDiagramElement) item);
					} else {
						newReferenceKey = makeUnique("" + nextIntId++);
						fire = true;
					}

					if (lastPath == null || !currentPath.isAncestor(lastPath)) {
						if (fire) {
							fireValidReference(newReferenceKey);
						}
						lastPath = currentPath;
						if (usedIDs.contains(newReferenceKey)) {
							throw new ConversionException("ID is used by more than one diagram element: "
									+ newReferenceKey + " for " + item);
						}

						usedIDs.add(newReferenceKey);
						references.associateId(item, newReferenceKey);
					} else {
						implicitElements.associateId(item, newReferenceKey);
					}
					converter.marshal(item, writer, this);
				}
			}

		}

		private String ensureUniqueness(IdentifiableDiagramElement item) {
			String currentId = item.getID();
			currentId = makeUnique(currentId);
			item.setID(currentId);
			return currentId;
		}

		private String makeUnique(String id) {
			String suffix = "";
			int i = 1;
			while (usedIDs.contains(id + suffix)) {
				suffix = "." + i++;
			}

			return id + suffix;
		}

		protected void fireValidReference(Object referenceKey) {
			writer.addAttribute(getMapper().aliasForAttribute("id"), referenceKey.toString());
		}

		protected String createReference(Path currentPath, Object existingReferenceKey) {
			return (String) existingReferenceKey;
		}

		protected Object createReferenceKey(IdentifiableDiagramElement item) {
			return item.getID();
		}
	}

	static class ReferencedImplicitElementException extends ConversionException {

		private static final long serialVersionUID = 7354329044723095330L;

		/**
		 * @deprecated since 1.2.1
		 */
		public ReferencedImplicitElementException(final String msg) {
			super(msg);
		}

		public ReferencedImplicitElementException(final Object item, final Path path) {
			super("Cannot reference implicit element");
			add("implicit-element", item.toString());
			add("referencing-element", path.toString());
		}
	}
}
