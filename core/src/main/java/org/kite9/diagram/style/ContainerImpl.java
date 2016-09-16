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
	public Layout getLayoutDirection() {
		String attribute = theElement.getAttribute("layout");
		if ((attribute != null) && (attribute.trim().length() != 0)) {
			return Layout.valueOf(attribute);
		} else {
			return null;
		}
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
