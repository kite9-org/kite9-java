package org.kite9.framework.serialization;

import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class BoxShadowManager implements ShorthandManager {

	public String getPropertyName() {
		return "box-shadow";
	}

	public boolean isAnimatableProperty() {
		return false;
	}

	public boolean isAdditiveProperty() {
		return false;
	}

	public void setValues(CSSEngine eng, PropertyHandler ph, LexicalUnit lu, boolean imp) throws DOMException {
		LexicalUnit xo = lu.getNextLexicalUnit();
		LexicalUnit yo = xo.getNextLexicalUnit();
		LexicalUnit blur = yo.getNextLexicalUnit();
		LexicalUnit color = blur.getNextLexicalUnit();
		
		ph.property("box-shadow-x-offset", xo, false);
		ph.property("box-shadow-y-offset", yo, false);
		ph.property("box-shadow-color", color, false);
		
	}

}
