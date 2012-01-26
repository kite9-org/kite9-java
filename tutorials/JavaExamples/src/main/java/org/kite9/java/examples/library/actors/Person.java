package org.kite9.java.examples.library.actors;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.java.examples.library.usecases.PerformSearch;
import org.kite9.java.examples.library.usecases.Uses;

@Uses(PerformSearch.class)
@K9OnDiagram
public interface Person extends Actor {

	public String getName();
	
}
