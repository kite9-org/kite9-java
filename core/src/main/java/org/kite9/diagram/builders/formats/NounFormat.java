package org.kite9.diagram.builders.formats;

import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.diagram.primitives.Connected;
import org.kite9.diagram.primitives.Container;

/**
 * Allows you to specify the style of a noun as it is shown on the diagram.
 */
public interface NounFormat {
	public Connected returnElement(Container c, SimpleNoun representing,
			InsertionInterface ii);
}