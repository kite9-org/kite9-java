package org.kite9.diagram.builders.java.krmodel;

import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.verb.AttributeVerb;
import org.kite9.diagram.builders.krmodel.verb.ComposesVerb;
import org.kite9.diagram.builders.krmodel.verb.SimpleTypedVerb;
import org.kite9.diagram.builders.krmodel.verb.SimpleVerb;
import org.kite9.diagram.builders.krmodel.verb.TypeVerb;
import org.kite9.diagram.position.Direction;

public class JavaRelationships {

	private static final NounPart METHOD_NOUN = new JavaModelNoun("method");
	private static final NounPart PACKAGE_NOUN = new JavaModelNoun("package");
	private static final NounPart CLASS_NOUN = new JavaModelNoun("class");
	private static final NounPart INTERFACE_NOUN = new JavaModelNoun("interface");
	private static final NounPart ANNOTATION_NOUN = new JavaModelNoun("annotation");
	private static final NounPart FIELD_NOUN = new JavaModelNoun("field");
	private static final NounPart INNER_CLASS_NOUN = new JavaModelNoun("inner-class");
	private static final NounPart PARAMETER_NOUN = new JavaModelNoun("parameter");
	private static final NounPart MODIFIER_NOUN = new JavaModelNoun("modifier");
	private static final NounPart NAME_NOUN = new JavaModelNoun("name");
	private static final NounPart VISIBILTY_NOUN = new JavaModelNoun("name");
	

	// ATTRIBUTE RELATIONSHIPS
    public static final AttributeVerb IS_VISIBILITY = new AttributeVerb("java.visibility", "visibility", VISIBILTY_NOUN);
    public static final AttributeVerb IS_NAME = new AttributeVerb("name", "name", NAME_NOUN );
    public static final AttributeVerb IS_MODIFIER = new AttributeVerb("java.modifier", "modifier", MODIFIER_NOUN);
    public static final AttributeVerb IS_CLASS = new AttributeVerb("java.class", "class", CLASS_NOUN);

	// passive relationships
    public static final AttributeVerb VISIBILITY_OF = new AttributeVerb(IS_VISIBILITY);
    public static final AttributeVerb NAME_OF = new AttributeVerb(IS_NAME);
    public static final AttributeVerb MODIFIER_OF = new AttributeVerb(IS_MODIFIER);
    public static final AttributeVerb CLASS_OF = new AttributeVerb(IS_CLASS);
	
	// COMPOSITION RELATIONSHIPS

	// active relationships (a has parameter b)
    public static final ComposesVerb PARAMETER = new ComposesVerb(PARAMETER_NOUN, CLASS_NOUN);
    public static final ComposesVerb INNER_CLASS = new ComposesVerb(INNER_CLASS_NOUN, CLASS_NOUN, CLASS_NOUN);
    public static final ComposesVerb FIELD = new ComposesVerb(FIELD_NOUN, CLASS_NOUN);
    public static final ComposesVerb METHOD = new ComposesVerb(METHOD_NOUN, CLASS_NOUN);
    public static final ComposesVerb PACKAGE_CLASS = new ComposesVerb(CLASS_NOUN, PACKAGE_NOUN);
    public static final ComposesVerb ANNOTATION = new ComposesVerb(ANNOTATION_NOUN, CLASS_NOUN);
    public static final ComposesVerb GROUP_PACKAGE = new ComposesVerb(PACKAGE_NOUN, null);	
    public static final ComposesVerb GROUP_CLASS = new ComposesVerb(CLASS_NOUN, null);	
    
    
    // passive relationships (b is parameter of a)
    public static final ComposesVerb PARAMETER_OF = new ComposesVerb(PARAMETER);
    public static final ComposesVerb INNER_CLASS_OF = new ComposesVerb(INNER_CLASS);
    public static final ComposesVerb FIELD_OF = new ComposesVerb(FIELD);
    public static final ComposesVerb METHOD_OF = new ComposesVerb(METHOD);
    public static final ComposesVerb PACKAGE_OF = new ComposesVerb(PACKAGE_CLASS);
    public static final ComposesVerb ANNOTATION_OF = new ComposesVerb(ANNOTATION);
    
    // TYPE
    
	public static final TypeVerb HAS_TYPE = new TypeVerb("type", "is a");
	public static final TypeVerb IS_TYPE_OF = new TypeVerb("is type of", HAS_TYPE);

    
    
    
    // OTHER RELATIONSHIPS

	// active relationships
	public static final SimpleTypedVerb RETURNS = new SimpleTypedVerb("returns", METHOD_NOUN, CLASS_NOUN);
	public static final SimpleTypedVerb EXTENDS = new SimpleTypedVerb("extends", Direction.UP, CLASS_NOUN, CLASS_NOUN);
	public static final SimpleTypedVerb IMPLEMENTS = new SimpleTypedVerb("implements", Direction.UP, CLASS_NOUN, INTERFACE_NOUN);
	public static final SimpleTypedVerb METHOD_CALLS_METHOD = new SimpleTypedVerb("calls", METHOD_NOUN, METHOD_NOUN);
	public static final SimpleTypedVerb CLASS_CALLS_METHOD = new SimpleTypedVerb("calls", CLASS_NOUN, METHOD_NOUN);
	public static final SimpleTypedVerb REFERENCES = new SimpleTypedVerb("references", METHOD_NOUN, null);
	public static final SimpleTypedVerb REQUIRES = new SimpleTypedVerb("requires", Direction.UP, CLASS_NOUN, null);

	// passive relationships
	public static final SimpleVerb RETURNED_BY = new SimpleVerb("returned by", RETURNS);
	public static final SimpleVerb EXTENDED_BY = new SimpleVerb("extended by", EXTENDS);
	public static final SimpleVerb IMPLEMENTED_BY = new SimpleVerb("implemented by", IMPLEMENTS);
	public static final SimpleVerb METHOD_CALLED_BY_CLASS = new SimpleVerb("called by", CLASS_CALLS_METHOD);
	public static final SimpleVerb METHOD_CALLED_BY_METHOD = new SimpleVerb("called by", METHOD_CALLS_METHOD);
	public static final SimpleVerb REFERENCED_BY = new SimpleVerb("referenced by", REFERENCES);
	public static final SimpleVerb REQUIRED_BY = new SimpleVerb("required by", REQUIRES);
	
	public static final SimpleTypedVerb METHOD_CALLS_CLASS = new SimpleTypedVerb("calls", METHOD_NOUN, CLASS_NOUN);
	public static final SimpleVerb CLASS_CALLED_BY_METHOD = new SimpleVerb("called by", METHOD_CALLS_CLASS);


}
