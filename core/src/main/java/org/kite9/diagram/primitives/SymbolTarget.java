package org.kite9.diagram.primitives;

import org.kite9.diagram.adl.ContainerProperty;
import org.kite9.diagram.adl.Symbol;

/**
 * Indicates that the DiagramElement can accept symbols.
 * 
 * @author robmoffat
 *
 */
public interface SymbolTarget {

	public ContainerProperty<Symbol> getSymbols();
	
	public void setSymbols(ContainerProperty<Symbol> l);
}
