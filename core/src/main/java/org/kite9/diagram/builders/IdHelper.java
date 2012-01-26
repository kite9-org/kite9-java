package org.kite9.diagram.builders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import org.kite9.diagram.builders.noun.NounPart;
import org.kite9.framework.common.Kite9ProcessingException;
import org.kite9.framework.model.AbstractHandle;
import org.kite9.framework.model.ProjectModel;

/**
 * Ensures that IDs for elements are unique within the diagram.
 * 
 * @author robmoffat
 * 
 */
public class IdHelper {
	
	ProjectModel pm;

	public IdHelper(ProjectModel pm) {
		super();
		this.pm = pm;
	}

	Set<String> used = new HashSet<String>();

	public synchronized String getId(Object o) {
		if (o instanceof Class<?>) {
			return getProjectClassId((Class<?>) o);
		} else if (o instanceof String) {
			return getDiagramEntityId((String) o);
		} else if (o instanceof Method) {
			return getProjectMethodId((Method) o);
		} else if (o instanceof Constructor<?>) {
			return getProjectConstructorId((Constructor<?>) o);
		} else if (o instanceof Package) {
			return getProjectPackageId((Package) o);
		} else if (o instanceof Field) {
			return getProjectFieldId((Field) o);
		} else if (o instanceof Annotation) {
			return getProjectClassId(((Annotation) o).annotationType());
		} else if (o instanceof Enum<?>) {
				return getProjectEnumId((Enum<?>) o);
		} else if (o instanceof NounRelationshipBinding) {
			return getSubjectBindingId((NounRelationshipBinding) o);
		} else if (o instanceof Relationship) {
		    	return getRelationshipId((Relationship) o);
		} else if (o instanceof Type) {
		    	return getDiagramEntityId(o.toString());
		} else if (o instanceof NounPart){
		    	return getId(((NounPart)o).getRepresented());
		} else if (o instanceof Object) {
				return getDiagramEntityId(o.toString());
		} else {
			throw new Kite9ProcessingException("Could not get id for " + o.getClass().toString());
		}
	}

	private String getProjectEnumId(Enum<?> o) {
		return getProjectClassId(o.getDeclaringClass()) + "/" + o.name();
	}

	private String getSubjectBindingId(NounRelationshipBinding o) {
		return getId(o.getSubject())+"/"+getId(o.getRelationship());
	}

	protected String getRelationshipId(Relationship o) {
	    return "rel_"+o.getName();
	}
	

	protected String getProjectMethodId(Method o) {
		return getProjectClassId(o.getDeclaringClass()) + "/" + o.getName()+"()";
	}
	
	protected String getProjectConstructorId(Constructor<?> o) {
		return getProjectClassId(o.getDeclaringClass()) + "/" + o.getName()+"()";
	}
	

	protected String getProjectFieldId(Field o) {
		return getProjectClassId(o.getDeclaringClass()) + "/" + o.getName();
	}

	protected String getDiagramEntityId(String o) {
		int index = 0;
		while (used.contains(o + index)) {
			index++;
		}

		used.add(o + index);
		return o + index;
	}

	protected String getProjectClassId(Class<?> o) {
		if (pm.withinModel(AbstractHandle.convertClassName(o))) {
			return "project_java_class:" + o.getCanonicalName();
		} else {
			return "java_class:"+o.getCanonicalName();
		}
 	}
	
	protected String getProjectPackageId(Package p) {
		if (pm.packageWithinModel(AbstractHandle.convertPackageName(p))) {
			return "project_java_package:" + p.getName();			
		} else {
			return "java_package:"+p.getName();
		}
	}

}
