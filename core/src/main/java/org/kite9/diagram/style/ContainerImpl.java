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
		for (XMLElement xmlElement : theElement) {
			DiagramElement de = xmlElement.getDiagramElement();			
			if (de instanceof Label) {
				label = (Label) de;
			} else if (de instanceof Connection) {
			} else if (de != null) { 
				contents.add(de);
			} 
		}

		super.initialize();
	}




	public ContainerImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);
	}

	@Override
	public Layout getLayoutDirection() {
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
