package org.kite9.diagram.adl;

import org.kite9.diagram.xml.ContainerProperty;
import org.kite9.diagram.xml.Symbol;

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
