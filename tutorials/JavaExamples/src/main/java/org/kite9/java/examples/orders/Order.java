package org.kite9.java.examples.orders;

import java.util.List;

import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.ClassBuilder;
import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.diagram.builders.wizards.er.EntityRelationshipWizard;
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
		EntityRelationshipWizard erf = new EntityRelationshipWizard(db);
		erf.show(classBuilder, null);
		return db.getDiagram();
	}
	
}
