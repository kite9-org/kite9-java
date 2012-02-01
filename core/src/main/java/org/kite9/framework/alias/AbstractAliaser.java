package org.kite9.framework.alias;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.common.Kite9ProcessingException;

/**
 * Handles the {@link K9Alias} tag and deferring to the getDefinedAlias tag for
 * user-defined aliases.
 * 
 * Default alias functionality is to strip everything before and including the
 * last period, except in the case of package names.
 * 
 * @author moffatr
 * 
 */
public abstract class AbstractAliaser implements Aliaser {

	protected String getAlias(Class<?> c) {
		StringBuffer out = new StringBuffer();

		while (c.isArray()) {
			out.append("array of ");
			c = c.getComponentType();
		}

		String aa = getAliasFromAnnotation(c);
		if (aa != null) {
			out.append(aa);
		} else if (c.isMemberClass()) {
			out.append(getAlias(c.getSimpleName()));
		} else {
			out.append(getAlias(c.getName()));
		}
		// handleTypeParameters(c.getTypeParameters(), out);
		return out.toString();
	}

	protected String getAlias(Annotation a) {
		Class<?> cl = a.annotationType();
		String aa = getAliasFromAnnotation(cl);
		if (aa != null)
			return aa;

		return getAlias(cl);
	}
	
	protected String getInstanceAlias(Object o) {
		Class<?> cl = o.getClass();
		String aa = getAliasFromAnnotation(cl);
		if (aa != null)
			return aa;

		return getAlias(cl);
	}

	protected String getAlias(Method m) {
		String aa = getAliasFromAnnotation(m);
		if (aa != null)
			return aa;

		return getAlias(m.getDeclaringClass().getName() + "." + m.getName());
	}
	
	protected String getAlias(Constructor<?> m) {
		String aa = getAliasFromAnnotation(m);
		if (aa != null)
			return aa;

		return getAlias(m.getDeclaringClass().getName() + "." + m.getName());
	}


	protected String getAlias(Field f) {
		String aa = getAliasFromAnnotation(f);
		if (aa != null)
			return aa;

		return getAlias(f.getDeclaringClass().getName() + "." + f.getName());
	}

	protected String getAlias(Type t) {
		if (t instanceof Class<?>) {
			return getAlias((Class<?>) t);
		} else if (t instanceof ParameterizedType) {
			StringBuilder sb = new StringBuilder();
			sb.append(getAlias((Class<?>) ((ParameterizedType) t).getRawType()));
			handleTypeParameters(((ParameterizedType) t).getActualTypeArguments(), sb);

			return sb.toString();
		} else if (t instanceof GenericArrayType) {
			return "array of " + getAlias(((GenericArrayType) t).getGenericComponentType());
		} else if (t instanceof TypeVariable<?>) {
			return t.toString();
		} else {
			throw new Kite9ProcessingException("Not handled yet");
		}
	}

	private void handleTypeParameters(Type[] args, StringBuilder sb) {
		sb.append("<");
		for (int i = 0; i < args.length; i++) {
			sb.append(getAlias(args[i]));
			if (i < args.length - 1) {
				sb.append(",");
			}
		}
		sb.append(">");
	}

	protected String getAlias(Package p) {
		String aa = getAliasFromAnnotation(p);
		if (aa != null)
			return aa;

		return p.getName();
	}

	protected String getAliasFromAnnotation(AnnotatedElement p) {
		K9OnDiagram ann = p.getAnnotation(K9OnDiagram.class);
		if (ann != null) {
			String aa = ann.alias();
			if (aa.length() > 0)
				return aa;
		}

		return null;
	}

	/**
	 * Strips off the package name or class name from the alias
	 */
	protected String getDefaultAlias(String fullName) {
		int prefix = fullName.lastIndexOf(".");
		return fullName.substring(prefix + 1);
	}

	protected abstract String getDefinedAlias(String fullName);

	public String getAlias(String fullName) {
		String al = getDefinedAlias(fullName);
		if (al != null) {
			return al;
		}

		return getDefaultAlias(fullName);
	}

	public String getObjectAlias(Object o) {
		while (o instanceof AliasEnabled) {
			o = ((AliasEnabled) o).getObjectForAlias();
		}

		if (o instanceof Field) {
			return getAlias((Field) o);
		} else if (o instanceof Method) {
			return getAlias((Method) o);
		} else if (o instanceof Constructor<?>) {
			return getAlias((Constructor<?>) o);
		} else if (o instanceof Package) {
			return getAlias((Package) o);
		} else if (o instanceof Annotation) {
			return getAlias((Annotation) o);
		} else if (o instanceof String) {
			return getAlias((String) o);
		} else if (o instanceof Enum<?>) {
			return getAlias((Enum<?>) o);
		} else if (o instanceof Class<?>) {
			return getAlias((Class<?>) o);
		} else if (o instanceof Type) {
			return getAlias((Type) o);
		} else {
			return getInstanceAlias(o);
		}
	}

	protected String getAlias(Enum<?> o) {
		return o.toString().toLowerCase();
	}

	public String getObjectStereotype(Object o) {
		while (o instanceof AliasEnabled) {
			o = ((AliasEnabled) o).getObjectForAlias();
		}

		if (o instanceof Field) {
			return getStereotype((Field) o);
		} else if (o instanceof Method) {
			return getStereotype((Method) o);
		} else if (o instanceof Constructor<?>) {
			return getStereotype((Constructor<?>) o);
		} else if (o instanceof Package) {
			return getStereotype((Package) o);
		} else if (o instanceof String) {
			return getStereotype((String) o);
		} else if (o instanceof Class<?>) {
			return getStereotype((Class<?>) o);
		} else if (o instanceof Annotation) {
			return getStereotype((Annotation) o);
		} else if (o instanceof Enum<?>) {
			return getStereotype((Enum<?>) o);
		} else if (o instanceof Type) {
			return getStereotype((Type) o);
		} else {
			return getInstanceStereotype(o);
		}
	}

	protected String getStereotype(Field o) {
		String as = getStereotypeFromAnnotation(o);
		if (as != null)
			return as;

		return getAlias("field");
	}

	protected String getStereotype(Type t) {
		if (t instanceof Class<?>) {
			return getStereotype((Class<?>) t);
		} else if (t instanceof ParameterizedType) {
			return getStereotype((Class<?>) ((ParameterizedType) t).getRawType());
		} else {
			throw new Kite9ProcessingException("Not handled yet");
		}
	}
	
	protected String getInstanceStereotype(Object o) {
		return getAlias("instance");
	}

	protected String getStereotype(Class<?> o) {
		String as = getInheritedStereotypeFromAnnoatation(o);
		if (as != null)
			return as;

		if (o.isInterface()) {
			return getAlias("interface");
		} else if (o.isEnum()) {
			return getAlias("enum");
		} else {
			return getAlias("class");
		}

	}

	protected String getStereotype(Method o) {
		String as = getStereotypeFromAnnotation(o);
		if (as != null)
			return as;

		return getAlias("method");
	}
	
	protected String getStereotype(Constructor<?> o) {
		String as = getStereotypeFromAnnotation(o);
		if (as != null)
			return as;

		return getAlias("constructor");
	}

	protected String getStereotype(Package o) {
		String as = getStereotypeFromAnnotation(o);
		if (as != null)
			return as;

		return getAlias("package");
	}

	protected String getStereotype(Enum<?> e) {
		String as = getInheritedStereotypeFromAnnoatation(e.getClass());
		if (as != null)
			return as;

		return getAlias("enum");
	}

	protected String getStereotype(Annotation a) {
		Class<?> cl = a.annotationType();
		String aa = getStereotypeFromAnnotation(cl);
		if (aa != null)
			return aa;

		return getAlias("annotation");
	}

	protected String getStereotype(String s) {
		return "";
	}

	protected String getStereotypeFromAnnotation(AnnotatedElement p) {
		K9OnDiagram ann = p.getAnnotation(K9OnDiagram.class);
		if (ann != null) {
			String aa = ann.stereotype();
			if (aa.length() > 0)
				return aa;
		}

		return null;
	}

	protected String getInheritedStereotypeFromAnnoatation(Class<?> c) {
		String current = getStereotypeFromAnnotation(c);
		if (current != null)
			return current;

		for (Class<?> i : c.getInterfaces()) {
			String ic = getStereotypeFromAnnotation(i);
			if (ic != null) {
				return ic;
			}
		}

		if (!(c.getSuperclass() == null)) {
			String sup = getInheritedStereotypeFromAnnoatation(c.getSuperclass());
			return sup;
		}

		return null;
	}
}
