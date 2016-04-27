package org.kite9.diagram.builders.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.java.krmodel.JavaPropositionBinding;
import org.kite9.diagram.builders.java.krmodel.JavaRelationships;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.Proposition;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.PackageHandle;
import org.kite9.framework.model.ProjectModel;

public class PackageBuilder extends AnnotatedElementBuilder<Package> {

	public PackageBuilder(List<Proposition> packages, ProjectModel model, NounFactory a) {
		super(packages, model, a);
	}

	public PackageBuilder show(Format f) {
		return (PackageBuilder) super.show(f);
	}

	public ClassBuilder withMembers(Class<?>... classes) {
		List<Proposition> ties2 = new ArrayList<Proposition>(classes.length);
		for (Class<?> c : classes) {
			Package p = c.getPackage();
			Proposition t = getTieForRepresentation(p);
			if (t != null) {
				ties2.add(new JavaPropositionBinding(getNounFactory().extractObject(t), JavaRelationships.IS_CLASS, createNoun(c)));
			}
		}
		ClassBuilder ch = new ClassBuilder(ties2, model, nf);
		return ch;
	}

	public ClassBuilder withMemberClasses(Filter<? super Class<?>> f) {
		List<Proposition> ties2 = new ArrayList<Proposition>(10);
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();

		for (Proposition t : ties) {
			NounPart sub = getNounFactory().extractObject(t);
			Package p = getRepresented(t);
			Set<String> classNames = model.getClassesInPackage(MemberHandle.convertPackageName(p));
			for (Class<?> c : MemberHandle.hydrateClasses(classNames, cl)) {
				if ((f == null) || (f.accept(c))) {
					ties2.add(new JavaPropositionBinding(sub, JavaRelationships.IS_CLASS, createNoun(c)));
				}
			}
		}
		ClassBuilder ch = new ClassBuilder(ties2, model, nf);
		return ch;
	}

	public PackageBuilder withDependencies(Filter<? super Package> f) {
		List<Proposition> ties2 = new ArrayList<Proposition>(10);
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();

		for (Proposition t : ties) {
			NounPart sub = getNounFactory().createNewSubjectNounPart(t);
			Package p = getRepresented(t);
			Set<PackageHandle> packNames = model.getDependsOnPackages(new PackageHandle(MemberHandle.convertPackageName(p), null));
			for (Package p2 : MemberHandle.hydratePackages(packNames, cl)) {
				if ((f == null) || (f.accept(p2))) {
					ties2.add(new JavaPropositionBinding(sub, JavaRelationships.REQUIRES, createNoun(p2)));
				}
			}
		}
		PackageBuilder ch = new PackageBuilder(ties2, model, nf);
		return ch;
	}

	@Override
	public PackageBuilder reduce(Filter<? super Package> f) {
		return new PackageBuilder(reduceInner(f), model, nf);
	}
}
