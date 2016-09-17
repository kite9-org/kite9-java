package org.kite9.diagram.style;

import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.xml.StyledXMLElement;

/**
 * Container and link-end labels. (TEMPORARY)
 * 
 * @author robmoffat
 * 
 */
public class LabelImpl extends AbstractRectangularXMLDiagramElement implements Label {
	
	
	public LabelImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public boolean hasContent() {
		return !theElement.getTextContent().trim().isEmpty();
	}

	private Object tempParent;
	
	
	@Deprecated
	@Override
	public void setParent(Object o) {
		this.tempParent = 0;
	}
	
	@Override
	public DiagramElement getParent() {
		return tempParent == null ? super.getParent() : (DiagramElement) tempParent;
	}

	@Override
	protected void initialize() {
	}
}