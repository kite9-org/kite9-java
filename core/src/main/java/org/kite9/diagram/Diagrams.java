package org.kite9.diagram;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.wizards.classdiagram.ClassDiagramWizard;
import org.kite9.diagram.builders.wizards.hierarchy.HierarchyWizard;
import org.kite9.diagram.primitives.DiagramElement;
import org.kite9.framework.Kite9Item;

public class Diagrams {

	@Kite9Item
	public Diagram ADLClassDiagram(DiagramBuilder db) {
		ClassDiagramWizard cdw = new ClassDiagramWizard(db);
		cdw.show(db.withPackages(Diagram.class, DiagramElement.class).withMemberClasses(null));
		return db.getDiagram();
	}
	
	@Kite9Item
	public Diagram ADLClassHierarchy(DiagramBuilder db) {
		HierarchyWizard hw = new HierarchyWizard(null, db);
		hw.show(db.withPackages(Diagram.class, DiagramElement.class).withMemberClasses(null));
		return db.getDiagram();
	}
}
