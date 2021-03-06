package org.kite9.diagram.xml;

import org.kite9.diagram.adl.DiagramElement;
import org.w3c.dom.Element;

public interface XMLElement extends Element, Iterable<XMLElement> {
	
	public String getID();

	public void setTagName(String tag);
	
	public void setOwnerDocument(ADLDocument doc);
	
	public int getChildXMLElementCount();
	
	public DiagramElement getDiagramElement();
	
	public ADLDocument getOwnerDocument();

	public <E extends Element> E getProperty(String name);
	
	public <E extends Element> E replaceProperty(String propertyName, E e);
	
}
