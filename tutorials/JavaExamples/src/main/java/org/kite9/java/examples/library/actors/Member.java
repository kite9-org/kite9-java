package org.kite9.java.examples.library.actors;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.java.examples.library.usecases.BorrowBooks;
import org.kite9.java.examples.library.usecases.PayFine;
import org.kite9.java.examples.library.usecases.ReturnBooks;
import org.kite9.java.examples.library.usecases.Uses;



@Uses({BorrowBooks.class, PayFine.class, ReturnBooks.class})
@K9OnDiagram
public interface Member extends Person {

	public int getMembershipNumber();
	
	public int getBorrowedBookCount();
	
	public int getBorrowedBookLimit();
	
	public void setBorrowedBookCount(int x);
}
