package org.kite9.diagram.builders.krmodel.noun;

/**
 * e.g. John's tractor, java.util.Map's get() method.
 * 
 * This is used where we have multiple objects on the diagram, potentially with the same ID, 
 * but we want to reference a specific one by it's composition hierarchy.
 * 
 */
public interface OwnedNoun extends NounPart {

    public abstract SimpleNoun getOwned();

    public abstract SimpleNoun getOwner();

}