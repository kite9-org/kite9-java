package org.kite9.java.examples.library;

import java.util.Date;

import org.kite9.framework.common.FSMHelp;
import org.kite9.java.examples.library.actors.Member;

public class Book {

	private BookState state = BookState.ON_SHELF;
	
	private Member borrower;
	
	private Date returnDate;
	
	private String title;
	
	private int id;
	
	public Book() {
		super();
	}

	public BookState getState() {
		return state;
	}

	public void setState(BookState state) {
		this.state = state;
	}

	public Member getBorrower() {
		return borrower;
	}

	public void setBorrower(Member borrower) {
		this.borrower = borrower;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	/**
	 * Called when the book is returned by a member
	 */
	@BeforeState({BookState.BORROWED, BookState.OVERDUE})
	@AfterState(BookState.RETURNED)
	public void returnBook() {
		checkBeforeState();
		state = BookState.RETURNED;
		borrower = null;
		checkAfterState();
		
	}
	
	/**
	 * Called when a member borrows a book
	 */
	@BeforeState({BookState.ON_SHELF})
	@AfterState(BookState.BORROWED)
	public void borrowBook(Member m) {
		checkBeforeState();
		state = BookState.BORROWED;
		borrower = m;
		checkAfterState();
	}
	
	/**
	 * Called when a returned book is put back on the shelf
	 */
	@BeforeState({BookState.RETURNED})
	@AfterState(BookState.ON_SHELF)
	public void processBook() {
		checkBeforeState();
		state = BookState.ON_SHELF;
		checkAfterState();
	}
	
	/**
	 * Called to move the book into overdue state
	 */
	@BeforeState({BookState.BORROWED})
	@AfterState({BookState.OVERDUE, BookState.BORROWED})
	public void checkReturnDate(Date today) {
		checkBeforeState();
		if (today.before(returnDate)) {
			state = BookState.OVERDUE;
		}
		checkAfterState();
	}

	private void checkBeforeState() throws LibraryException {
		FSMHelp.stateOk(BeforeState.class, state);
	}
	
	private void checkAfterState() throws LibraryException {
		FSMHelp.stateOk(AfterState.class, state);
	}
	
	public void testBook() {
		Book b = new Book();
		b.borrowBook(new Member() {
			
			public int getMembershipNumber() {
				return 0;
			}

			public int getBorrowedBookCount() {
				return 0;
			}

			public int getBorrowedBookLimit() {
				return 0;
			}

			public void setBorrowedBookCount(int x) {
				
			}

			public String getName() {
				return null;
			}
		});
		
		b.returnBook();
		b.processBook();
	}
	
}
