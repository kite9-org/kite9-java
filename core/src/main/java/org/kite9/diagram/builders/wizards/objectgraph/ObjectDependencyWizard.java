package org.kite9.diagram.builders.wizards.objectgraph;

import java.util.Stack;

import org.kite9.diagram.builders.formats.BasicFormats;
import org.kite9.diagram.builders.formats.PropositionFormat;
import org.kite9.diagram.builders.java.AbstractJavaBuilder;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.java.FieldValueBuilder;
import org.kite9.diagram.builders.java.ObjectBuilder;
import org.kite9.diagram.builders.krmodel.AbstractBuilder;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.primitives.Container;

public class ObjectDependencyWizard {

	DiagramBuilder db;
	PropositionFormat objectFormat;  
	
	public ObjectDependencyWizard(DiagramBuilder db, Container c) {
		this.db = db;
		objectFormat = BasicFormats.asConnectionWithBody(db.getInsertionInterface(), BasicFormats.asGlyph(null), Direction.RIGHT, c);
	}
	
	public void show(Object o) {
		Stack<AbstractJavaBuilder> s = new Stack<AbstractJavaBuilder>();
		s.add(db.withObjects(o));
		while (s.size() > 0) {
			AbstractBuilder ob = s.pop();
			if (ob instanceof ObjectBuilder) {
				if (((ObjectBuilder) ob).size()>0) {
					((ObjectBuilder)ob).show(objectFormat);
					s.add(((ObjectBuilder)ob).withFieldValues(null));
				}
			} else if (ob instanceof FieldValueBuilder) {
				s.add(((FieldValueBuilder)ob).withValues(null));
			}
		}
		
	}
}
