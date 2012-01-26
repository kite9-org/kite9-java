package org.kite9.java.examples.library.server;

import org.kite9.java.examples.library.Book;
import org.kite9.java.examples.library.actors.Member;

/**
 * Handles the basic storage and retrieval logic for the library database.
 * 
 * @author robmoffat
 *
 */
public interface LibraryDB {

	public Book retrieveBook(int bookId);
	
	public Member retrieveMember(int memberId);
	
	public void saveBook(Book book);
	
	public void saveMember(Member book);
}
