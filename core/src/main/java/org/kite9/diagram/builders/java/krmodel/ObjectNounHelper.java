package org.kite9.diagram.builders.java.krmodel;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.kite9.diagram.builders.id.IdHelper;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * This helps build nouns from Java Objects, especially with regard to arrays, collections and maps.
 * 
 * @see TypeNounHelper
 * 
 * @author robmoffat
 *
 */
public class ObjectNounHelper {

	Unraveller[] unravellers = new Unraveller[] { new CollectionUnraveller(), new ArrayUnraveller(),
			new BasicUnraveller()};

	protected static interface Unraveller {

		void unravel(Object o, Aliaser a, Set<NounPart> out);

		boolean handles(Object t);

	}

	public Set<NounPart> generateNouns(Object t, Aliaser a) {
		Set<NounPart> out = new LinkedHashSet<NounPart>();
		unravelMain(t, a, out);
		return out;
	}


	private void unravelMain(Object t, Aliaser a, Set<NounPart> out) {
		if (t==null) {
			return;
		}
		
		for (Unraveller u : unravellers) {
			if (u.handles(t)) {
				u.unravel(t, a, out);
				return;
			}
		}

		throw new Kite9ProcessingException("Can't handle: " + t);
	}

	public class CollectionUnraveller implements Unraveller {

		public void unravel(Object to, Aliaser a, Set<NounPart> out) {
			for (Object o : ((Collection<?>) to)) {
				unravelMain(o, a, out);
			}
		}

		public boolean handles(Object t) {
			return t instanceof Collection;
		}

	}

	public class ArrayUnraveller implements Unraveller {

		public void unravel(Object to, Aliaser a, Set<NounPart> out) {
			int len = Array.getLength(to);
			for (int i = 0; i < len; i++) {
				unravelMain(Array.get(to, i), a, out);
			}
		}

		public boolean handles(Object t) {
			return t.getClass().isArray();
		}

	}
	
	

	public static class BasicUnraveller implements Unraveller {

		public boolean handles(Object t) {
			return true;
		}

		public void unravel(Object to, Aliaser a, Set<NounPart> out) {
			out.add(new ProjectStaticSimpleNoun(ihto, a));
		}

	}

}