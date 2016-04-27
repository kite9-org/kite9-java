package org.kite9.diagram.builders.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

import org.kite9.diagram.builders.AbstractElementBuilder;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.framework.model.ProjectModel;

public class AnnotatedElementBuilder<X extends AnnotatedElement> extends AbstractElementBuilder<X> {

	public AnnotatedElementBuilder(List<Proposition> ties, ProjectModel model, NounFactory a) {
		super(ties, model, a);
	}

	public AnnotationBuilder withAnnotations(Filter<? super Annotation> f) {
		return new AnnotationBuilder(new BasicObjectExtractor() {
			
			public List<?> createObjects(Object from) {
				return Arrays.asList(((AnnotatedElement)from).getAnnotations());
			}

			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.ANNOTATION;
			}
			
		}.generatePropositions(Annotation.class, f), model, nf);
	}

	@Override
	public AnnotatedElementBuilder<X> reduce(Filter<? super X> f) {
		return new AnnotatedElementBuilder<X>(reduceInner(f), model, nf);
	}

}
