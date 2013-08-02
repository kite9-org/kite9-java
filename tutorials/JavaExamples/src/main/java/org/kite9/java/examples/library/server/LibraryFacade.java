package org.kite9.java.examples.library.server;

import java.util.Calendar;

import org.kite9.java.examples.library.Book;
import org.kite9.java.examples.library.BookState;
import org.kite9.java.examples.library.LibraryException;
import org.kite9.java.examples.library.actors.Member;
import org.kite9.java.examples.library.usecases.Authenticate;
import org.kite9.java.examples.library.usecases.BorrowBooks;

/**
 * This method provides the logic of running the library and managing all the transactions with the
 * database.
 * 
 * @author robmoffat
 *
 */
public class LibraryFacade {

	LibraryDB db;
	
	@Implements(Authenticate.class)
	public Member authenticate(int membershipId) {
		//return db.retrieveMember(membershipId);
		return null;
	}
	
	@Implements(BorrowBooks.class)
	public void borrow(int membershipId, int bookId) {
		//Member m = authenticate(membershipId);
		Member m = null;
		if (m==null) {
			throw new LibraryException("Could not find member: "+membershipId);
		}
		
		int borrowedBookCount = m.getBorrowedBookCount();
		if (borrowedBookCount < m.getBorrowedBookLimit()) {
			Book b = db.retrieveBook(bookId);
			BookState bs = b.getState();
			if ((bs==BookState.ON_SHELF) || (bs==BookState.RETURNED)) {
				b.setState(BookState.BORROWED);
				b.setBorrower(m);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, 1);
				b.setReturnDate(cal.getTime());
				db.saveBook(b);
				m.setBorrowedBookCount(borrowedBookCount+1);
				db.saveMember(m);
			} else {
				throw new LibraryException("Book is already borrowed "+b.getState());
			}
		} else {
			throw new LibraryException("Book limit exceeded for "+m.getName());			
		}
	}
	
}
