package org.kite9.diagram.adl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Styles of link-end.  Can be either an arrow-head or unstyled in ADL.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("style")
public class LinkEndStyle {

	public static final String ARROW = "ARROW";
	public static final String NONE = "NONE";
	public static final String CIRCLE = "CIRCLE";
	public static final String GAP = "GAP";
}
