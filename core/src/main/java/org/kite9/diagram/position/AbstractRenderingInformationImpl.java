package org.kite9.diagram.position;

import org.kite9.diagram.adl.ContainerProperty;
import org.kite9.diagram.primitives.AbstractDiagramElement;
import org.kite9.diagram.primitives.DiagramElement;
import org.w3c.dom.Element;

public abstract class AbstractRenderingInformationImpl extends AbstractDiagramElement implements RenderingInformation {

	public int compareTo(DiagramElement o) {
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public ContainerProperty<Element> getDisplayData() {
		return getProperty("displayData", ContainerProperty.class);
	}

	public void setDisplayData(ContainerProperty<Element> displayData) {
		replaceProperty("displayData", displayData, ContainerProperty.class);
	}

	public AbstractRenderingInformationImpl() {
		super("renderingInformation", TESTING_DOCUMENT);
	}

	public boolean isRendered() {
		return !"false".equals(getAttribute("rendered"));
	}

	public void setRendered(boolean r) {
		if (r) {
			removeAttribute("rendered");
		} else {
			setAttribute("rendered", "false");
		}
	}
	
	
	
	public Dimension2D getPosition() {
		Dimension2D pos = getProperty("position", Dimension2D.class);
		return (pos == null) ?  CostedDimension.ZERO : pos;
	}

	public void setPosition(Dimension2D position) {
		replaceProperty("position", position, Dimension2D.class);
	}

	public Dimension2D getSize() {
		Dimension2D size = getProperty("size", Dimension2D.class);
		return (size == null) ?  CostedDimension.ZERO : size;
	}

	public void setSize(Dimension2D size) {
		replaceProperty("size", size, Dimension2D.class);
	}
	
	public Dimension2D getInternalSize() {
		Dimension2D size = getProperty("internal-size", Dimension2D.class);
		return (size == null) ?  CostedDimension.ZERO : size;
	}

	public void setInternalSize(Dimension2D size) {
		replaceProperty("internal-size", size, Dimension2D.class);
	}

}
