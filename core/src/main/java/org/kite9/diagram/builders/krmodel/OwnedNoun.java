package org.kite9.diagram.builders.krmodel;

/**
 * e.g. John's tractor, java.util.Map's get() method.
 */
public interface OwnedNoun extends NounPart {

    public abstract SimpleNoun getOwned();

    public abstract SimpleNoun getOwner();

}