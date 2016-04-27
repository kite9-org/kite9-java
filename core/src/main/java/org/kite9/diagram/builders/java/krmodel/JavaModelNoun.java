package org.kite9.diagram.builders.java.krmodel;

import java.util.List;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.noun.SimpleNoun;

public class JavaModelNoun implements SimpleNoun {
	
	public static final String JAVA_MODEL_PART = "kite9-java";
	
	private final String label;

	public JavaModelNoun(String label) {
		this.label = label;
	}

	public Address getID() {
		return new AddressImpl(JAVA_MODEL_PART, label);
	}

	public Address extend(Address in) {
		return in.merge(getID());
	}

	public String getLabel() {
		return label;
	}

	public List<NounPart> getDisambiguation() {
		return null;
	}

}
