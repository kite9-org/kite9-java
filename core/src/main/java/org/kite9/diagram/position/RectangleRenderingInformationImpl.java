package org.kite9.diagram.position;

import org.w3c.dom.Node;

public class RectangleRenderingInformationImpl extends AbstractRenderingInformationImpl implements RectangleRenderingInformation {

	boolean multipleHorizontalLinks;
	boolean multipleVerticalLinks;

	public boolean isMultipleHorizontalLinks() {
		return multipleHorizontalLinks;
	}

	public void setMultipleHorizontalLinks(boolean multipleHorizontalLinks) {
		this.multipleHorizontalLinks = multipleHorizontalLinks;
	}

	public boolean isMultipleVerticalLinks() {
		return multipleVerticalLinks;
	}

	public void setMultipleVerticalLinks(boolean multipleVerticalLinks) {
		this.multipleVerticalLinks = multipleVerticalLinks;
	}
	
	public HPos getHorizontalJustification() {
		String hpos = getAttribute("x-just");
		if (hpos.length() == 0) {
			return null;
		} else {
			return HPos.valueOf(hpos);
		}
	}

	public void setHorizontalJustification(HPos horizontalJustification) {
		setAttribute("x-just", horizontalJustification == null ? "" : horizontalJustification.name());
	}

	public VPos getVerticalJustification() {
		String vpos = getAttribute("y-just");
		if (vpos.length() == 0) {
			return null;
		} else {
			return VPos.valueOf(vpos);
		}
	}

	public void setVerticalJustification(VPos verticalJustification) {
		setAttribute("y-just", verticalJustification == null ? "" :verticalJustification.name());
	}

	@Override
	protected Node newNode() {
		return new RectangleRenderingInformationImpl();
	}


}
