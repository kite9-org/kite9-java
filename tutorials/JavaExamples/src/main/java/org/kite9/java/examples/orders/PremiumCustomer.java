package org.kite9.java.examples.orders;

import java.util.Set;

import org.kite9.diagram.annotation.K9OnDiagram;

@K9OnDiagram(on=Order.class)
public class PremiumCustomer extends Customer {

	int premiumCustomerNumber;
	
	public Set<ExpeditedOrder> openExpeditedOrders;
}
