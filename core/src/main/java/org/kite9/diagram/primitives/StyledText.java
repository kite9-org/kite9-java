package org.kite9.diagram.primitives;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class StyledText {

	private String text;
	
	@XStreamAsAttribute
	private String style;
	
	public StyledText(String t) {
		this.text = t;
	}

	public StyledText(String t, String s) {
		this.text = t;
		this.style = s;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
