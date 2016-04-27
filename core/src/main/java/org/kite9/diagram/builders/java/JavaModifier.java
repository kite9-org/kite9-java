package org.kite9.diagram.builders.java;

import org.kite9.framework.alias.AliasEnabled;

class JavaModifier implements AliasEnabled {
	
	public static final JavaModifier FINAL = new JavaModifier("final");
	public static final JavaModifier STATIC = new JavaModifier("static");

	public static final JavaModifier PUBLIC = new JavaModifier("public");
	public static final JavaModifier PROTECTED = new JavaModifier("protected");
	public static final JavaModifier PRIVATE = new JavaModifier("private");
	
	String value;

	public JavaModifier(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Object getObjectForAlias() {
		return value;
	}

	
}
