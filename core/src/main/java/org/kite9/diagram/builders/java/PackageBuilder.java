package org.kite9.diagram.builders.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.krmodel.NounFactory;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Tie;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.PackageHandle;
import org.kite9.framework.model.ProjectModel;

public class PackageBuilder extends AnnotatedElementBuilder<Package> {
       
    public PackageBuilder(List<Tie> packages, ProjectModel model, Aliaser a) {
	super(packages, model, a);
    }
    
    public PackageBuilder show(PropositionFormat f ) {
	return (PackageBuilder) super.show(f);
    }

    
    public ClassBuilder withMembers(Class<?>... classes) {
	List<Tie> ties2 = new ArrayList<Tie>(classes.length);
	for (Class<?> c : classes) {
	    Package p = c.getPackage();
	    Tie t = getTieForRepresentation(p);
	    if (t!=null) {
		ties2.add(new Tie(NounFactory.createNewSubjectNounPart(t), JavaRelationships.CLASS, createNoun(c)));
	    }
	}
	ClassBuilder ch = new ClassBuilder(ties2, model, a);
	return ch;
    }
    
    public ClassBuilder withMemberClasses(Filter<? super Class<?>> f) {
	List<Tie> ties2 = new ArrayList<Tie>(10);
	final ClassLoader cl = Thread.currentThread().getContextClassLoader();

	for (Tie t : ties) {
	    NounPart sub = NounFactory.createNewSubjectNounPart(t);
	    Package p = getRepresented(t);
	    Set<String> classNames = model.getClassesInPackage(MemberHandle.convertPackageName(p));
	    for (Class<?> c : MemberHandle.hydrateClasses(classNames, cl)) {
		if ((f==null) || (f.accept(c))) {
		    ties2.add(new Tie(sub, JavaRelationships.CLASS, createNoun(c)));
		}
	    }
	}
	ClassBuilder ch = new ClassBuilder(ties2, model, a);
	return ch;	
    }
    
    public PackageBuilder withDependencies(Filter<? super Package> f) {
    	List<Tie> ties2 = new ArrayList<Tie>(10);
    	final ClassLoader cl = Thread.currentThread().getContextClassLoader();

    	for (Tie t : ties) {
    	    NounPart sub = NounFactory.createNewSubjectNounPart(t);
    	    Package p = getRepresented(t);
    	    Set<PackageHandle> packNames = model.getDependsOnPackages(new PackageHandle(MemberHandle.convertPackageName(p), null));
    	    for (Package p2 : MemberHandle.hydratePackages(packNames, cl)) {
    		if ((f==null) || (f.accept(p2))) {
    		    ties2.add(new Tie(sub, JavaRelationships.REQUIRES, createNoun(p2)));
    		}
    	    }
    	}
    	PackageBuilder ch = new PackageBuilder(ties2, model, a);
    	return ch;	
        }
    
    @Override
    public PackageBuilder reduce(Filter<? super Package> f) {
	return new PackageBuilder(reduceInner(f), model, a);
    }
}
