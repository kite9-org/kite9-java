package org.kite9.java.examples.library.actors;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.java.examples.library.usecases.ReserveBook;
import org.kite9.java.examples.library.usecases.Uses;


@Uses(ReserveBook.class)
@K9OnDiagram
public interface Librarian extends Person {

}
