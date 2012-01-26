package org.kite9.java.examples.library.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.kite9.java.examples.library.usecases.UseCase;



/**
 * This annotation is added to implementation classes to show which use cases they implement.
 * @author robmoffat
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Implements {

	public Class<? extends UseCase>[] value(); 
}
