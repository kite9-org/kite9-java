package org.kite9.diagram.builders.java;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public class FieldValueBuilder extends AbstractElementBuilder<FieldValue> {

	public FieldValueBuilder(List<Tie> ties, ProjectModel model, Aliaser a) {
		super(ties, model, a);
	}

	public FieldValueBuilder show(PropositionFormat f) {
		return (FieldValueBuilder) super.show(f);
	}

	public TypeBuilder withType(Filter<? super Field> f) {
		List<Tie> ties2 = new ArrayList<Tie>();

		for (Tie t : ties) {
			FieldValue fv = getRepresented(t);
			Field m = fv.getField();
			if (f == null || f.accept(m)) {
				ties2.add(new Tie(NounFactory.createNewSubjectNounPart(t),
						JavaRelationships.HAS_TYPE, createNoun(m
								.getGenericType())));
			}
		}
		return new TypeBuilder(ties2, model, a);
	}

	public ObjectBuilder withValues(Filter<? super Object> f) {
		List<Tie> ties2 = new ArrayList<Tie>();

		for (Tie t : ties) {
			FieldValue fv = getRepresented(t);
			Object o = fv.getValue();
				Set<NounPart> nouns = new ObjectNounHelper().generateNouns(o, getAliaser());
				for (NounPart nounPart : nouns) {
					if ((f == null || f.accept(nounPart))) {
					ties2.add(new Tie(NounFactory.createNewSubjectNounPart(t),
							JavaRelationships.REFERENCES, nounPart));
					}
			}	
		}
		return new ObjectBuilder(ties2, model, a);
	}

	@Override
	public FieldValueBuilder reduce(Filter<? super FieldValue> f) {
		return new FieldValueBuilder(reduceInner(f), model, a);
	}
}
