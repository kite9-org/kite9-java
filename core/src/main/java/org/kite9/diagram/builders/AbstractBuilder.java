package org.kite9.diagram.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.noun.AnnotatedNounPart;
import org.kite9.diagram.builders.noun.BasicNounFactory;
import org.kite9.diagram.builders.noun.NounFactory;
import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.diagram.builders.noun.OwnedNoun;
import org.kite9.diagram.builders.noun.OwnedNounImpl;
import org.kite9.diagram.builders.noun.SimpleNoun;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.ProjectModel;

public abstract class AbstractBuilder {

    protected ProjectModel model;
    protected Aliaser a;
    protected NounFactory nf;

    public AbstractBuilder(ProjectModel model, Aliaser a) {
	super();
	this.model = model;
	this.a = a;
    }

    protected Package[] packagesOf(Class<?>... packagesForClasses) {
	Set<Package> packages = new LinkedHashSet<Package>();
	for (Class<?> c : packagesForClasses) {
	    packages.add(c.getPackage());
	}
	return (Package[]) packages.toArray(new Package[packages.size()]);
    }

    public ClassLoader getCurrentClassLoader() {
	return Thread.currentThread().getContextClassLoader();
    }

    protected List<Tie> createTies(Collection<Tie> old, Relationship r, Object... items) {
	List<Tie> ties = new ArrayList<Tie>(items.length * old.size());
	for (Tie item : old) {
	    for (int i = 0; i < items.length; i++) {
		ties.add(new Tie(createNewSubjectNounPart(item), r, createNoun(items[i])));
	    }
	}

	return ties;
    }
    
    public NounPart createNoun(Object o) {
		return getNounFactory().createNoun(o);
	}

    public SimpleNoun getUnderlyingSimpleNoun(NounPart in) {
	if (in == null)
	    return null;

	if (in instanceof SimpleNoun) {
	    return (SimpleNoun) in;
	}

	if (in instanceof OwnedNoun) {
	    return ((OwnedNoun) in).getOwned();
	}

	if (in instanceof AnnotatedNounPart) {
	    return getUnderlyingSimpleNoun(((AnnotatedNounPart) in).getNounPart());
	}

	throw new Kite9ProcessingException("Can't process this noun" + in);

    }

    protected NounPart createNewSubjectNounPart(Tie t) {
	if (t == null)
	    return null;

	SimpleNoun originalSubject = getUnderlyingSimpleNoun(t.getSubject());
	SimpleNoun originalObject = getUnderlyingSimpleNoun(t.getObject());
	if (originalSubject == null) {
	    return originalObject;
	}

	if (t.getRelationship() instanceof HasRelationship) {
	    return new OwnedNounImpl(originalSubject, originalObject);
	} else {
	    return originalObject;
	}
    }

    public NounFactory getNounFactory() {
		if (nf==null) {
			nf = new BasicNounFactory(a);
		}

    	return nf;
    }

}