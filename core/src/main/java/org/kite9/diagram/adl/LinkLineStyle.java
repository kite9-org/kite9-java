package org.kite9.diagram.adl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Styles of link-line.  Can be normal, dotted or invisible.
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("line-style")
public enum LinkLineStyle {

	DOTTED, INVISIBLE, NORMAL
}
