package org.kite9.diagram.builders.id;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * An AddressImpl is a string in the form {c[thing1]d[thing2]j[thing3]}.  This class can convert IDs to this common
 * representation.  It understands all the allowed prefixes, and whether they are internal or external 
 * (only one external prefix is required and allowed).  
 * 
 * Addresses are project-unique, but many different IDs could potentially refer to the same underlying
 * diagram element. 
 * 
 * Elements are name/value like name[value], though each name is a single character.
 * 
 * Elements are ordered alphabetically.
 * 
 * @author robmoffat
 */
public class AddressImpl extends TreeMap<String, String> implements Address {
	
	public AddressImpl(boolean addEmptyDocumentId) {
		if (addEmptyDocumentId) {
			put("document", Address.REPLACE_DOCUMENT_ID);
		}
	}
	
	public AddressImpl(String key, String value) {
		this(key, value, false);
	}
	
	public AddressImpl(String key, String value, boolean addEmptyDocumentId) {
		this(addEmptyDocumentId);
		put(checkKey(key), checkValue(value));
	}
	
	private String checkValue(String value) {
		throwOnIllegalCharacter(value,"]");
		throwOnIllegalCharacter(value,"[");
		throwOnIllegalCharacter(value,"{");
		throwOnIllegalCharacter(value,"}");
		return value;
	}

	protected void throwOnIllegalCharacter(String value, String c) {
		if (value.contains(c)) {
			throw new IllegalStateException("Compound ID parts cannot contain '"+c+"' - "+value);
		}
	}

	private String checkKey(String key) {
		if (key == null) {
			throw new IllegalStateException("Key cannot be null");
		}
		
		key = key.trim();
		
		if (key.length() == 0) {
			throw new IllegalStateException("Key length = 0");
		}
		
		checkValue(key);
		
		return key;
	}

	public AddressImpl(Address toCopy) {
		for (Entry<String, String> ent : toCopy.entrySet()) {
			put(checkKey(ent.getKey()), checkValue(ent.getValue()));
		}
	}

	public AddressImpl(String id) {
		if ((id.startsWith("{")) && (id.endsWith("}"))) {
			// already in Kite9id format.
			id = id.substring(1, id.length() - 2);
			String[] parts = id.split("\\]");

			for (String part : parts) {
				String[] split = part.split("\\[");
				String key = split[0].trim();
				put(checkKey(key), checkValue(split[1]));
			}

		} else {
			put("z", checkValue(id.replaceAll("[\\[\\]\\{\\}]", "")));
		}

	}

	public AddressImpl() {
	}

	public String toString() {

		StringBuilder sb = new StringBuilder(200);
		sb.append("{");
		for (Entry<String, String> element : entrySet()) {
			sb.append(element.getKey());
			sb.append("[");
			sb.append(element.getValue());
			sb.append("]");
		}
		sb.append("}");

		return sb.toString();
	}
	
	public AddressImpl copy() {
		return new AddressImpl(this);
	}
	
	public AddressImpl extend(String key, String value) {
		if (value != null) {
			AddressImpl out = new AddressImpl(this);
			out.put(checkKey(key), checkValue(value));
			return out;
		} else {
			return this;
		}
	}

	public Address merge(Address otherParts) {
		AddressImpl out = new AddressImpl(this);
		for (Entry<String, String> ent : otherParts.entrySet()) {
			out.put(checkKey(ent.getKey()), checkValue(ent.getValue()));
		}
		
		return out;
	}

}