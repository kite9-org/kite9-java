package org.kite9.tool.scanner;

import java.util.List;

import org.kite9.diagram.builders.ClassBuilder;
import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.diagram.builders.wizards.er.EntityRelationshipWizard;
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
			addDefaultWorkItem(out);
		}
		return out;
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
			
			DiagramBuilder db = new DiagramBuilder(getContext().getAliaser(),
					"Default Diagram", getProjectModel());
			EntityRelationshipWizard erw = new EntityRelationshipWizard(db);

			ClassBuilder cb = db.withClasses(items);

			erw.showGlyphs(cb);
			// erw.showRelationships(cb, null);

			db.withKeyText("Kite9 Class Diagram Example",
					"This is a default diagram created by Kite9");
			
			out.add(new BasicWorkItem(db.getDiagram(), "example", "", getContext().getProjectId(), getContext().getSecretKey()));
		} catch (Exception e) {
			getContext().getLogger().error(
					"Could not generate a default diagram", e);
		}
	}
}