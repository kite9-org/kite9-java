package org.kite9.diagram.builders.java;

import java.lang.annotation.Annotation;
import java.util.List;

import org.kite9.diagram.builders.AbstractElementBuilder;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.framework.model.ProjectModel;

public class AnnotationBuilder extends AbstractElementBuilder<Annotation> {

    public AnnotationBuilder(List<Proposition> forX, ProjectModel model, NounFactory a) {
    	super(forX, model, a);
    }

    public AnnotationBuilder show(Format f) {
    	return (AnnotationBuilder) super.show(f);
    }
    
    
    @Override
    public AnnotationBuilder reduce(Filter<? super Annotation> f) {
    	return new AnnotationBuilder(reduceInner(f), model, nf);
    }
    
}
