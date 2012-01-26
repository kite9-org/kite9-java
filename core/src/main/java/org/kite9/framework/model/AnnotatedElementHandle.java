package org.kite9.framework.model;

import java.lang.reflect.AnnotatedElement;

public interface AnnotatedElementHandle<X extends AnnotatedElement> extends Handle<X>, Comparable<AnnotatedElementHandle<? extends AnnotatedElement>> {

	public String getDeclaringClass();
}
