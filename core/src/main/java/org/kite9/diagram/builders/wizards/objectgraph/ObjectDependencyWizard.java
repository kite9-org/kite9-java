package org.kite9.diagram.builders.wizards.objectgraph;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Stack;

import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.formats.BasicFormats;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.java.AbstractJavaBuilder;
import org.kite9.diagram.builders.java.AccessibleValueBuilder;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.java.ObjectBuilder;
import org.kite9.diagram.builders.krmodel.AbstractBuilder;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.primitives.Container;

public class ObjectDependencyWizard {

	DiagramBuilder db;
	PropositionFormat objectFormatNew;  
	Filter<Field> fieldFilter = new Filter<Field>() {

		public boolean accept(Field f) {
			if (Modifier.isStatic(f.getModifiers())) {
				return false;
			}
			
			if (db.isExcluded(f)) {
				return false;
			}
			
			return true;
		}
	};
	
	public Filter<Field> getFieldFilter() {
		return fieldFilter;
	}

	public void setFieldFilter(Filter<Field> fieldFilter) {
		this.fieldFilter = fieldFilter;
	}

	public Filter<Object> getValueFilter() {
		return valueFilter;
	}

	public void setValueFilter(Filter<Object> valueFilter) {
		this.valueFilter = valueFilter;
	}
	
	public boolean isPrimitive(Class<?> c) {		
		if ((c==int.class) || (c==float.class) || (c==double.class) || (c==boolean.class) 
				|| (c==char.class) || (c==short.class) || (c==void.class)) {
			return true;
		}
		
		if ((c==Integer.class) || (c == Float.class) || (c==Double.class) || (c==Boolean.class)
				|| (c==Character.class) || (c==Short.class) || (c==Void.class)) {
			return true;
		}
		
		return false;
	}

	Filter<Object> valueFilter;
	
	public PropositionFormat getObjectFormatNew() {
		return objectFormatNew;
	}

	public void setObjectFormatNew(PropositionFormat objectFormatNew) {
		this.objectFormatNew = objectFormatNew;
	}

	public PropositionFormat getObjectFormatExisting() {
		return objectFormatExisting;
	}

	public void setObjectFormatExisting(PropositionFormat objectFormatExisting) {
		this.objectFormatExisting = objectFormatExisting;
	}

	PropositionFormat objectFormatExisting;
	
	private Filter<? super Method> methodFilter = new Filter<Method>() {

		public boolean accept(Method o) {
			if (o.getParameterTypes().length >0) {
				return false;
			}
			
			if (Modifier.isStatic(o.getModifiers())) {
				return false;
			}
			
			if (isPrimitive(o.getReturnType())) {
				return false;
			}
			
			if (Modifier.isNative(o.getModifiers())) {
				return false;
			}
			
			if (Modifier.isAbstract(o.getModifiers())) {
				return false;
			}
			
			if (db.isExcluded(o)) {
				return false;
			}
			
			String name = o.getName();
			return (name.startsWith("get") || name.startsWith("is"));
		}
		
	};
	
	private boolean showFieldValues = true;
	
	public boolean isShowFieldValues() {
		return showFieldValues;
	}

	public void setShowFieldValues(boolean showFieldValues) {
		this.showFieldValues = showFieldValues;
	}

	private boolean showMethodReturnValues = false;
	
	public boolean isShowMethodReturnValues() {
		return showMethodReturnValues;
	}

	public void setShowMethodReturnValues(boolean showMethodReturnValues) {
		this.showMethodReturnValues = showMethodReturnValues;
	}

	public Filter<? super Method> getMethodFilter() {
		return methodFilter;
	}

	public void setMethodFilter(Filter<? super Method> methodFilter) {
		this.methodFilter = methodFilter;
	}

	public ObjectDependencyWizard(DiagramBuilder db, Container c) {
		this.db = db;
		objectFormatNew = BasicFormats.asConnectionWithBody(db.getInsertionInterface(), BasicFormats.asGlyph(null), Direction.RIGHT, c);
		objectFormatExisting = BasicFormats.asConnectionWithBody(db.getInsertionInterface(), BasicFormats.asGlyph(null), null, c);
		valueFilter = db.onlyInModel();
	}
	
	public void show(Object o) {
		Stack<AbstractJavaBuilder> s = new Stack<AbstractJavaBuilder>();
		s.add(db.withObjects(o));
		while (s.size() > 0) {
			AbstractBuilder ob = s.pop();
			if (ob instanceof ObjectBuilder) {
				if (((ObjectBuilder) ob).size()>0) {
					ObjectBuilder notDoneYet = ((ObjectBuilder)ob).reduce(db.not(db.onlyOnDiagram()));
					ObjectBuilder alreadyDone = ((ObjectBuilder)ob).reduce(db.onlyOnDiagram());
					notDoneYet.show(objectFormatNew);
					alreadyDone.show(objectFormatExisting);
					if (isShowFieldValues()) {
						s.add(notDoneYet.withFieldValues(fieldFilter));
					}
					if (isShowMethodReturnValues()) {
						s.add(notDoneYet.withMethodReturnValues(methodFilter));
					}
				}
			} else if (ob instanceof AccessibleValueBuilder) {
				s.add(((AccessibleValueBuilder)ob).withValues(valueFilter));
			}
		}
		
	}
}
