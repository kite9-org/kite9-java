package org.kite9.diagram.builders.krmodel;

import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.IdHelper;
import org.kite9.diagram.builders.WithHelperMethodsDiagramBuilder;
import org.kite9.framework.alias.Aliaser;

/**
 * This class contains basic functionality for managing nouns and ties, that all
 * builders will need.
 * 
 * @author robmoffat
 * 
 */
public abstract class KRDiagramBuilder extends WithHelperMethodsDiagramBuilder {

	protected Aliaser a;

	
	public KRDiagramBuilder(String id, IdHelper helper, Aliaser a) {
		super(id, helper);
		this.a = a;
	}

	public NounPart createNoun(Object o) {
		return getNounFactory().createNoun(o);
	}

	public abstract NounFactory getNounFactory();

	public Aliaser getAliaser() {
		return a;
	}
	
	protected List<Tie> createTies(Set<Tie> old, Relationship r,
			Object... items) {
		return NounFactory.createTies(old, r, getNounFactory(), items);
	}

	@Override
	public boolean isOnDiagram(Object o) {
		if (super.isOnDiagram(o)) {
			return true;
		} else {
			return super.isOnDiagram(getNounFactory().createNoun(o));
		}
	}

	
	
}