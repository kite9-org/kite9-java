package org.kite9.diagram.builders.java;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public class FieldBuilder extends AnnotatedElementBuilder<Field>{
        
    public FieldBuilder(List<Tie> ties,ProjectModel model, Aliaser a) {
	super(ties, model, a);
    }
    
    public FieldBuilder show(PropositionFormat f ) {
	return (FieldBuilder) super.show(f);
    }
    
    
    public TypeBuilder withType(Filter<? super Field> f) {
	List<Tie> ties2 = new ArrayList<Tie>();

	for (Tie t : ties) {
	    Field m = getRepresented(t);
	    if (f==null || f.accept(m)) {
		ties2.add(new Tie(NounFactory.createNewSubjectNounPart(t), JavaRelationships.HAS_TYPE, createNoun(m.getGenericType())));
	    }
	}
	return new TypeBuilder(ties2, model, a);
}

	public FieldBuilder showVisibility(PropositionFormat f) {
		for (Tie t : ties) {
		    Field m = getRepresented(t);
		    NounPart sub = NounFactory.createNewSubjectNounPart(t);
			if (Modifier.isPublic(m.getModifiers())) {
				f.write(sub, JavaRelationships.VISIBILITY, createNoun(new JavaModifier("public")));
			} else if (Modifier.isPrivate(m.getModifiers())) {
				f.write(sub, JavaRelationships.VISIBILITY, createNoun(new JavaModifier("private")));
			} else if (Modifier.isProtected(m.getModifiers())) {
				f.write(sub, JavaRelationships.VISIBILITY, createNoun(new JavaModifier("protected")));
			}
		}
		return this;
	}

    @Override
    public FieldBuilder reduce(Filter<? super Field> f) {
	return new FieldBuilder(reduceInner(f), model, a);
    }
}
