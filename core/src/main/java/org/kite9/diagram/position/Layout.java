package org.kite9.diagram.position;

/**
 * Extra layout options for Containers
 * 
 * @author robmoffat
 *
 */
public enum Layout {
	
	HORIZONTAL, VERTICAL, LEFT, RIGHT, UP, DOWN;

	public static Layout reverse(Layout d) {
		if (d==null)
			return null;
		switch (d) {
		case HORIZONTAL:
			return HORIZONTAL;
		case VERTICAL:
			return VERTICAL;
		case LEFT: 
			return RIGHT;
		case RIGHT:
			return LEFT;
		case UP:
			return DOWN;
		default:
			return UP;
		}
	}
}
