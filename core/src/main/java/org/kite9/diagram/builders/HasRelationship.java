package org.kite9.diagram.builders;

/**
 * In a has-relationship, the name specifies the type of thing that is had, rather
 * than the type of the relationship.
 *  
 * @author moffatr
 *
 */
public class HasRelationship extends Relationship {
    
    // active relationships (a has parameter b)
    public static final HasRelationship VISIBILITY = new HasRelationship("visibility");
    public static final HasRelationship PARAMETER = new HasRelationship("parameter");
    public static final HasRelationship NAME = new HasRelationship("name");
    public static final HasRelationship MODIFIER = new HasRelationship("modifier");
    public static final HasRelationship INNER_CLASS = new HasRelationship("inner class");
    public static final HasRelationship FIELD = new HasRelationship("field");
    public static final HasRelationship METHOD = new HasRelationship("method");;
    public static final HasRelationship PACKAGE = new HasRelationship("package");;
    public static final HasRelationship ANNOTATION = new HasRelationship("annotation");;
    public static final HasRelationship CLASS = new HasRelationship("class");;
    public static final HasRelationship TYPE = new HasRelationship("type");
    public static final HasRelationship CLASS_GROUP = new HasRelationship("classes");;
    
    // passive relationships (b is parameter of a)
    public static final HasRelationship VISIBILITY_OF = new HasRelationship(VISIBILITY);
    public static final HasRelationship PARAMETER_OF = new HasRelationship(PARAMETER);
    public static final HasRelationship NAME_OF = new HasRelationship(NAME);
    public static final HasRelationship MODIFIER_OF = new HasRelationship(MODIFIER);
    public static final HasRelationship INNER_CLASS_OF = new HasRelationship(INNER_CLASS);
    public static final HasRelationship FIELD_OF = new HasRelationship(FIELD);
    public static final HasRelationship METHOD_OF = new HasRelationship(METHOD);;
    public static final HasRelationship PACKAGE_OF = new HasRelationship(PACKAGE);;
    public static final HasRelationship ANNOTATION_OF = new HasRelationship(ANNOTATION);;
    public static final HasRelationship CLASS_OF = new HasRelationship(CLASS);;
    public static final HasRelationship TYPE_OF = new HasRelationship(TYPE);
    
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
	return (type==RelationshipType.ACTIVE ? ("has "+getName()) : ("is "+getName()+" of "));
    }

    
}
