package org.kite9.java.examples.library;

public class LibraryException extends RuntimeException {

	private static final long serialVersionUID = 7683048545539635959L;

	public LibraryException() {
		super();
	}

	public LibraryException(String message, Throwable cause) {
		super(message, cause);
	}

	public LibraryException(String message) {
		super(message);
	}

	public LibraryException(Throwable cause) {
		super(cause);
	}

}
