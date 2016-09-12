package org.kite9.diagram.adl;

import org.kite9.diagram.style.TerminatorImpl;

/**
 * Describes what's at the end of a {@link Connection}.
 * 
 * @author robmoffat
 *
 */
public interface Terminator extends DiagramElement {

	public static final Terminator NONE = new TerminatorImpl(null, null) {

		@Override
		public String getShapeName() {
			return "NONE";
		}
	};
	

}
