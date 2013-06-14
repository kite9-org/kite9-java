package org.kite9.diagram.adl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Styles of link-line.  Can be normal, dotted or invisible.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("line-style")
public class LinkLineStyle {
	
	public static final String DOTTED = "DOTTED";
	public static final String INVISIBLE = "INVISIBLE";
	public static final String NORMAL = "NORMAL";

}
