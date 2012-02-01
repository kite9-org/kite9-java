package org.kite9.diagram.builders.java;

import org.kite9.diagram.builders.krmodel.HasRelationship;
import org.kite9.diagram.builders.krmodel.Relationship;
import org.kite9.diagram.position.Direction;

public class JavaRelationships {
	
	// HAS RELATIONSHIPS

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
    
    // OTHER RELATIONSHIPS

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

}
