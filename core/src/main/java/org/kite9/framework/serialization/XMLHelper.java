package org.kite9.framework.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.util.SAXDocumentFactory;
import org.kite9.diagram.xml.DiagramXMLElement;
import org.kite9.framework.common.Kite9ProcessingException;
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
	public static final String DIAGRAM_ELEMENT = "diagram";


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

	public String toXML(DiagramXMLElement d) {
		try {
			 TransformerFactory transfac = TransformerFactory.newInstance();
			 Transformer trans = transfac.newTransformer();
			 trans.setOutputProperty(OutputKeys.INDENT, "yes");
			 trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			 trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			 StringWriter sw= new StringWriter();
			 Result result = new StreamResult(sw);
			 trans.transform(new DOMSource(d), result);
			 sw.close();
			 return sw.toString();
		} catch (Exception e) {
			throw new Kite9ProcessingException("Couldn't output xml: ",e);
		}
		
	}

	public DiagramXMLElement fromXML(String s)  {
		return fromXML(new StringReader(s));
	}
	

	public DiagramXMLElement fromXML(Reader s) {
		try {
			SAXDocumentFactory sdf = new SAXDocumentFactory(new ADLExtensibleDOMImplementation(), null);
			Document d = sdf.createDocument(null, s);
			DiagramXMLElement d2 = (DiagramXMLElement) d.getDocumentElement();
			return d2;		
		} catch (IOException e) {
			throw new Kite9ProcessingException("Couldn't parse xml: ", e);
		}

	}

	public DiagramXMLElement fromXML(InputStream s) {
		return fromXML(new InputStreamReader(s));
	}

}
