package org.kite9.tool.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.ClassHelp;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.ProjectModel;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.AbstractContextualizable;
import org.kite9.tool.context.Kite9Context;

public class BasicClassScanner extends AbstractContextualizable implements
		Scanner {

	ProjectModel pm;

	String basePackage = "";

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public List<WorkItem> getItems() {
		ProjectModel pm = getProjectModel();
		Set<MemberHandle> members = pm.getMembersWithAnnotation(MemberHandle
				.convertClassName(Kite9Item.class));
		List<WorkItem> workItems = new ArrayList<WorkItem>(20);

		for (MemberHandle mh : members) {
			if (mh instanceof MethodHandle) {
				try {
					Method method = MethodHandle.hydrateMethod((MethodHandle) mh,
							getContext().getUserClassLoader());
					Class<?> testClass = method.getDeclaringClass();
					String className = testClass.getName();
	
					if ((basePackage == null)
							|| (className.startsWith(basePackage))) {
	
						String id = testClass.getName();
						String name = getContext().getAliaser().getObjectAlias(
								method);
						Object[] args = null;
						Object instance = null;
	
						if (method.getParameterTypes().length == 0) {
							args = new Object[0];
						} else if ((method.getParameterTypes().length == 1)
								&& (method.getParameterTypes()[0]
										.equals(DiagramBuilder.class))) {
							args = new Object[] { createDiagramBuilder(
									getContext(), method) };
						} else {
							throw new Kite9ProcessingException(
									"Could not determine arguments for " + id);
						}
	
						WorkItem item = null;
						if (Modifier.isStatic(method.getModifiers())) {
							Object diagram = method.invoke(null, args);
							if (diagram != null) {
								item = (WorkItem) diagram;
							}
						} else {
							instance = instance == null ? ClassHelp
									.createInstance(testClass) : instance;
							Object designItem = method.invoke(instance, args);
							if (designItem instanceof WorkItem) {
								item = (WorkItem) designItem;
							}
						}
						if (item != null) {
							workItems.add(item);
							getContext().getLogger().send(
									"Added work item: " + id + " " + name);
						} else {
							getContext().getLogger().error(
									"No work item defined for: " + id + " "
											+ name);
						}
					}
				} catch (Throwable e) {
					getContext().getLogger().error("Could not process design item for " + mh.getName(), e);
				}
			}
		}

		return workItems;

	}

	protected Object createDiagramBuilder(Kite9Context context, Method method) {
		return new DiagramBuilder(context.getAliaser(), method, pm);
	}

	public ProjectModel getProjectModel() {
		return pm;
	}

	public void setProjectModel(ProjectModel pm) {
		this.pm = pm;
	}
}
