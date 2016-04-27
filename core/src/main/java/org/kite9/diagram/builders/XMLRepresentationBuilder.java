package org.kite9.diagram.builders;

import java.lang.reflect.Method;

import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.id.IdHelper;
import org.kite9.diagram.builders.java.AbstractJavaRepresentationBuilder;
import org.kite9.diagram.builders.java.krmodel.BasicJavaNounFactory;
import org.kite9.diagram.builders.java.krmodel.JavaIdHelper;
import org.kite9.diagram.builders.krmodel.Knowledge;
import org.kite9.diagram.builders.krmodel.noun.NounFactory;
import org.kite9.diagram.builders.krmodel.noun.NounPart;
import org.kite9.diagram.builders.krmodel.proposition.ComplexProposition;
import org.kite9.diagram.builders.krmodel.proposition.SimpleRelationship;
import org.kite9.diagram.builders.representation.Representation;
import org.kite9.diagram.builders.representation.XMLRepresentation;
import org.kite9.framework.alias.Aliaser;
import org.kite9.framework.alias.PropertyAliaser;
import org.kite9.framework.model.ProjectModel;

public class XMLRepresentationBuilder extends AbstractJavaRepresentationBuilder<String>{
	
	public XMLRepresentationBuilder(NounPart theDiagram, ProjectModel pm, NounFactory nf) {
		super(pm, theDiagram, nf);
	}

	public XMLRepresentationBuilder(Method creator, ProjectModel pm, NounFactory nf) {
		this(nf.createNoun(creator), pm, nf);
	}
	
	public XMLRepresentationBuilder(Method creator, ProjectModel pm, Aliaser a, IdHelper helper) {
		this(creator, pm, new BasicJavaNounFactory(a, helper));
	}

	public XMLRepresentationBuilder(NounPart theDiagram, ProjectModel pm, Aliaser a, IdHelper helper) {
		this(theDiagram, pm, new BasicJavaNounFactory(a, helper));
	}
	
	public XMLRepresentationBuilder(Method creator, ProjectModel pm) {
		this(creator, pm, new PropertyAliaser(), new JavaIdHelper());
	}
	
	public XMLRepresentationBuilder(NounPart theDiagram, ProjectModel pm) {
		this(theDiagram, pm, new PropertyAliaser(), new JavaIdHelper());
	}
	
	public String getXML() {
		return representation.render();
	}
	
	public XMLRepresentation getRepresentation() {
		return (XMLRepresentation) super.getRepresentation();
	}

	@Override
	protected Representation<String> createRepresentation(NounPart id) {
		return new XMLRepresentation();
	}
	
	public Format asXML() {
		return new Format() {

			@Override
			public void write(SimpleRelationship p) {
				getRepresentation().getRelationships().add(p);
				addNouns(p);
			}
			
			private void addNouns(Knowledge k) {
				if (k instanceof NounPart) {
					getRepresentation().getNouns().add((NounPart) k);
				} else if (k instanceof SimpleRelationship) {
					addNouns(((SimpleRelationship) k).getSubject());
					addNouns(((SimpleRelationship) k).getObject());
				} else if (k instanceof ComplexProposition) {
					for (SimpleRelationship sr : ((ComplexProposition) k).decompose()) {
						addNouns(sr);
					}
				}
			}
		};
	}

}
