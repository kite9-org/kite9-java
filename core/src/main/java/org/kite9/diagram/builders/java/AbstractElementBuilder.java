package org.kite9.diagram.builders.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.Relationship;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public abstract class AbstractElementBuilder<X> extends AbstractJavaBuilder {

	protected Set<Tie> ties;
	
	public Set<Tie> getTies() {
		return ties;
	}

	public int size() {
		return ties.size();
	}

	public AbstractElementBuilder(List<Tie> ties2, ProjectModel model, Aliaser a) {
		super(model, a);
		this.ties = new LinkedHashSet<Tie>(ties2);
	}

	public AbstractElementBuilder<X> show(PropositionFormat f) {
		for (Tie m : ties) {
			f.write(m.getSubject(), m.getRelationship(), m.getObject());
		}

		return this;
	}

	/**
	 * Creates a new element builder of the same type as the original, which
	 * only has ties matching the filter.
	 */
	public abstract AbstractElementBuilder<X> reduce(Filter<? super X> f);

	/**
	 * Use this method to implement reduce
	 */
	@SuppressWarnings("unchecked")
	protected List<Tie> reduceInner(Filter<? super X> f) {
		List<Tie> out = new ArrayList<Tie>(ties.size());
		for (Tie tie : ties) {
			if (f.accept((X) tie.getObject().getRepresented())) {
				out.add(tie);
			}
		}

		return out;
	}

	protected interface ContentSelector<T, S> {

		T[] contents(S c);

		Set<? extends S> traverse(S c);

	}

	public ObjectBuilder withObjects(Relationship r, Object... forObjects) {
		return new ObjectBuilder(createTies(ties, r, (Object[]) forObjects),
				model, a);
	}

	public ClassBuilder withClasses(Relationship r, Class<?>... forClasses) {
		return new ClassBuilder(createTies(ties, r, (Object[]) forClasses),
				model, a);
	}

	public PackageBuilder withPackages(Relationship r, Package... packages) {
		return new PackageBuilder(createTies(ties, r, (Object[]) packages),
				model, a);
	}

	public PackageBuilder withPackages(Relationship r,
			Class<?>... packagesForClasses) {
		Package[] packages = packagesOf(packagesForClasses);
		return withPackages(r, packages);
	}

	@SuppressWarnings("unchecked")
	public X getRepresented(Tie t) {
		Object out = t.getObject().getRepresented();
		return (X) out;
	}

	public Tie getTieForRepresentation(Object p) {
		for (Tie t : ties) {
			if (getRepresented(t).equals(p)) {
				return t;
			}
		}

		return null;
	}
	
	public List<Tie> createTies(Collection<Tie> old, Relationship r,
			Object... to) {
		return NounFactory.createTies(old, r, getNounFactory(), to);
	}


}
