package org.kite9.java.examples.library;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks up {@link Book} enumeration with the allowed transitions to {@link BookState} 
 *  
 * @author robmoffat
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BeforeState {

	public BookState[] value();
}
