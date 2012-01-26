package org.kite9.diagram.builders.wizards.er;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.kite9.diagram.builders.ClassBuilder;
import org.kite9.diagram.builders.DiagramBuilder;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.Relationship;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.diagram.builders.noun.NounPart;

/**
 * Helper wizard for creating links for an entity relationship diagram.
 * 
 * @author robmoffat
 * 
 */
public class EntityRelationshipWizard {

	DiagramBuilder db;
	Format attributeFormat;
	Format relationshipFormat;
	Format classFormat;
	Format implementsFormat;
	Format extendsFormat;
	boolean interfaceTraversal;
	
	public Format getImplementsFormat() {
		return implementsFormat;
	}


	public void setImplementsFormat(Format implementsFormat) {
		this.implementsFormat = implementsFormat;
	}


	public Format getExtendsFormat() {
		return extendsFormat;
	}


	public void setExtendsFormat(Format extendsFormat) {
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

	public Format getAttributeFormat() {
		return attributeFormat;
	}


	public void setAttributeFormat(Format attributeFormat) {
		this.attributeFormat = attributeFormat;
	}


	public Format getRelationshipFormat() {
		return relationshipFormat;
	}


	public void setRelationshipFormat(Format relationshipFormat) {
		this.relationshipFormat = relationshipFormat;
	}


	public Format getClassFormat() {
		return classFormat;
	}


	public void setClassFormat(Format classFormat) {
		this.classFormat = classFormat;
	}
	
	public EntityRelationshipWizard(DiagramBuilder db) {
		this.db = db;
		this.attributeFormat = db.asTextLines();
		this.relationshipFormat = db.asGlyphs();
		this.classFormat = relationshipFormat;
		this.implementsFormat = relationshipFormat;
		this.extendsFormat = relationshipFormat;
	}
	
	/**
	 * Shows classes as glyphs, shows relationships between classes, fields as text lines and inheritance.  
	 */
	public void show(ClassBuilder classBuilder, final Filter<Field> fieldFilter) {
		showGlyphs(classBuilder);
		showRelationships(classBuilder, fieldFilter);
		showFieldAttributes(classBuilder, fieldFilter);
		showInheritance(classBuilder, interfaceTraversal);
		showExtends(classBuilder);
	}
	
	
	public void showGlyphs(ClassBuilder classBuilder) {
		classBuilder.show(classFormat);
	}

	public void showRelationships(ClassBuilder classBuilder, final Filter<Field> fieldFilter) {

		classBuilder.withFields(new Filter<Field>() {

			public boolean accept(Field o) {
				if ((fieldFilter==null) || (fieldFilter.accept(o))) {
					NounPart np = db.createNoun(o.getGenericType());

					return (db.isOnDiagram(np.getRepresented()));
				} else {
					return false;
				}
			}

		}, false).withType(null).show(db.asGlyphs());
	}

	public void showFieldAttributes(ClassBuilder classBuilder, final Filter<Field> fieldFilter) {
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
		classBuilder.show(new Format() {

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
