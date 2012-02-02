package org.kite9.java.examples.orders;

import java.util.List;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.java.ClassBuilder;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.wizards.classdiagram.ClassDiagramWizard;
import org.kite9.framework.Kite9Item;

@K9OnDiagram(on=Order.class)
public abstract class Order {
	
	List<OrderLine> lines;
	float grandTotal;

	@K9OnDiagram
	static class OrderLine {
		
		public int quantity;
		public StockItem stockItem;
		
	}
	
	@Kite9Item
	public static Diagram orderEntityRelationshipDiagramb(final DiagramBuilder db) {
		ClassBuilder classBuilder = db.withAnnotatedClasses();
		ClassDiagramWizard erf = new ClassDiagramWizard(db);
		erf.show(classBuilder);
		return db.getDiagram();
	}
	
}
