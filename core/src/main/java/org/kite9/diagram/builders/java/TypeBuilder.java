package org.kite9.diagram.builders.java;

import java.lang.reflect.Type;
import java.util.List;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.Tie;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

/**
 * This is a bit like a ClassBuilder, but handles generic types.  This is useful for example
 * when dealing with fields and method return types.
 * 
 * @author moffatr
 *
 */
public class TypeBuilder extends AbstractElementBuilder<Type>{

    public TypeBuilder(List<Tie> ties2, ProjectModel model, Aliaser a) {
	super(ties2, model, a);
    }

    @Override
    public TypeBuilder reduce(Filter<? super Type> f) {
	return (TypeBuilder) reduceInner(f);
    }

    public TypeBuilder show(PropositionFormat f) {
	return (TypeBuilder) super.show(f);
    }

}
