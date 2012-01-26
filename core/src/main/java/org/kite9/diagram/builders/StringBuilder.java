package org.kite9.diagram.builders;

import java.util.List;

import org.kite9.diagram.builders.formats.Format;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public class StringBuilder extends AbstractElementBuilder<String>{

    public StringBuilder(List<Tie> ties2, ProjectModel model, Aliaser a) {
	super(ties2, model, a);
    }

    @Override
    public StringBuilder reduce(Filter<? super String> f) {
	return (StringBuilder) reduceInner(f);
    }

    public StringBuilder show(Format f) {
	return (StringBuilder) super.show(f);
    }
}
