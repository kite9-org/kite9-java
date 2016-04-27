package org.kite9.diagram.builders.java.krmodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;
import org.kite9.diagram.builders.id.HasAddress;
import org.kite9.diagram.builders.id.IdHelper;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * Ensures that IDs for elements are unique within the diagram.  This provides a bunch
 * of methods for generating IDs from java objects.
 * 
 * @author robmoffat
 * 
 */
public class JavaIdHelper implements IdHelper {
		
	Set<String> used = new HashSet<String>();
	
	public String getId(Object o) {
		Address compound = getCompoundId(o);
		return compound.toString();
	}

	public synchronized Address getCompoundId(Object o) {
		if (o instanceof HasAddress) {
			return ((HasAddress)o).getID();
		} else if (o instanceof Class<?>) {
			return getProjectClassId((Class<?>) o);
		} else if (o instanceof String) {
			return getDiagramEntityId((String) o, true);
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
		} else if (o instanceof Type) {
		    return getDiagramEntityId(o.toString(), true);
		} else if (o instanceof Object) {
			return getProjectClassId(o.getClass());
		} else {
			throw new Kite9ProcessingException("Could not get id for " + o.getClass().toString());
		}
	}

	private Address getProjectEnumId(Enum<?> o) {
		Address cid = getProjectClassId(o.getDeclaringClass());
		return cid.extend("enum", o.name());
	}

	protected Address getProjectMethodId(Method o) {
		return getProjectClassId(o.getDeclaringClass()).extend("method",o.getName());
	}
	
	protected Address getProjectConstructorId(Constructor<?> o) {
		return getProjectClassId(o.getDeclaringClass()).extend("constructor",o.getName());
	}
	

	protected Address getProjectFieldId(Field o) {
		return getProjectClassId(o.getDeclaringClass()).extend("field",o.getName());
	}

	protected Address getDiagramEntityId(String o, boolean addDocumentId) {
		int index = 0;
		while (used.contains(o + index)) {
			index++;
		}

		used.add(o + index);
		
		if (addDocumentId) {
			return new AddressImpl("entity", o + index, true);
		} else {	
			return new AddressImpl().extend("entity", o+index);
		}
		
	}

	protected Address getProjectClassId(Class<?> o) {
		return new AddressImpl("class", getClassName(o)).extend("package", getPackageName(o));
 	}

	protected String getPackageName(Class<?> o) {
		Package p = o.getPackage();
		if (p == null) {
			return null; 
		} else {
			return p.getName();
		}
	}

	protected String getClassName(Class<?> o) {
		String name = o.getName();
		if (name.contains(".")) {
			name = name.substring(name.lastIndexOf(".")+1);
		}
		return name;
	}
	
	protected Address getProjectPackageId(Package p) {
		return new AddressImpl("package", p.getName());
	}

}
