package org.kite9.diagram.builders.wizards.sequence;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.noun.NounFactory;
import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.diagram.builders.noun.SimpleNoun;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.ProjectModel;

/**
 * This class examines the method-call hierarchy and produces a sequence diagram
 * based on this.  
 * 
 * @author robmoffat
 */
public class ClassBasedSequenceDiagramDataProvider extends AbstractSequenceDiagramDataProvider {

	public ClassBasedSequenceDiagramDataProvider(DiagramBuilder db, Method m,
			Filter<? super AccessibleObject> limitFilter) {
		this(db.getNounFactory(), db.getProjectModel(), db.getCurrentClassLoader(), m, limitFilter, db.getAliaser());
	}
		
	Map<SimpleNoun, SimpleNoun> containers = new HashMap<SimpleNoun, SimpleNoun>();

		
	public ClassBasedSequenceDiagramDataProvider(NounFactory nf, ProjectModel pm, ClassLoader cl, Method m,
			Filter<? super AccessibleObject> limitFilter, Aliaser a) {
		super(nf, pm, cl, m, limitFilter, a);
	}

	protected SimpleNoun ensureNoun(AccessibleObject m2) {
		Class<?> declaringClass = getDeclaringClass(m2);
		Package declaringPackage = declaringClass.getPackage();
		
		SimpleNoun packageNoun = checkSimpleNoun(declaringPackage, nf.createNoun(declaringPackage));
		if (!groups.contains(packageNoun)) {
			// need to add as a participant
			groups.add(packageNoun);
		}
		
		// now create method noun
		SimpleNoun classNoun = checkSimpleNoun(declaringClass, nf.createNoun(declaringClass));
		containers.put(classNoun, packageNoun);
		
		return classNoun;
		
	}

	private Class<?> getDeclaringClass(AccessibleObject m2) {
		if (m2 instanceof Method) {
			return ((Method)m2).getDeclaringClass();
		} else if (m2 instanceof Field) {
			return ((Field)m2).getDeclaringClass();
		} else if (m2 instanceof Constructor<?>) {
			return ((Constructor<?>)m2).getDeclaringClass();
		}
		
		throw new Kite9ProcessingException("Can't process: "+m2);
	}
	

	private SimpleNoun checkSimpleNoun(Object o, NounPart np) {
		if (!(np instanceof SimpleNoun)) {
			throw new Kite9ProcessingException(this.getClass().getName()+" can only handle collaborators as SimpleNouns "+o+ " is "+np);
		}
		
		return (SimpleNoun) np;
	}
	

	protected SimpleNoun createCallStep(AccessibleObject m2) {
		SimpleNoun np = ensureNoun(m2);
		CallStep out = new CallStep(np, containers.get(np), createCallLabel(m2), null);
			if (out != null)
				steps.add(out);
		return np;
	}
	
	protected ReturnStep createReturnStep(SimpleNoun to,
			AccessibleObject m2) {
		if ((m2 instanceof Method)) {
			return new ReturnStep(null, createReturnLabel(((Method)m2).getReturnType()), (((Method)m2).getReturnType() != Void.TYPE));
		} else {
			return new ReturnStep(null, null, false);
		}
	}

}
