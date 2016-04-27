package org.kite9.diagram.builders;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.java.AbstractJavaBuilder;
import org.kite9.diagram.builders.java.ClassBuilder;
import org.kite9.diagram.builders.java.ObjectBuilder;
import org.kite9.diagram.builders.java.PackageBuilder;
import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.RepresentingNoun;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.framework.model.ProjectModel;

public abstract class AbstractElementBuilder<X> extends AbstractJavaBuilder {
	
	public interface ObjectExtractor {
		
		public <Y> List<Proposition> generatePropositions(Class<Y> subjectClass, Filter<? super Y> f);		
		
	}
	
	public abstract class BasicObjectExtractor implements ObjectExtractor {
				
		public <Y> List<Proposition> generatePropositions(Class<Y> subjectClass, Filter<? super Y> f) {
			List<Proposition> ties2 = new ArrayList<Proposition>();
			for (Proposition t : ties) {
			    X m = getRepresented(t);
			    NounPart subject = getNounFactory().createNewSubjectNounPart(t);
				for (Object object : createObjects(m)) {
				    Object filterObject = getFilterObject(object);
					if (checkFilter(subjectClass, f, filterObject)) {
				    	ties2.add(createProposition(subject, getVerb(subject, object), createObjectNoun(filterObject)));
				    }
				}
			}
			return ties2;
		}
		
		public Object getFilterObject(Object from) {
			return from;
		}
		
		public NounPart createObjectNoun(Object from) {
			return nf.createNoun(from);
		}
		
		public abstract List<?> createObjects(Object from);
		
		public abstract Verb getVerb(Object subject, Object object);


	}
	

	protected final Set<Proposition> ties;
	
	public Set<Proposition> getTies() {
		return ties;
	}

	public int size() {
		return ties.size();
	}

	public AbstractElementBuilder(List<Proposition> ties2, ProjectModel model, NounFactory nf) {
		super(model, nf);
		this.ties = new LinkedHashSet<Proposition>(ties2);
	}

	public AbstractElementBuilder<X> show(Format f) {
		return show(f, ties);
	}
	
	public AbstractElementBuilder<X> show(Format f, Set<Proposition> props) {
		for (Proposition javaPropositionBinding : ties) {
			show(f, javaPropositionBinding);
		}

		return this;
	}
	
	public void show(Format f, Proposition javaPropositionBinding) {
		for (SimpleRelationship m : javaPropositionBinding.decompose()) {
			f.write(m);
		}
	}

	/**
	 * Creates a new element builder of the same type as the original, which
	 * only has ties matching the filter.
	 */
	public abstract AbstractElementBuilder<X> reduce(Filter<? super X> f);

	/**
	 * Use this method to implement reduce
	 */
	protected List<Proposition> reduceInner(Filter<? super X> f) {
		List<Proposition> out = new ArrayList<Proposition>(ties.size() * 3);
		for (Proposition p : ties) {
			X x = getRepresented(p);
			if (checkFilter(f, x)) {
				out.add(p);
			}
		}

		return out;
	}
	
	protected <V> boolean checkFilter(Filter<? super V> f, V np) {
		if (f == null) {
			return true;
		}
		
		return f.accept((V) np);
	}
	
	@SuppressWarnings("unchecked")
	protected <V> boolean checkFilter(Class<V> subjectClass, Filter<? super V> f, Object np) {
		if (f == null) {
			return true;
		}

		if (!subjectClass.isAssignableFrom(np.getClass())) {
			return false;
		}
		
		
		return f.accept((V) np);
	}

	protected interface ContentSelector<T, S> {

		T[] contents(S c);

		Set<? extends S> traverse(S c);

	}

	public ObjectBuilder withObjects(Verb r, Object... forObjects) {
		return new ObjectBuilder(createPropositions(ties, r, (Object[]) forObjects),
				model, nf);
	}

	public ClassBuilder withClasses(Verb r, Class<?>... forClasses) {
		return new ClassBuilder(createPropositions(ties, r, (Object[]) forClasses),
				model, nf);
	}

	public PackageBuilder withPackages(Verb r, Package... packages) {
		return new PackageBuilder(createPropositions(ties, r, (Object[]) packages),
				model, nf);
	}

	public PackageBuilder withPackages(Verb r,
			Class<?>... packagesForClasses) {
		Package[] packages = packagesOf(packagesForClasses);
		return withPackages(r, packages);
	}

	@SuppressWarnings("unchecked")
	public X getRepresented(Proposition t) {
		Knowledge r = t.getObject();
		if (r instanceof RepresentingNoun) {
			return (X) ((RepresentingNoun) r).getRepresented();
		}

		return null;
	}

	public Proposition getTieForRepresentation(Object p) {
		for (Proposition t : ties) {
			if (getRepresented(t).equals(p)) {
				return t;
			}
		}

		return null;
	}

}
