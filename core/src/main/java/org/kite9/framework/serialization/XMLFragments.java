package org.kite9.framework.serialization;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Contains rendering information for drawing components in SVG format.
 * 
 * @author robmoffat
 */
public class XMLFragments {
	
	@XStreamImplicit
	private final List<Element> parts = new ArrayList<Element>();

	public List<Element> getParts() {
		return parts;
	}


}
