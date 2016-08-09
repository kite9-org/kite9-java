package org.kite9.diagram.adl;

import org.w3c.dom.Element;

public interface XMLElement extends Element {
	
	public String getID();

	public void setTagName(String tag);
	
	public void setOwnerDocument(ADLDocument doc);

}
