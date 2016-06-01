package org.kite9.framework.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.util.SAXDocumentFactory;
import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.CompositionalShape;
import org.kite9.diagram.adl.ContainerProperty;
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
import org.kite9.diagram.position.RouteRenderingInformation;
import org.kite9.diagram.primitives.CompositionalDiagramElement;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Connection;
import org.kite9.diagram.primitives.Contained;
import org.kite9.diagram.primitives.Container;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.diagram.visitors.ContainerVisitor;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.server.BasicWorkItem;
import org.kite9.framework.server.WorkItem;
import org.w3c.dom.Document;

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
			Key.class, Link.class, TextLine.class, TextLine.class, Symbol.class, LinkEndStyle.class,
			CompositionalShape.class, BasicWorkItem.class, Dimension2D.class, RouteRenderingInformation.class,
			DiagramRenderingInformation.class, RectangleRenderingInformation.class, CostedDimension.class };

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

	public String toXML(Diagram d) {
		try {
			 preProcess(d);
			 TransformerFactory transfac = TransformerFactory.newInstance();
			 Transformer trans = transfac.newTransformer();
			 trans.setOutputProperty(OutputKeys.INDENT, "yes");
			 trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			 trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			 StringWriter sw= new StringWriter();
			 Result result = new StreamResult(sw);
			 trans.transform(new DOMSource(d.getOwnerDocument()), result);
			 sw.close();
			 return sw.toString();
		} catch (Exception e) {
			throw new Kite9ProcessingException("Couldn't output xml: ",e);
		}
		
	}

	public Object fromXML(String s)  {
		return fromXML(new StringReader(s));
	}
	

	public Object fromXML(Reader s) {
		try {
			SAXDocumentFactory sdf = new SAXDocumentFactory(new ADLExtensibleDOMImplementation(), null);
			Document d = sdf.createDocument(null, s);
			Diagram d2 = (Diagram) d.getDocumentElement();
			handlePostProcessing(d2);
			return d2;		
		} catch (IOException e) {
			throw new Kite9ProcessingException("Couldn't parse xml: ", e);
		}

	}

	public Object fromXML(InputStream s) {
		return fromXML(new InputStreamReader(s));
	}

	private void preProcess(final Diagram d) {
		d.getAllLinks().clear();
		final LinkedHashSet<Connection> allLinks = new LinkedHashSet<Connection>();

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
						allLinks.addAll(((Connected) c).getLinks());
					}
				}

			}.visit(d);

		}
		
		for (Connection connection : allLinks) {
			d.getAllLinks().appendChild(connection);
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
				l.getFrom().getLinks().add(l);
				l.getTo().getLinks().add(l);
				if (l instanceof Link) {
					((Link) l).setRank(rank++);
				}
			}
		}

		if (diag instanceof Contained) {
			((Contained) diag).setContainer((Container) parent);
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
			ContainerProperty<CompositionalDiagramElement> textLines = ((Glyph) diag).getText();
			if (textLines != null) {
				for (CompositionalDiagramElement c : textLines) {
					postProcess(c, diag);
				}
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
				postProcess(c, diag);
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
}
