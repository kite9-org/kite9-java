package org.kite9.java.examples.orders;

import java.util.Set;

import org.kite9.diagram.annotation.K9OnDiagram;

@K9OnDiagram(on=Order.class)
public class Customer {

	public Set<Order> completedOrders;
	public Set<RegularOrder> openRegularOrders;
}
