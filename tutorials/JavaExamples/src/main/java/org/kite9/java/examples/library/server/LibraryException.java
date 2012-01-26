package org.kite9.java.examples.library.server;

public class LibraryException extends Exception {

	private static final long serialVersionUID = 4420306290199165731L;

	private LibraryException() {
		super();
	}

	private LibraryException(String message, Throwable cause) {
		super(message, cause);
	}

	private LibraryException(String message) {
		super(message);
	}

	private LibraryException(Throwable cause) {
		super(cause);
	}

}
