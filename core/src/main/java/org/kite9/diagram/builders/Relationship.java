package org.kite9.diagram.builders;

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

	// active relationships
	public static final Relationship HAS_TYPE = new Relationship("is a");
	public static final Relationship RETURNS = new Relationship("returns");
	public static final Relationship EXTENDS = new Relationship("extends", Direction.UP);
	public static final Relationship IMPLEMENTS = new Relationship("implements", Direction.UP);
	public static final Relationship CALLS = new Relationship("calls");
	public static final Relationship REFERENCES = new Relationship("references");
	public static final Relationship REQUIRES = new Relationship("requires", Direction.UP);

	// passive relationships
	public static final Relationship IS_TYPE_OF = new Relationship("is type of", HAS_TYPE);
	public static final Relationship RETURNED_BY = new Relationship("returned by", RETURNS);
	public static final Relationship EXTENDED_BY = new Relationship("extended by", EXTENDS);
	public static final Relationship IMPLEMENTED_BY = new Relationship("implemented by", IMPLEMENTS);
	public static final Relationship CALLED_BY = new Relationship("called by", CALLS);
	public static final Relationship REFERENCED_BY = new Relationship("referenced by", REFERENCES);
	public static final Relationship REQUIRED_BY = new Relationship("required by", REQUIRES);

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
		if (type==RelationshipType.PASSIVE) {
			return Direction.reverse(activeRelationship.d);
		}
		return d;
	}

	public String toString() {
		String className = this.getClass().getName();
		return className.substring(className.lastIndexOf(".")) + ":" + name;
	}
}
