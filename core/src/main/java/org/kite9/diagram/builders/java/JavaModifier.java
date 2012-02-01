package org.kite9.diagram.builders.java;

import org.kite9.framework.alias.AliasEnabled;

class JavaModifier implements AliasEnabled {
    
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
