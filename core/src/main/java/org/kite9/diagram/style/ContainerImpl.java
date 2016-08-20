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
import org.kite9.framework.common.Kite9ProcessingException;

public class ContainerImpl extends AbstractConnectedXMLDiagramElement implements Container {
	
	private List<DiagramElement> contents = new ArrayList<>();
	private Label label;
	
	@Override
	public List<DiagramElement> getContents() {
		return contents;
	}
	
	public ContainerImpl(StyledXMLElement el, DiagramElement parent) {
		super(el, parent);

		for (XMLElement xmlElement : el) {
			DiagramElement de = xmlElement.getDiagramElement();			
			if (de instanceof Label) {
				label = (Label) de;
			} else if (de instanceof Connection) {
				handleConnection((Connection) de);
			} else {
				contents.add(de);
			} 
		}
	}

	protected void handleConnection(Connection c) {
		throw new Kite9ProcessingException("Containers shouldn't have embedded connections");
	}

	@Override
	public Layout getLayoutDirection() {
		return null;
	}

	@Override
	public Label getLabel() {
		return label;
	}

	@Override
	public boolean isBordered() {
		return !"false".equals(theElement.getAttribute("border"));
	}
	
}
