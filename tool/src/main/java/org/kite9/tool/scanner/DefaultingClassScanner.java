package org.kite9.tool.scanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.java.ClassBuilder;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.wizards.classdiagram.ClassDiagramWizard;
import org.kite9.framework.model.ClassHandle;
import org.kite9.framework.server.BasicWorkItem;
import org.kite9.framework.server.WorkItem;

/**
 * This augments the basic spring scanner to produce a default work item if the
 * case arises where there actually wouldn't be any at all. This is so that we
 * produce at least one item for our efforts.
 */
public class DefaultingClassScanner extends BasicClassScanner {

	private boolean provideDefault = true;

	@Override
	public List<WorkItem> getItems() {
		List<WorkItem> out = super.getItems();
		
		if ((out.size() == 0) && (provideDefault)) {
			addAnnotatedWorkItems(out);
		}
		
		if ((out.size() == 0) && (provideDefault)) {
			addDefaultWorkItem(out);
		}
		return out;
	}
	
	private void addAnnotatedWorkItems(List<WorkItem> out) {
		Set<String> classes = getProjectModel().getClassesWithAnnotation(ClassHandle.convertClassName(K9OnDiagram.class));
		
		// ok, need to sort into groups
		Map<Object, Set<Class<?>>> diagramToClassMap = new HashMap<Object, Set<Class<?>>>();
		for (String string : classes) {
			Class<?> cl = ClassHandle.hydrateClass(string, getContext().getUserClassLoader());
			K9OnDiagram onDiagram = cl.getAnnotation(K9OnDiagram.class);
			Class<?>[] on = onDiagram.on();
			if ((on==null) || (on.length==0)) {
				addTo("default", cl, diagramToClassMap);
			} else {
				for (int i = 0; i < on.length; i++) {
					addTo(on[i], cl, diagramToClassMap);
				}
			}
		}
		
		for (Entry<Object, Set<Class<?>>> set : diagramToClassMap.entrySet()) {
			Class<?>[] on = (Class<?>[]) set.getValue().toArray(new Class<?>[set.getValue().size()]);
			createDefaultWorkItem(out, on, set.getKey().toString(), false);
		}
		
	}

	private void addTo(Object key, Class<?> cl,
			Map<Object, Set<Class<?>>> diagramToClassMap) {
		Set<Class<?>> items = diagramToClassMap.get(key);
		if (items==null) {
			items = new HashSet<Class<?>>();
			diagramToClassMap.put(key, items);
		}
		items.add(cl);
	}

	private void addDefaultWorkItem(List<WorkItem> out) {
		try {
			Class<?>[] items = new Class[5];
			int i = 0;
			
			for (String string : getProjectModel().getAllClasses()) {
				if (i < 5) {
					items[i++] = ClassHandle.hydrateClass(string, getContext().getUserClassLoader());
				} else {
					break;
				}
			}
			
			createDefaultWorkItem(out, items, "example", true);
		} catch (Exception e) {
			getContext().getLogger().error(
					"Could not generate a default diagram", e);
		}
	}

	private void createDefaultWorkItem(List<WorkItem> out, Class<?>[] items, String name, boolean key) {
		DiagramBuilder db = new DiagramBuilder(getContext().getAliaser(),
				"Default Diagram", getProjectModel());
		ClassDiagramWizard erw = new ClassDiagramWizard(db);

		ClassBuilder cb = db.withClasses(items);

		erw.show(cb);

		if (key) 
			db.withKeyText("Kite9 Class Diagram Example",
					"This is a default diagram created by Kite9");
		
		out.add(new BasicWorkItem(db.getDiagram(), name, "", getContext().getProjectId(), getContext().getSecretKey()));
	}
}
