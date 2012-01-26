package org.kite9.diagram.builders.wizards.fsm;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Set;

import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.diagram.builders.noun.NounFactory;
import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.diagram.builders.noun.SimpleNoun;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.FieldHandle;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.ProjectModel;

/**
 * This class can be used where your state transition methods are annotated with custom before and after annotations.
 * 
 * @author robmoffat
 * 
 */
public class EnumWithAnnotationFSMDataProvider implements FSMDataProvider {

	private Class<? extends Enum<?>> enumClass;
	private ProjectModel pm;
	private Field f;
	Class<? extends Annotation> beforeAnnotation;
	String beforeField;
	Class<? extends Annotation> afterAnnotation;
	String afterField;
	ClassLoader cl;
	NounFactory nf;

	/**
	 * Creates a FSMDataProvider which examines methods having a state change, and before / after 
	 * state change annotations.
	 * @param db  Diagram Builder (used for project model and noun factory)
	 * @param f Field which state change occurs on
	 * @param enumClass Class of the field
	 * @param beforeAnnotation Annotation used to mark up 'before' states (field containing those states is assumed to be an array of the enumeration, and called 'value'.
	 * @param afterAnnotation Annotation used to mark up 'after' states (field containing those states is assumed to be an array of the enumeration, and called 'value'.
	 */
	public EnumWithAnnotationFSMDataProvider(DiagramBuilder db, Field f,
			Class<? extends Enum<?>> enumClass, Class<? extends Annotation> beforeAnnotation,
			Class<? extends Annotation> afterAnnotation) {
		this(db.getNounFactory(), db.getProjectModel(), f, enumClass, db.getCurrentClassLoader(), beforeAnnotation, "value", afterAnnotation, "value");
	}


	/**
	 * This more involved constructor allows you to choose a noun factory and project model, and
	 * use annotation fields other than the default 'value' field.
	 */
	public EnumWithAnnotationFSMDataProvider(NounFactory nf, ProjectModel pm, Field f,
			Class<? extends Enum<?>> enumClass, ClassLoader cl, Class<? extends Annotation> beforeAnnotation,
			String beforeField, Class<? extends Annotation> afterAnnotation, String afterField) {
		this.enumClass = enumClass;
		this.pm = pm;
		this.f = f;
		this.cl = cl;
		this.beforeAnnotation = beforeAnnotation;
		this.afterAnnotation = afterAnnotation;
		this.beforeField = beforeField;
		this.afterField = afterField;
		this.nf = nf;
	}

	public SimpleNoun[] getStates() {
		SimpleNoun[] out = new SimpleNoun[enumClass.getEnumConstants().length];
		int i = 0;
		for (Enum<?> e : enumClass.getEnumConstants()) {
			out[i++] = checkSimpleNoun(nf.createNoun(e));
		}
		return out;
	}

	public Transition[] getTransitions() {
		Set<MemberHandle> handles = pm.getCalledBy(new FieldHandle(f));
		Transition[] out = new Transition[handles.size()];
		int i = 0;
		for (MemberHandle handle : handles) {
			out[i++] = createTransition(handle);
		}

		return out;
	}

	protected Transition createTransition(MemberHandle handle) {
		if (handle instanceof MethodHandle) {
			Method m = ((MethodHandle) handle).hydrate(cl);
			SimpleNoun[] from = getDefinedNouns(m, beforeAnnotation, beforeField);
			SimpleNoun[] to = getDefinedNouns(m, afterAnnotation, afterField);
			if ((from!=null) || (to!=null)) {
				return new Transition(from, to, getTransitionObject(m));
			}
		} 
		
		return null;
	}

	protected SimpleNoun[] getDefinedNouns(Member m, Class<? extends Annotation> annotation, String field) {
		Object[] df = null;
		
		if (m instanceof AnnotatedElement) {
			df = getDefinedValues((AnnotatedElement) m, annotation, field);
		}
		
		if (df == null) {
			return null;
//			throw new Kite9ProcessingException("No annotation for: "+m.getName()+ ", on member="+m+" annotation="+annotation.getName()+", field="+field+".  Is retention set to RUNTIME?");
		}
		
		SimpleNoun[] out = new SimpleNoun[df.length];
		for (int i = 0; i < out.length; i++) {
			NounPart np = nf.createNoun(df[i]);
			out[i] = checkSimpleNoun(np);
		}

		return out;
	}

	protected Enum<?>[] getDefinedValues(AnnotatedElement m, Class<? extends Annotation> annotation, String field) {
		Annotation a = m.getAnnotation(annotation);
		if (a == null) {
			return null;
		}

		try {
			Method m2 = a.getClass().getDeclaredMethod(field);
			Object o = m2.invoke(a);
			if ((o.getClass().isArray()) && (Enum.class.isAssignableFrom(o.getClass().getComponentType()))) {
				return (Enum<?>[]) o;
			} else {
				throw new Kite9ProcessingException("Was expecting enumeration array for " + field + " on " + annotation
						+ " but was " + o.getClass());
			}
		} catch (Exception e) {
			throw new Kite9ProcessingException("Could not get annotation values for " + m + " with field " + field, e);
		}
	}

	protected SimpleNoun getTransitionObject(Object m) {
		NounPart np = nf.createNoun(m);
		return checkSimpleNoun(np);
	}

	private SimpleNoun checkSimpleNoun(NounPart np) {
		if (np instanceof SimpleNoun) {
			return (SimpleNoun) np;
		} else {
			throw new Kite9ProcessingException(
					"Noun factory must create a SimpleNoun for the FiniteStateMachineFormat" + np);
		}
	}
}
