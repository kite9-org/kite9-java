package org.kite9.diagram.builders.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public class AnnotatedElementBuilder<X extends AnnotatedElement> extends AbstractElementBuilder<X> {

	public AnnotatedElementBuilder(List<Tie> ties, ProjectModel model, Aliaser a) {
		super(ties, model, a);
	}

	@SuppressWarnings("unchecked")
	public AnnotationBuilder withAnnotations(Filter<? super Annotation> f) {
		List<Tie> annotations = new ArrayList<Tie>();
		for (Tie t : ties) {
			X anEl = (X) t.getObject().getRepresented();
			Annotation[] anns = anEl.getAnnotations();
			NounPart subject = NounFactory.createNewSubjectNounPart(t);
			for (Annotation annotation : anns) {
				if ((f == null) || (f.accept(annotation))) {
					NounPart object = createNoun(annotation);
					annotations.add(new Tie(subject, JavaRelationships.ANNOTATION, object));
				}
			}
		}

		return new AnnotationBuilder(annotations, model, a);
	}

	@Override
	public AnnotatedElementBuilder<X> reduce(Filter<? super X> f) {
		return new AnnotatedElementBuilder<X>(reduceInner(f), model, a);
	}

}
