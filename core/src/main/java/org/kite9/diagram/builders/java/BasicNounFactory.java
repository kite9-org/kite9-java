package org.kite9.diagram.builders.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.framework.alias.Aliaser;

public class BasicNounFactory extends NounFactory {

	private Aliaser a;

	public BasicNounFactory(Aliaser a) {
		this.a = a;
	}

	public NounPart createNoun(Object item) {
		if (item == null) {
			return null;
		} else if (item instanceof Type) {
			return new TypeNounHelper().generateNoun((Type) item, a);
		} else if (oneOf(item, Method.class, Constructor.class, Field.class, Package.class, Annotation.class,
				Enum.class)) {
			return new ProjectStaticSimpleNoun(item, a);
		} else {
			return new ObjectInstanceSimpleNoun(item, a);
		}
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
