package org.kite9.java.examples.library.usecases;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 * This annotation is added to actors to show which use cases they employ.
 * @author robmoffat
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Uses {

	public Class<? extends UseCase>[] value(); 
}
