package org.kite9.diagram.builders.java;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kite9.diagram.builders.krmodel.AnnotatedNounPartImpl;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.logging.LogicException;

/**
 * This helps build nouns from Java Types, especially with regard to arrays, collections and generics.
 *
 * @author robmoffat
 *
 */
public class TypeNounHelper {

	Unraveller[] unravellers = new Unraveller[] { new CollectionUnraveller(), new ArrayUnraveller(), new MapUnraveller(),
			new BasicUnraveller(), new TypeVariableUnraveller()};

	protected static interface Unraveller {

		NounPart unravel(Type t, Aliaser a);

		boolean handles(Type t);

	}

	public NounPart generateNoun(Type t, Aliaser a) {
		for (Unraveller u : unravellers) {
			if (u.handles(t)) {
				return u.unravel(t, a);
			}
		}

		throw new Kite9ProcessingException("Can't handle: " + t);
	}

	public class CollectionUnraveller implements Unraveller {

		public NounPart unravel(Type to, Aliaser a) {
			if (to instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) to;
				Type raw = pt.getRawType();
				Type[] args = pt.getActualTypeArguments();
				if ((raw instanceof Class<?>) && (Iterable.class.isAssignableFrom((Class<?>) raw))) {
					if (args.length != 1) {
						throw new Kite9ProcessingException(
								"Could not handle collection with more than one type argument: " + to);
					} else {
						NounPart np = generateNoun(args[0], a);
						String label = a.getObjectAlias(raw) + " of ";
						return new AnnotatedNounPartImpl(np, label);
					}
				}
			}

			throw new LogicException("Should not unravel with collection unraveller !" + to);
		}

		public boolean handles(Type t) {
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				Type rt = pt.getRawType();
				if (rt instanceof Class<?>) {
					if ((Iterable.class.isAssignableFrom((Class<?>) rt))) {
						return true;
					}
				}
			}
			return false;
		}

	}

	public class ArrayUnraveller implements Unraveller {

		public NounPart unravel(Type to, Aliaser a) {
			if (to instanceof GenericArrayType) {
				GenericArrayType gt = (GenericArrayType) to;
				Type raw = gt.getGenericComponentType();
				NounPart np = generateNoun(raw, a);
				return new AnnotatedNounPartImpl(np, "array of ");
			} else if ((to instanceof Class<?>) && (((Class<?>) to).isArray())) {
				NounPart np = generateNoun(((Class<?>) to).getComponentType(), a);
				return new AnnotatedNounPartImpl(np, "array of ");
			}

			throw new LogicException("Should not unravel with array unraveller !" + to);
		}

		public boolean handles(Type t) {
			return (t instanceof GenericArrayType) || ((t instanceof Class<?>) && (((Class<?>) t).isArray()));
		}

	}
	
	public class MapUnraveller implements Unraveller {

		public NounPart unravel(Type to, Aliaser a) {
				ParameterizedType pt = (ParameterizedType) to;
				Type raw = pt.getRawType();
				Type[] args = pt.getActualTypeArguments();
				
				Type key = args[0];
				Type value = args[1];
				List<NounPart> disambiguation = new ArrayList<NounPart>();
				if (key!=null) {
					disambiguation.add(new AnnotatedNounPartImpl(generateNoun(key, a),"from "));
					disambiguation.add(new AnnotatedNounPartImpl(generateNoun(value, a),"to "));
				}

				return new ProjectStaticSimpleNoun(raw, a, disambiguation);
		}

		public boolean handles(Type t) {
			if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) t;
				Type rt = pt.getRawType();
				if (rt instanceof Class<?>) {
					if ((Map.class.isAssignableFrom((Class<?>) rt))) {
						return true;
					}
				}
			}
			
			return false;
		}

	}
	

	public static class BasicUnraveller implements Unraveller {

		public boolean handles(Type t) {
			return (t instanceof Class<?>);
		}

		public NounPart unravel(Type t, Aliaser a) {
			return new ProjectStaticSimpleNoun((Class<?>) t, a);
		}

	}


	public static class TypeVariableUnraveller implements Unraveller {

		public boolean handles(Type t) {
			return (t instanceof TypeVariable);
		}

		public NounPart unravel(Type t, Aliaser a) {
			return new ProjectStaticSimpleNoun("<"+((TypeVariable<?>) t).getName()+">", a);
		}

	}

}