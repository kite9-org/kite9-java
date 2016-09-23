package org.kite9.diagram.style;

import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.adl.Connection;
import org.kite9.diagram.adl.Container;
import org.kite9.diagram.adl.DiagramElement;
import org.kite9.diagram.adl.Label;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.xml.StyledXMLElement;
import org.kite9.diagram.xml.XMLElement;
import org.kite9.framework.serialization.CSSConstants;
import org.kite9.framework.serialization.EnumValue;

public class ContainerImpl extends AbstractConnectedXMLDiagramElement implements Container {
	
	private List<DiagramElement> contents = new ArrayList<>();
	private Label label;
	
	@Override
	public List<DiagramElement> getContents() {
		ensureInitialized();
		return contents;
	}
	
	@Override
	protected void initialize() {
		initElement(theElement);
		super.initialize();
	}

	private void initElement(XMLElement theElement) {
		for (XMLElement xmlElement : theElement) {
			DiagramElement de = xmlElement.getDiagramElement();			
			if (de instanceof Label) {
				label = (Label) de;
			} else if (de instanceof Connection) {
				addConnectionReference((Connection) de);
			} else if (de != null) { 
				contents.add(de);
			} else {
				initElement(xmlElement);
			}
		}
	}

	protected void addConnectionReference(Connection de) {
		((ContainerImpl) getDiagram()).addConnectionReference(de);
	}

	public ContainerImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public Layout getLayout() {
		String attribute = theElement.getAttribute("layout");
		if ((attribute != null) && (attribute.trim().length() != 0)) {
			return Layout.valueOf(attribute);
		} 
		
		EnumValue ev = (EnumValue) getCSSStyleProperty(CSSConstants.LAYOUT_PROPERTY);
		if (ev != null) {
			return (Layout) ev.getTheValue();
		}
		
		return null;
	}

	@Override
	public Label getLabel() {
		ensureInitialized();
		return label;
	}

	@Override
	public boolean isBordered() {
		return !"false".equals(theElement.getAttribute("border"));
	}
	
}
