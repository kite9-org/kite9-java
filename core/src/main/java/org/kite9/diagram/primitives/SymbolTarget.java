package org.kite9.diagram.primitives;

import java.util.List;

import org.kite9.diagram.adl.Symbol;

/**
 * Indicates that the DiagramElement can accept symbols.
 * 
 * @author robmoffat
 *
 */
public interface SymbolTarget {

	public List<Symbol> getSymbols();
	
	public void setSymbols(List<Symbol> l);
}
