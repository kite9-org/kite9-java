/**
 * 
 */
package org.kite9.diagram.position;

public enum HPos {
	LEFT, RIGHT;

	public Direction getDirection() {
		if (this.ordinal() == 0) {
			return Direction.LEFT;
		} else {
			return Direction.RIGHT;
		}
	}
}