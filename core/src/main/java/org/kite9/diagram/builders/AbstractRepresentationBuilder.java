package org.kite9.diagram.builders;

import org.kite9.diagram.builders.java.AbstractJavaBuilder;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.representation.Representation;
import org.kite9.framework.model.ProjectModel;

/**
 * Contains functionality to put together the diagram object model.
 * 
 * It keeps track of what items of knowledge have been added to the diagram, and
 * the diagram element representing that knowledge. The insertion interface is
 * used to specify this.
 * 
 * Note that this extends AbstractBuilder just for convenience later on.  It doesn't use 
 * any of its methods.
 */
public abstract class AbstractRepresentationBuilder<K> extends AbstractJavaBuilder {

	protected Representation<K> representation;
	protected NounPart theDiagramNoun;
	
	public AbstractRepresentationBuilder(ProjectModel pm, NounPart theDiagram, NounFactory nf) {
		super(pm, nf);
		this.representation = createRepresentation(theDiagram);
		this.theDiagramNoun = theDiagram;
	}

	public Representation<K> getRepresentation() {
		return representation;
	}

	protected abstract Representation<K> createRepresentation(NounPart theDiagram);

	@Override
	public NounFactory getNounFactory() {
		return nf;
	}
	
	public boolean isOnDiagram(Object o) {
		if (!(o instanceof NounPart)) {
			o = nf.createNoun(o);
		}
		return representation.contains((NounPart) o);
	}

	/**
	 * Filters to just the items already on the diagram
	 */
	public Filter<Object> onlyOnDiagram() {
		return new Filter<Object>() {
	
			public boolean accept(Object o) {
				return isOnDiagram(o);
			}
		};
	}
	
}