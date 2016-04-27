package org.kite9.diagram.builders.representation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class XMLRepresentation implements Representation<String> {
	
	@XStreamImplicit
	private List<SimpleRelationship> relationships = new ArrayList<SimpleRelationship>();
	
	public List<SimpleRelationship> getRelationships() {
		return relationships;
	}

	public Set<NounPart> getNouns() {
		return nouns;
	}
	
	@XStreamOmitField
	private Set<NounPart> nouns = new HashSet<>();
	
	@Override
	public boolean contains(NounPart o) {
		return nouns.contains(o);
	}
	@Override
	public String render() {
		XStream xstream = new XStream();
		xstream.processAnnotations(this.getClass());
		return xstream.toXML(this);
	}

}
