package org.kite9.diagram.primitives;

import org.kite9.diagram.adl.ADLDocument;
import org.w3c.dom.Element;

public interface XMLDiagramElement extends DiagramElement, Element {

	public void setTagName(String tag);
	
	public void setOwnerDocument(ADLDocument doc);
}
