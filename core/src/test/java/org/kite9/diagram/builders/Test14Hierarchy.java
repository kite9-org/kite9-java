package org.kite9.diagram.builders;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.builders.wizards.hierarchy.HierarchyWizard;
import org.kite9.diagram.position.Layout;
import org.kite9.framework.Kite9Item;

public class Test14Hierarchy extends AbstractBuilderTest {

	interface Top {
	}

	interface Middle1 extends Top {
	}

	interface Middle2 extends Top {
	}

	class Abstract1 implements Top {

	}

	class Abstract2 implements Middle1 {

	}

	class Conc1 extends Abstract1 implements Middle2 {

	}

	class Conc2 extends Abstract1 implements Middle1 {

	}

	class Conc3 extends Abstract2 implements Middle1 {

	}

	@Test
	@Kite9Item
	public void test_14_1_TestHierarchy() throws IOException {
		DiagramBuilder db = createBuilder();
		String rh = "Rob's Hierarchy";
		db.withStrings(rh).show(db.asConnectedContexts());
		HierarchyWizard hb = new HierarchyWizard(rh, db);
		hb.add(true, Top.class);
		hb.showClasses(db.asConnectedContexts(false, Layout.HORIZONTAL), db.asConnectedGlyphs(""));
		hb.showInheritance(db.asConnectedGlyphs(""));
		renderDiagram(db.getDiagram());
	}

	@Before
	public void setUpModel() {
		pmi.addClass(convertClassName(Top.class));
		pmi.addClass(convertClassName(Middle1.class));
		pmi.addClass(convertClassName(Middle2.class));
		pmi.addClass(convertClassName(Conc1.class));
		pmi.addClass(convertClassName(Conc2.class));
		pmi.addClass(convertClassName(Conc3.class));
		pmi.addClass(convertClassName(Abstract1.class));
		pmi.addClass(convertClassName(Abstract2.class));
		pmi.addSubclass(convertClassName(Top.class), convertClassName(Middle1.class));
		pmi.addSubclass(convertClassName(Top.class), convertClassName(Middle2.class));
		pmi.addSubclass(convertClassName(Top.class), convertClassName(Abstract1.class));

		pmi.addSubclass(convertClassName(Middle1.class), convertClassName(Abstract2.class));
		pmi.addSubclass(convertClassName(Middle1.class), convertClassName(Conc2.class));
		pmi.addSubclass(convertClassName(Middle1.class), convertClassName(Conc3.class));

		pmi.addSubclass(convertClassName(Middle2.class), convertClassName(Conc1.class));

		pmi.addSubclass(convertClassName(Abstract1.class), convertClassName(Conc1.class));
		pmi.addSubclass(convertClassName(Abstract1.class), convertClassName(Conc2.class));
		pmi.addSubclass(convertClassName(Abstract2.class), convertClassName(Conc3.class));

	}

}
