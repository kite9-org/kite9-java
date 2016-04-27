package org.kite9.diagram.builders.id;

import java.util.Map.Entry;

/**
 * When an element has an Address (aka compound ID), this is an indication that it refers to a 
 * particular real-world concept that is consistent across many diagrams.  Addresses have 
 * several parts, which tend to be independent, and hone down to a single element.
 * 
 * <ul>
 * <li>One ID should refer to the one thing, even in multiple places.  You should never see an ID being used to refer to two different things.
 * <li>Ideally, IDs should be persistent across instances of diagrams (whether revisions, or different diagrams).  So, if we have persistent components, use them.
 * <li>As far as possible, we should minimize the number of different IDs referring to the same thing.  But you know, sometimes we’re going to end up with different IDs representing the same object.
 * <li>Since components can change, we expect IDs to also change.  For this reason, we use components, in the hope that some elements of the elements ID will persist across versions of the object.
 * </ul>
 * 
 * e.g. A Method could have package, class and name.
 * 
 * @author robmoffat
 *
 */
public interface Address {
	
	/**
	 * This is a special value which is interpreted by the post controller to say 
	 * "add in whatever document ID you decide on here".
	 */
	public static final String REPLACE_DOCUMENT_ID = "**doc**";
	public static final String DOCUMENT_ID = "doc";
	
	public Address merge(Address otherParts);
	
	public Address extend(String key, String value);

	public Iterable<Entry<String, String>> entrySet();
	
	public String get(Object component);
}
