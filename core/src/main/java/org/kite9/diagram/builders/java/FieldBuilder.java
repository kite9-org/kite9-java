package org.kite9.diagram.builders.java;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.diagram.builders.krmodel.verb.Verb;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public class FieldBuilder extends AnnotatedElementBuilder<Field> {

	public FieldBuilder(List<Proposition> ties, ProjectModel model, NounFactory a) {
		super(ties, model, a);
	}

	public FieldBuilder show(Format f) {
		return (FieldBuilder) super.show(f);
	}

	public TypeBuilder withType(Filter<? super Field> f) {
		return new TypeBuilder(new BasicObjectExtractor() {
			
			@Override
			public Verb getVerb(Object subject, Object object) {
				return JavaRelationships.HAS_TYPE;
			}
			
			@Override
			public List<?> createObjects(Object from) {
				return Arrays.asList(((Field)from).getGenericType());
			}
		}.generatePropositions(Field.class, f), model, nf);
	}

	public FieldBuilder showVisibility(Format f) {
		for (Proposition t : ties) {
			Field m = getRepresented(t);
			NounPart sub = getNounFactory().extractObject(t);
			if (Modifier.isPublic(m.getModifiers())) {
				show(f, new JavaPropositionBinding(sub, JavaRelationships.IS_VISIBILITY, createNoun(new JavaModifier("public"))));
			} else if (Modifier.isPrivate(m.getModifiers())) {
				show(f, new JavaPropositionBinding(sub, JavaRelationships.IS_VISIBILITY, createNoun(new JavaModifier("private"))));
			} else if (Modifier.isProtected(m.getModifiers())) {
				show(f, new JavaPropositionBinding(sub, JavaRelationships.IS_VISIBILITY, createNoun(new JavaModifier("protected"))));
			}
		}
		return this;
	}

	@Override
	public FieldBuilder reduce(Filter<? super Field> f) {
		return new FieldBuilder(reduceInner(f), model, nf);
	}
}
