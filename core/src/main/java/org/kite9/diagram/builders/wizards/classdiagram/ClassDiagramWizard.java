package org.kite9.diagram.builders.wizards.classdiagram;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.java.ClassBuilder;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.krmodel.NounPart;
import org.kite9.diagram.builders.krmodel.Relationship;

/**
 * Helper wizard for creating links for an entity relationship diagram.
 * 
 * @author robmoffat
 * 
 */
public class ClassDiagramWizard {

	DiagramBuilder db;
	PropositionFormat attributeFormat;
	PropositionFormat relationshipFormat;
	PropositionFormat classFormat;
	PropositionFormat implementsFormat;
	PropositionFormat extendsFormat;
	boolean interfaceTraversal;
	
	public PropositionFormat getImplementsFormat() {
		return implementsFormat;
	}


	public void setImplementsFormat(PropositionFormat implementsFormat) {
		this.implementsFormat = implementsFormat;
	}


	public PropositionFormat getExtendsFormat() {
		return extendsFormat;
	}


	public void setExtendsFormat(PropositionFormat extendsFormat) {
		this.extendsFormat = extendsFormat;
	}


	public boolean isInterfaceTraversal() {
		return interfaceTraversal;
	}


	public void setInterfaceTraversal(boolean interfaceTraversal) {
		this.interfaceTraversal = interfaceTraversal;
	}


	public boolean isFieldTraversal() {
		return fieldTraversal;
	}


	public void setFieldTraversal(boolean fieldTraversal) {
		this.fieldTraversal = fieldTraversal;
	}


	boolean fieldTraversal;

	public PropositionFormat getAttributeFormat() {
		return attributeFormat;
	}


	public void setAttributeFormat(PropositionFormat attributeFormat) {
		this.attributeFormat = attributeFormat;
	}


	public PropositionFormat getRelationshipFormat() {
		return relationshipFormat;
	}


	public void setRelationshipFormat(PropositionFormat relationshipFormat) {
		this.relationshipFormat = relationshipFormat;
	}


	public PropositionFormat getClassFormat() {
		return classFormat;
	}


	public void setClassFormat(PropositionFormat classFormat) {
		this.classFormat = classFormat;
	}
	
	public ClassDiagramWizard(DiagramBuilder db) {
		this.db = db;
		this.attributeFormat = db.asTextLines();
		this.relationshipFormat = db.asConnectedGlyphs();
		this.classFormat = relationshipFormat;
		this.implementsFormat = relationshipFormat;
		this.extendsFormat = relationshipFormat;
	}
	
	/**
	 * A conventience method: Shows classes as glyphs, shows relationships between classes, fields, methods as text lines and inheritance.  
	 */
	public void show(ClassBuilder classBuilder) {
		showGlyphs(classBuilder);
		showFieldRelationships(classBuilder, null);
		showInheritance(classBuilder, interfaceTraversal);
		showExtends(classBuilder);
		showFieldAttributes(classBuilder, db.onlyAnnotated());
		showMethodAttributes(classBuilder, db.onlyAnnotated());
	}
	
	public void showGlyphs(ClassBuilder classBuilder) {
		classBuilder.show(classFormat);
	}

	public void showFieldRelationships(ClassBuilder classBuilder, final Filter<Field> fieldFilter) {

		classBuilder.withFields(new Filter<Field>() {

			public boolean accept(Field o) {
				if ((fieldFilter==null) || (fieldFilter.accept(o))) {
					NounPart np = db.createNoun(o.getGenericType());

					return (db.isOnDiagram(np.getRepresented()));
				} else {
					return false;
				}
			}

		}, false).withType(null).show(relationshipFormat);
	}

	public void showFieldAttributes(ClassBuilder classBuilder, final Filter<? super Field> fieldFilter) {
		// show any other fields too
		classBuilder.withFields(new Filter<Field>() {

			public boolean accept(Field o) {
				if ((fieldFilter==null) || (fieldFilter.accept(o))) {
					NounPart np = db.createNoun(o.getGenericType());
					return (!db.isOnDiagram(np.getRepresented()));
				} else {
					return false;
				}
			}

		}, false).show(attributeFormat);
	}
	
	public void showMethodAttributes(ClassBuilder classBuilder, final Filter<? super Method> methodFilter) {
		// show any other fields too
		classBuilder.withMethods(new Filter<Method>() {

			public boolean accept(Method o) {
				if ((methodFilter==null) || (methodFilter.accept(o))) {
					NounPart np = db.createNoun(o.getGenericReturnType());
					return (!db.isOnDiagram(np.getRepresented()));
				} else {
					return false;
				}
			}

		}, false).show(attributeFormat);
	}
	
	public void showInheritance(final ClassBuilder classBuilder, boolean traverse) {
		final Set<Class<?>> allClasses = createClassSet(classBuilder);
		
		classBuilder.withInterfaces(new Filter<Class<?>>() {

			public boolean accept(Class<?> o) {
				return allClasses.contains(o);
			}
			
		}, traverse).show(implementsFormat);
	}
	
	public void showExtends(final ClassBuilder classBuilder) {
		final Set<Class<?>> allClasses = createClassSet(classBuilder);
		
		classBuilder.withSuperClasses(new Filter<Class<?>>() {

			public boolean accept(Class<?> o) {
				return allClasses.contains(o);
			}
			
		}).show(implementsFormat);
	}


	private Set<Class<?>> createClassSet(final ClassBuilder classBuilder) {
		final Set<Class<?>> allClasses = new HashSet<Class<?>>();
		classBuilder.show(new PropositionFormat() {

			public void write(NounPart subject, Relationship verb, NounPart object) {
				Object r = object.getRepresented();
				if (r instanceof Class<?>) {
					allClasses.add((Class<?>) r);
				}
			}
			
		});
		return allClasses;
	}
}
