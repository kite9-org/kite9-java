package org.kite9.framework.serialization;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Contains rendering information for drawing components in SVG format.
 * 
 * @author robmoffat
 */
public class XMLFragments {
	
	public XMLFragments() {
		this("http://www.w3.org/2000/svg");
	}
	
	public XMLFragments(String namespace) {
		super();
		this.xmlns = namespace;
	}

	@XStreamAsAttribute
	private final String xmlns;
	
	public String getNamespace() {
		return xmlns;
	}

	@XStreamImplicit
	private final List<Element> parts = new ArrayList<Element>();

	public List<Element> getParts() {
		return parts;
	}


}
