package org.kite9.diagram.builders.wizards.sequence;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * This class examines the method-call hierarchy and produces a sequence diagram, where 
 * each class in the diagram is a container, and each method is a heading within a container
 * based on this.  
 * 
 * @author robmoffat
 */
public class MethodBasedSequenceDiagramDataProvider extends AbstractSequenceDiagramDataProvider {

	public MethodBasedSequenceDiagramDataProvider(DiagramBuilder db, Method m) {
		super(db, m);
	}
	
	Map<SimpleNoun, SimpleNoun> containers = new HashMap<SimpleNoun, SimpleNoun>(); 

	protected ReturnStep createReturnStep(SimpleNoun to,
			AccessibleObject m2) {
		if ((m2 instanceof Method)) {
			return new ReturnStep(null, createReturnLabel(((Method)m2).getReturnType()), (((Method)m2).getReturnType() != Void.TYPE));
		} else {
			return new ReturnStep(null, null, false);
		}
	}

	protected SimpleNoun ensureNoun(AccessibleObject m2) {
		Class<?> declaringClass = getDeclaringClass(m2);
		SimpleNoun classNoun = checkSimpleNoun(declaringClass, nf.createNoun(declaringClass));
		if (!groups.contains(classNoun)) {
			// need to add as a participant
			groups.add(classNoun);
		}
		
		// now create method noun
		SimpleNoun methodNoun = checkSimpleNoun(m2, nf.createNoun(m2));
		containers.put(methodNoun, classNoun);
		
		return methodNoun;
		
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


}
