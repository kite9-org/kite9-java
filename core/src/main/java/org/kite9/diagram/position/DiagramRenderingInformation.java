package org.kite9.diagram.position;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Contains some extra details about how the diagram was rendered
 * 
 * @author robmoffat
 *
 */
@XStreamAlias("diagram-ri")
public class DiagramRenderingInformation extends RectangleRenderingInformation {

	private static final long serialVersionUID = 797534848534052362L;

	@XStreamAsAttribute
	private String hash;

	@XStreamAsAttribute
	private String stylesheet;

	
	public String getStylesheet() {
		return stylesheet;
	}

	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
}
