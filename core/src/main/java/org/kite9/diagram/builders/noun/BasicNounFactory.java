package org.kite9.diagram.builders.noun;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.kite9.diagram.builders.JavaModifier;
import org.kite9.diagram.builders.TypeNounHelper;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;

public class BasicNounFactory implements NounFactory {

	private Aliaser a;

	public BasicNounFactory(Aliaser a) {
		this.a = a;
	}

	public NounPart createNoun(Object item) {
		if (item == null) {
			return null;
		} else if (item instanceof Type) {
			return new TypeNounHelper().generateNoun((Type) item, a);
		} else if (oneOf(item, Method.class, Constructor.class, JavaModifier.class, Field.class, Package.class, Annotation.class,
				String.class, Enum.class)) {
			return new SimpleNounImpl(item, a);
		}

		throw new Kite9ProcessingException("Noun factory doesn't know how to create noun for: " + item);
	}

	private boolean oneOf(Object item, Class<?>... classes) {
		for (Class<?> class1 : classes) {
			if (class1.isAssignableFrom(item.getClass())) {
				return true;
			}
		}

		return false;
	}
}
