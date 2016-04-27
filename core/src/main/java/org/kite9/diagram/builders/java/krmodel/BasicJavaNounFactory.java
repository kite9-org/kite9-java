package org.kite9.diagram.builders.java.krmodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.kite9.diagram.builders.id.IdHelper;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.framework.alias.Aliaser;

public class BasicJavaNounFactory extends AbstractNounFactory {

	private final Aliaser a;
	private final IdHelper idHelper;

	public BasicJavaNounFactory(Aliaser a, IdHelper idHelper) {
		this.a = a;
		this.idHelper = idHelper;
	}

	public NounPart createNoun(Object item) {
		if (item == null) {
			return null;
		} else if (item instanceof Type) {
			return new TypeNounHelper(idHelper, a).generateNoun((Type) item, a);
		} else if (oneOf(item, Method.class, Constructor.class, Field.class, Package.class, Annotation.class,
				Enum.class)) {
			return new ProjectStaticSimpleNoun(idHelper.getId(item), item, a.getObjectAlias(item));
		} else {
		//	return new ObjectInstanceSimpleNoun(item, a);
			return null;
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

	@Deprecated
	@Override
	public NounPart createNewSubjectNounPart(Proposition t) {
		return extractObject(t);
	}
	

}
