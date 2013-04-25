package org.kite9.diagram.adl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Styles of link-end.  Can be either an arrow-head or unstyled in ADL.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("style")
public enum LinkEndStyle {

	ARROW, NONE, CIRCLE, GAP
}
