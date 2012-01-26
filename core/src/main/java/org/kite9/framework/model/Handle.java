package org.kite9.framework.model;


/**
 * Lightweight interface to some java reflection construct.
 *
 */
public interface Handle<X> {

    /**
     * Returns the reflection-object that this is a handle for
     * @return
     */
    public X hydrate(ClassLoader cl);
}
