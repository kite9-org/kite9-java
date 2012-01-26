package org.kite9.framework.model;

import java.lang.reflect.Method;

public class MethodHandle extends MemberHandle {

	public MethodHandle(Method m) {
		super();
		this.className = convertClassName(m.getDeclaringClass());
		this.name = m.getName();
		this.desc = Type.getMethodDescriptor(m);
	}

	public MethodHandle(String className, String name, String desc) {
	    this.className = className;
	    this.name = name;
	    this.desc = desc;
	}

	public Method hydrate(ClassLoader cl) {
	    return hydrateMethod(this, cl);
	}
	
	public Class<?> hydrateClass(ClassLoader cl) {
	    return hydrateClass(className, cl);
	}

	public String getDeclaringClass() {
		return className;
	}


}
