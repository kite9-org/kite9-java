package org.kite9.diagram.builders.krmodel.verb;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.position.Direction;
import org.kite9.framework.alias.AliasEnabled;

/**
 * Models the relationship between a subject and an object. e.g. has, uses, etc.
 * i.e. a verb.
 * 
 * @author moffatr
 * 
 */
public abstract class AbstractVerb implements AliasEnabled, Knowledge, Verb {
	
	private final VerbType type;

	/**
	 * Creates the active voice relationship
	 */
	public AbstractVerb(String id, String name) {
		this(id, name, null);
	}

	/**
	 * Creates a passive voice relationship. Note that when a builder comes to
	 * render the relationship on the diagram, it will be converted to the
	 * active voice.
	 */
	public AbstractVerb(String name, Verb active) {
		super();
		this.name = name;
		this.type = VerbType.PASSIVE;
		this.activeRelationship = active;
		this.id = active.getID().toString();
		this.d = null;
	}

	/**
	 * Creates a relationship where there is a natural diagrammatic direction
	 * required for from and to. e.g. inheritance relationships usually go
	 * downwards
	 */
	public AbstractVerb(String id, String name, Direction d) {
		this.name = name;
		this.d = d;
		this.type = VerbType.ACTIVE;
		this.id = id;
		this.activeRelationship = null;
	}

	protected final Verb activeRelationship;

	protected final String name;
	
	protected final String id;

	protected final Direction d;

	public Object getObjectForAlias() {
		return name;
	}

	public Verb getActiveRelationship() {
		if (type == VerbType.ACTIVE) {
			return this;
		}

		return activeRelationship;
	}

	public VerbType getType() {
		return type;
	}

	public Direction getDirection() {
		return d;
	}

	public String toString() {
		String className = this.getClass().getName();
		return className.substring(className.lastIndexOf(".")) + ":" + name;
	}

	public Address getID() {
		return new AddressImpl(id);
	}
	
	public String getStringID() {
		return id;
	}

	
	
}
