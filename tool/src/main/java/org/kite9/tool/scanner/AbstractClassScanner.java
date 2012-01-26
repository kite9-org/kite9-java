package org.kite9.tool.scanner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.ClassHelp;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.ProjectModel;
import org.kite9.framework.server.BasicWorkItem;
import org.kite9.framework.server.WorkItem;
import org.kite9.tool.context.AbstractContextualizable;
import org.kite9.tool.context.Kite9Context;

public abstract class AbstractClassScanner extends AbstractContextualizable implements Scanner {

	ProjectModel pm;

	/**
	 * Returns the Test corresponding to the given suite. This is a template
	 * method, subclasses override runFailed(), clearStatus().
	 */
	public List<WorkItem> getWorkItems(String className) {
		List<WorkItem> workItems = new ArrayList<WorkItem>(20);

		if (className.length() <= 0) {
			return null;
		}

		Class<?> testClass = null;
		try {
			testClass = ClassHelp.loadClass(className, getContext().getUserClassLoader());
		} catch (Exception e) {
			getContext().getLogger().error("Could not process design items for " + className, e);
			return Collections.emptyList();
		}
		Object instance = null;
		Method[] methods = null;
		
		try {
			methods = testClass.getDeclaredMethods();
		} catch (Throwable e) {
			getContext().getLogger().error("Could not find methods for " + className);	
			return Collections.emptyList();
		}

		for (Method method : methods) {
			if (method.getAnnotation(Kite9Item.class) != null) {
				String id = testClass.getName();
				String name = getContext().getAliaser().getObjectAlias(method);
				Object[] args = null;

				if (method.getParameterTypes().length == 0) {
					args = new Object[0];
				} else if ((method.getParameterTypes().length == 1)
						&& (method.getParameterTypes()[0].equals(DiagramBuilder.class))) {
					args = new Object[] { createDiagramBuilder(getContext(), method) };
				} else {
					throw new Kite9ProcessingException("Could not determine arguments for " + id);
				}

				WorkItem item = null;
				try {
					if (Modifier.isStatic(method.getModifiers())) {
						Object diagram = method.invoke(null, args);
						if (diagram != null) {
							item = new BasicWorkItem(diagram, name, id, getContext().getProjectId(),
									getContext().getSecretKey());
						}
					} else {
						instance = instance == null ? ClassHelp.createInstance(testClass) : instance;
						Object designItem = method.invoke(instance, args);
						if (designItem != null) {
							item = new BasicWorkItem(designItem, name, id, getContext().getProjectId(), getContext()
									.getSecretKey());
						}
					}
					if (item != null) {
						workItems.add(item);
						getContext().getLogger().send("Added work item: " + id + " " + name);
					} else {
						getContext().getLogger().error("No work item defined for: " + id + " " + name);						
					}
				} catch (Exception e) {
					getContext().getLogger().error("Could not process design items for " + id + " " + name, e);
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
