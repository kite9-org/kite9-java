package org.kite9.diagram.builders.wizards.sequence;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.adl.TextLine;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.SimpleNoun;
import org.kite9.diagram.primitives.Label;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.ProjectModel;

/**
 * Code for creating a call tree from a method description held in the project model.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractSequenceDiagramDataProvider implements SequenceDiagramDataProvider {


	public AbstractSequenceDiagramDataProvider(DiagramBuilder db, Method m,
			Filter<? super AccessibleObject> limitFilter) {
		this(db.getNounFactory(), db.getProjectModel(), db.getCurrentClassLoader(), m, limitFilter, db.getAliaser());
	}

	protected NounFactory nf;
	protected ProjectModel pm;
	protected Method m;
	protected Filter<? super AccessibleObject> limitFilter;
	protected List<SimpleNoun> groups = new ArrayList<SimpleNoun>();
	protected List<Step> steps = new ArrayList<Step>();
	protected ClassLoader cl;
	protected Aliaser a;

	public AbstractSequenceDiagramDataProvider(NounFactory nf, ProjectModel pm, ClassLoader cl, Method m,
			Filter<? super AccessibleObject> limitFilter, Aliaser a) {
		this.nf = nf;
		this.pm = pm;
		this.m = m;
		this.limitFilter = limitFilter;
		this.cl = cl;
		this.a = a;
	}

	protected void buildSteps(SimpleNoun caller, AccessibleObject m2) {
		SimpleNoun np = createCallStep(m2);
	
		if (m2 instanceof Method) {
			List<MemberHandle> handles = pm.getCalls(new MethodHandle((Method) m2));

			for (MemberHandle memberHandle : handles) {
				AccessibleObject ao = memberHandle.hydrate(cl);
				if ((limitFilter==null) || (limitFilter.accept(ao))) {
					buildSteps(np, ao);
				}
			}
		}

		if (caller != null) {
			// create return arrow
			ReturnStep back = createReturnStep(caller, m2);
			if (back != null)
				steps.add(back);
		}
	}

	protected abstract SimpleNoun createCallStep(AccessibleObject m2);

	protected Label createCallLabel(AccessibleObject m2) {
		if (m2 instanceof Method) {
			return new TextLine(a.getObjectAlias(m2)+"()");
		} else if (m2 instanceof Field) {
			return new TextLine("get "+a.getObjectAlias(m2));
		} else if (m2 instanceof Constructor<?>) {
			return new TextLine("<new>");
		}
		
		throw new Kite9ProcessingException("Can't process: "+m2);
	}

	protected abstract ReturnStep createReturnStep(SimpleNoun to,
			AccessibleObject m2);

	protected Label createReturnLabel(Class<?> returnType) {
		String label = a.getObjectAlias(returnType);
		return new TextLine("returns "+label);
	}

	
	

	public List<Step> getSteps() {
		if (steps.size()==0) {
			buildSteps(null, m);
		}
		return steps;
	}

	
}
