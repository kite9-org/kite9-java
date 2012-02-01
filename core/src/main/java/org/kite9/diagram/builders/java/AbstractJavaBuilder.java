package org.kite9.diagram.builders.java;

import java.util.LinkedHashSet;
import java.util.Set;

import org.kite9.diagram.builders.krmodel.AbstractBuilder;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.model.ProjectModel;

public abstract class AbstractJavaBuilder extends AbstractBuilder {

	protected ProjectModel model;

	public AbstractJavaBuilder(ProjectModel model, Aliaser a) {
		super(a);
		this.model = model;
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

}