package org.kite9.diagram.builders;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.builders.wizards.er.EntityRelationshipWizard;
import org.kite9.framework.Kite9Item;

public class Test17ArchitectureWizard extends AbstractBuilderTest {

	interface MySite {
		
		String getName();
		
	}
	
	abstract class Server implements MySite {
		
		Sat1 hasSat1;
		List<Sat2> allTheSat2s;
		
		public abstract Sat3 getSat3();
	}
	
	interface Satellite {
	}
	
	abstract class Sat1 implements Satellite {
	}
	
	abstract class Sat2 implements Satellite {
	}
	
	abstract class Sat3 implements Satellite {
	}
	
	@Test
	@Kite9Item
	public void test_17_1_TestArchitectureWizard() throws IOException {
		DiagramBuilder db = createBuilder();
		String rh = "Rob's Architecture";
		StringBuilder context = db.withStrings(rh).show(db.asContexts());
		EntityRelationshipWizard erw = new EntityRelationshipWizard(db);
		erw.show(context.withClasses(HasRelationship.CLASS_GROUP, Test17ArchitectureWizard.class).withInnerClasses(null, false), null);
		renderDiagram(db.getDiagram());
	}
	
	@Before
	public void setUpModel() {
		pmi.addClass(convertClassName(Test17ArchitectureWizard.class));
		pmi.addClass(convertClassName(Server.class));
		pmi.addClass(convertClassName(MySite.class));
		pmi.addClass(convertClassName(Satellite.class));
		pmi.addClass(convertClassName(Sat1.class));
		pmi.addClass(convertClassName(Sat2.class));
		pmi.addClass(convertClassName(Sat3.class));
		pmi.addSubclass(convertClassName(Satellite.class), convertClassName(Sat1.class));
		pmi.addSubclass(convertClassName(Satellite.class), convertClassName(Sat2.class));
		pmi.addSubclass(convertClassName(Satellite.class), convertClassName(Sat3.class));

		pmi.addSubclass(convertClassName(MySite.class), convertClassName(Server.class));

	}
}
