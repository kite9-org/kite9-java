package org.kite9.diagram.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.wizards.objectgraph.ObjectDependencyWizard;
import org.kite9.framework.Kite9Item;

public class Test18ObjectDependencyWizard extends AbstractBuilderTest {
	
	static class In {
		
	}
	
	static interface Thing {
		
	}
	
	static class Gizmo implements Thing {
		
	}
	
	static class Gizmo2 implements Thing {
		
		
	}

	static class MyOb {
		
		List<Helper> helpers = new ArrayList<Helper>();
		
		public In process(In in) {
			for (Helper h : helpers) {
				in = h.doX(in);
				in = h.doY(in);
			}
			return in;
		}
		
		Thing t = new Gizmo();
	}
	
	static interface Helper {
		
		@K9OnDiagram
		public In doX(In in);
		
		public In doY(In in);
	}
	
	static class AHelper implements Helper {
		
		Thing t = new Gizmo2();
		
		public AHelper() {
			
		}

		public In doX(In in) {
			return in;
		}

		public In doY(In in) {
			return in;
		}

	}
	
	static class BHelper implements Helper {
		
		Thing t = new Gizmo();
		
		private MyOb backLink;
		
		public BHelper(MyOb backLink) {
			this.backLink = backLink;
		}

		public In doX(In in) {
			System.out.println("Backlink: "+backLink+" "+in);
			return in;
		}

		public In doY(In in) {
			return in;
		}
	}
	
	public MyOb getTheObject() {
		MyOb ob = new MyOb();
		ob.helpers.add(new AHelper());
		ob.helpers.add(new BHelper(ob));
		return ob;
	}
	
	@Test
	@Kite9Item
	public void test18_1_ObjectDependencyWizard1() throws IOException {
		MyOb ob = getTheObject();
		DiagramBuilder db = createBuilder();
		ObjectDependencyWizard odw = new ObjectDependencyWizard(db, null);
		odw.show(ob);
		renderDiagram(db.getDiagram());
	}
	
	@Before
	public void setUpModel() {
		pmi.addClass(convertClassName(Test18ObjectDependencyWizard.class));
		pmi.addClass(convertClassName(MyOb.class));
		pmi.addClass(convertClassName(Thing.class));
		pmi.addClass(convertClassName(Gizmo.class));
		pmi.addClass(convertClassName(Helper.class));
		pmi.addClass(convertClassName(AHelper.class));
		pmi.addClass(convertClassName(BHelper.class));
		pmi.addSubclass(convertClassName(Thing.class), convertClassName(Gizmo.class));
		pmi.addSubclass(convertClassName(Helper.class), convertClassName(AHelper.class));
		pmi.addSubclass(convertClassName(Helper.class), convertClassName(BHelper.class));
	}
}
