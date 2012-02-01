package org.kite9.diagram.builders.krmodel;

/**
 * In a has-relationship, the name specifies the type of thing that is had,
 * rather than the type of the relationship.
 * 
 * @author moffatr
 * 
 */
public class HasRelationship extends Relationship {

	public HasRelationship(String name) {
		super(name);
	}

	public HasRelationship(HasRelationship active) {
		super(active.getName());
		this.type = RelationshipType.PASSIVE;
		this.activeRelationship = active;
	}

	@Override
	public Object getObjectForAlias() {
		return (type == RelationshipType.ACTIVE ? ("has " + getName()) : ("is "
				+ getName() + " of "));
	}

}
