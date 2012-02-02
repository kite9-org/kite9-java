package org.kite9.java.examples;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kite9.diagram.adl.Arrow;
import org.kite9.diagram.adl.Context;
import org.kite9.diagram.adl.Diagram;
import org.kite9.diagram.adl.Glyph;
import org.kite9.diagram.adl.Link;
import org.kite9.diagram.adl.LinkEndStyle;
import org.kite9.diagram.builders.Filter;
import org.kite9.diagram.builders.java.ClassBuilder;
import org.kite9.diagram.builders.java.DiagramBuilder;
import org.kite9.diagram.builders.java.ObjectBuilder;
import org.kite9.diagram.builders.java.PackageBuilder;
import org.kite9.diagram.builders.krmodel.Relationship;
import org.kite9.diagram.builders.wizards.fsm.EnumWithAnnotationFSMDataProvider;
import org.kite9.diagram.builders.wizards.fsm.FSMDataProvider;
import org.kite9.diagram.builders.wizards.fsm.FiniteStateMachineWizard;
import org.kite9.diagram.builders.wizards.sequence.ClassBasedSequenceDiagramDataProvider;
import org.kite9.diagram.builders.wizards.sequence.ColumnSequenceDiagramWizard;
import org.kite9.diagram.builders.wizards.sequence.MethodBasedSequenceDiagramDataProvider;
import org.kite9.diagram.builders.wizards.sequence.NoLayoutSequenceDiagramWizard;
import org.kite9.diagram.position.Direction;
import org.kite9.diagram.position.Layout;
import org.kite9.diagram.primitives.Contained;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.common.HelpMethods;
import org.kite9.java.examples.library.AfterState;
import org.kite9.java.examples.library.BeforeState;
import org.kite9.java.examples.library.Book;
import org.kite9.java.examples.library.BookState;
import org.kite9.java.examples.library.actors.Actor;
import org.kite9.java.examples.library.actors.Member;
import org.kite9.java.examples.library.actors.Person;
import org.kite9.java.examples.library.server.LibraryFacade;
import org.kite9.java.examples.library.usecases.UseCase;
import org.kite9.java.examples.library.usecases.Uses;

public class Examples {

	private void addLink(Glyph collection, Glyph list, String string, Collection<Contained> top) {
		Arrow a = new Arrow(string);
		new Link(a, collection, null, null, LinkEndStyle.ARROW, null, Direction.UP);
		new Link(list, a, null, null, null, null, Direction.UP);
		top.add(a);
	}

	@Kite9Item
	public Diagram example_1_1_ClassHierarchy() throws IOException {
		Glyph collection = new Glyph("C", "interface", "Collection", null, null);
		Context c1 = new Context("C1", HelpMethods.listOf(collection), false, null, null);

		Glyph list = new Glyph("L", "interface", "List", null, null);
		Glyph set = new Glyph("S", "interface", "Set", null, null);

		Context c2 = new Context("C2", HelpMethods.listOf(set, list), false, null, Layout.RIGHT);

		Glyph hs = new Glyph("HS", "class", "HashSet", null, null);
		Glyph ts = new Glyph("TS", "class", "TreeSet", null, null);
		Glyph lhs = new Glyph("LHS", "class", "LinkedHashSet", null, null);
		Glyph al = new Glyph("AL", "class", "ArrayList", null, null);
		Glyph ll = new Glyph("LL", "class", "LinkedList", null, null);

		Context c3 = new Context("C3", HelpMethods.listOf(hs, ts, lhs, al, ll), false, null, Layout.RIGHT);

		List<Contained> c2Links = new ArrayList<Contained>(3);
		List<Contained> c3Links = new ArrayList<Contained>(6);
		
		Context c2e = new Context("C2e", c2Links, false, null, Layout.RIGHT);
		Context c3e = new Context("C3e", c3Links, false, null, Layout.RIGHT);

		List<Contained> top = HelpMethods.listOf(c1, c2, c3, c2e, c3e);
		
		// order must be same as containers above
		addLink(collection, set, "extends", c2Links);
		addLink(collection, list, "extends", c2Links);
		addLink(set, hs, "implements", c3Links);
		addLink(set, ts, "implements", c3Links);
		addLink(set, lhs, "implements", c3Links);
		addLink(list, al, "implements", c3Links);
		addLink(list, ll, "implements", c3Links);

		return new Diagram(top, null);
	}

	@Kite9Item
	public Diagram example_1_3_FlowChart(DiagramBuilder builder) throws IOException {
		ObjectBuilder checkLaundry = builder.withObjects("Check Laundry Basket").show(builder.asConnectedGlyphs("choice"));
		checkLaundry.withObjects(null, "Is it empty?").show(builder.asTextLines());
		checkLaundry.withObjects(new Relationship("yes", Direction.RIGHT), "All Done!").show(builder.asConnectedGlyphs());

		Relationship no1 = new Relationship("no", Direction.DOWN);
		ObjectBuilder loadLaundry = checkLaundry.withObjects(no1, "Load Laundry Into Machine").show(
				builder.asConnectedGlyphs("action"));
		loadLaundry.withObjects(null, "Ensure machine is empty first", "Add powder, close door", "Start programme")
				.show(builder.asTextLines());

		String finished = "Is Cycle Finished?";
		ObjectBuilder isFinished = loadLaundry.withObjects(new Relationship("wait"), finished).show(
				builder.asConnectedGlyphs("choice"));

		ObjectBuilder waiting = isFinished.withObjects(new Relationship("no"), "Have a Coffee")
				.show(builder.asConnectedGlyphs());

		waiting.withObjects(new Relationship("check again"), finished).show(builder.asConnectedGlyphs());

		Relationship yes = new Relationship("yes");
		ObjectBuilder sunny = isFinished.withObjects(yes, "Is it Sunny?").show(builder.asConnectedGlyphs("choice"));

		sunny.withObjects(new Relationship("it's fine"), "Hang on the Line").show(builder.asConnectedGlyphs("action"));
		sunny.withObjects(new Relationship("it's wet"), "Hang on the Dryer").show(builder.asConnectedGlyphs("action"));

		return builder.getDiagram();
	}

	@Kite9Item
	public Diagram example_1_5_UseCases(DiagramBuilder builder) throws IOException {
		// show use cases inside a context
		PackageBuilder ucp = builder.withPackages(UseCase.class);
		ucp.show(builder.asConnectedContexts());
		ClassBuilder uc = ucp.withMemberClasses(builder.not(builder.only(UseCase.class, Uses.class)));
		uc.show(builder.asConnectedGlyphs(null));

		// show actors inside context
		PackageBuilder ac = builder.withPackages(Actor.class);
		ac.show(builder.asConnectedContexts(true, Layout.VERTICAL));
		ClassBuilder contents = ac.withMembers(Person.class);
		contents.show(builder.asConnectedGlyphs());
		contents.withMethods(null, false).show(builder.asTextLines());
		contents.withSubClasses(builder.onlyAnnotated(), true).show(builder.asConnectedGlyphs())
			.withMethods(null, false).show(builder.asTextLines());
		// show references between the two
		uc.withReferencingAnnotatedElements(builder.only(Uses.class)).show(builder.asConnectedGlyphs(null,Direction.RIGHT));
		

		Diagram d = builder.getDiagram();
		d.setLayoutDirection(Layout.HORIZONTAL);
		return d;
	}

	@Kite9Item
	public Diagram example_1_6_Packaging(DiagramBuilder db) throws IOException {
		PackageBuilder p1 = db.withPackages(UseCase.class, Actor.class, Uses.class);
		p1.show(db.asConnectedGlyphs());
		p1.withMemberClasses(null).show(db.asTextLines());
		p1.withDependencies(new Filter<Package>() {

			public boolean accept(Package o) {
				return o.getName().startsWith("org.kite9.java.examples");
			}

		}).show(db.asConnectedGlyphs());

		return db.getDiagram();
	}

	@Kite9Item
	public Diagram example_1_7_ClassDependency(DiagramBuilder db) throws IOException {
		ClassBuilder cb = db.withClasses(Book.class, Member.class);
		cb.show(db.asConnectedGlyphs());
		cb.withDependencies(db.not(db.only(Object.class)), true).show(db.asConnectedGlyphs());
		return db.getDiagram();
	}

	@Kite9Item
	public Diagram example_1_8_StateTransition(DiagramBuilder db) throws Exception {
		ClassBuilder cb = db.withClasses(Book.class);
		cb.show(db.asConnectedContexts());
		Context c = (Context) db.getNounElement(Book.class);
		FiniteStateMachineWizard fsmFormat = new FiniteStateMachineWizard(db, c);
		Field f = Book.class.getDeclaredField("state");
		FSMDataProvider provider = new EnumWithAnnotationFSMDataProvider(db, f, BookState.class, BeforeState.class,
				AfterState.class);
		fsmFormat.write(provider);

		return db.getDiagram();
	}

	@Kite9Item
	public Diagram example_1_9_SequenceDiagramMethods(DiagramBuilder db) throws Exception {
		Method m = LibraryFacade.class.getDeclaredMethod("borrow", int.class, int.class);
		final String packageLimit = Book.class.getPackage().getName();
		MethodBasedSequenceDiagramDataProvider mbsddp = createMethodProvider(db, m, packageLimit);
		ColumnSequenceDiagramWizard format = new ColumnSequenceDiagramWizard(db);
		format.write(mbsddp, db.getDiagram());
		return db.getDiagram();
	}
	
	@Kite9Item
	public Diagram example_1_10_SequenceDiagramClasses(DiagramBuilder db) throws Exception {
		Method m = LibraryFacade.class.getDeclaredMethod("borrow", int.class, int.class);
		final String packageLimit = Book.class.getPackage().getName();
		ClassBasedSequenceDiagramDataProvider mbsddp = createClassProvider(db, m, packageLimit);
		ColumnSequenceDiagramWizard format = new ColumnSequenceDiagramWizard(db);
		format.write(mbsddp, db.getDiagram());
		db.withKeyText("BorrowBook", "Exception routes are ommitted for clarity");
		return db.getDiagram();
	}
	
	@Kite9Item
	public Diagram example_1_11_MethodCallDiagramMethods(DiagramBuilder db) throws Exception {
		Method m = LibraryFacade.class.getDeclaredMethod("borrow", int.class, int.class);
		final String packageLimit = Book.class.getPackage().getName();
		MethodBasedSequenceDiagramDataProvider mbsddp = createMethodProvider(db, m, packageLimit);
		NoLayoutSequenceDiagramWizard format = new NoLayoutSequenceDiagramWizard(db);
		format.write(mbsddp, db.getDiagram());
		return db.getDiagram();
	}

	@Kite9Item
	public Diagram example_1_12_MethodCallDiagramClasses(DiagramBuilder db) throws Exception {
		Method m = LibraryFacade.class.getDeclaredMethod("borrow", int.class, int.class);
		final String packageLimit = Book.class.getPackage().getName();
		ClassBasedSequenceDiagramDataProvider mbsddp = createClassProvider(db, m, packageLimit);
		NoLayoutSequenceDiagramWizard format = new NoLayoutSequenceDiagramWizard(db);
		format.write(mbsddp, db.getDiagram());
		db.withKeyText("BorrowBook", "Exception routes are ommitted for clarity");
		return db.getDiagram();
	}

	private ClassBasedSequenceDiagramDataProvider createClassProvider(DiagramBuilder db, Method m,
			final String packageLimit) {
		return new ClassBasedSequenceDiagramDataProvider(db, m,
				new Filter<AccessibleObject>() {

					public boolean accept(AccessibleObject o) {
						if ((o instanceof Constructor<?>) || (o instanceof Method)) {
							java.lang.reflect.Member m = (java.lang.reflect.Member) o;
							Class<?> declaringClass = m.getDeclaringClass();
							String packageName = declaringClass.getPackage().getName();
							if ((packageName.contains(packageLimit)) && (!declaringClass.getName().endsWith("Exception"))) {
								return true;
							}
						}
						return false;
					}

				});
	}
	
	
	
	private MethodBasedSequenceDiagramDataProvider createMethodProvider(DiagramBuilder db, Method m,
			final String packageLimit) {
		return new MethodBasedSequenceDiagramDataProvider(db, m,
				new Filter<AccessibleObject>() {

					public boolean accept(AccessibleObject o) {
						if ((o instanceof Constructor<?>) || (o instanceof Method)) {
							java.lang.reflect.Member m = (java.lang.reflect.Member) o;
							if (m.getDeclaringClass().getPackage().getName().contains(packageLimit)) {
								return true;
							}
						}
						return false;
					}

				});
	}
	
	
}
