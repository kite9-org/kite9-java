package org.kite9.diagram.builders.krmodel;

import org.kite9.diagram.position.Direction;
import org.kite9.framework.alias.AliasEnabled;

/**
 * Models the relationship between a subject and an object. e.g. has, uses, etc.
 * i.e. a verb.
 * 
 * @author moffatr
 * 
 */
public class Relationship implements AliasEnabled {

	public enum RelationshipType {
		ACTIVE, PASSIVE, BIDIRECTIONAL
	};

	RelationshipType type = RelationshipType.ACTIVE;

	/**
	 * Creates the active voice relationship
	 */
	public Relationship(String name) {
		super();
		this.name = name;
		this.type = RelationshipType.ACTIVE;
	}

	/**
	 * Creates a passive voice relationship. Note that when a builder comes to
	 * render the relationship on the diagram, it will be converted to the
	 * active voice.
	 */
	public Relationship(String name, Relationship active) {
		super();
		this.name = name;
		this.type = RelationshipType.PASSIVE;
		this.activeRelationship = active;
	}

	/**
	 * Creates a relationship where there is a natural diagrammatic direction
	 * required for from and to. e.g. inheritance relationships usually go
	 * downwards
	 */
	public Relationship(String name, Direction d) {
		this.name = name;
		this.d = d;
	}

	protected Relationship activeRelationship;

	private String name;

	private Direction d = null;

	public String getName() {
		return name;
	}

	public Object getObjectForAlias() {
		return name;
	}

	public Relationship getActiveRelationship() {
		if (type == RelationshipType.ACTIVE) {
			return this;
		}

		return activeRelationship;
	}

	public RelationshipType getType() {
		return type;
	}

	public Direction getDirection() {
		if (type == RelationshipType.PASSIVE) {
			return Direction.reverse(activeRelationship.d);
		}
		return d;
	}

	public String toString() {
		String className = this.getClass().getName();
		return className.substring(className.lastIndexOf(".")) + ":" + name;
	}
}
