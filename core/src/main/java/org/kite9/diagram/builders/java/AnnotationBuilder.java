package org.kite9.diagram.builders.java;

import java.lang.annotation.Annotation;
import java.util.List;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public class AnnotationBuilder extends AbstractElementBuilder<Annotation> {

    public AnnotationBuilder(List<Tie> forX, ProjectModel model, Aliaser a) {
    	super(forX, model, a);
    }

    public AnnotationBuilder show(PropositionFormat f) {
    	return (AnnotationBuilder) super.show(f);
    }
    
    
    @Override
    public AnnotationBuilder reduce(Filter<? super Annotation> f) {
    	return new AnnotationBuilder(reduceInner(f), model, a);
    }
    
}
