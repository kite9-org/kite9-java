package org.kite9.diagram.style.impl;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.Container;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.xml.StyledXMLElement;

/**
 * Container and link-end labels. (TEMPORARY)
 * 
 * @author robmoffat
 * 
 */
public class LabelContainerImpl extends AbstractContainerXMLDiagramElement implements Label, Container {
	
	
	public LabelContainerImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public boolean hasContent() {
		return getContents().size() > 0;
	}
	
	
	@Override
	protected void initialize() {
	}

	@Override
	public boolean isConnectionLabel() {
		return getParent() instanceof Connection;
	}

	@Override
	public Label getLabel() {
		return null;		// not going to use this here.  Consider removing?
	}

	@Override
	public boolean isBordered() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}