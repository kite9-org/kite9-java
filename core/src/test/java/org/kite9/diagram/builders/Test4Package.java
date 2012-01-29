package org.kite9.diagram.builders;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.kite9.diagram.builders.formats.Format;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.model.PackageHandle;


public class Test4Package extends AbstractBuilderTest {

    @Kite9Item
    @Test
    public void test_4_1_PackageAsContext() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withPackages(Test4Package.class)
		.show(db.asConnectedContexts())
		.withMembers(Test4Package.class)
			.show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
	
    }
    
    @Kite9Item
    @Test
    public void test_4_2_PackageAsContextWithClassContents1() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withPackages(Test4Package.class)
		.show(db.asConnectedContexts())
		.withMemberClasses(null)
			.show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    @Kite9Item
    @Test
    public void test_4_3_PackageAsContextWithClassContents2() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withPackages(Test4Package.class)
		.show(db.asConnectedContexts())
		.withMemberClasses(null).reduce(db.only(Test4Package.class))
			.show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    
    @Kite9Item
    @Test
    public void test_4_4_PackageDependency() throws IOException {
	DiagramBuilder db = createBuilder();
	db.withPackages(Test4Package.class)
		.show(db.asConnectedGlyphs())
		.withDependencies(null)
			.show(db.asConnectedGlyphs());
	renderDiagram(db.getDiagram());
    }
    
    
    
    @Before
    public void setUpModel() {
	pmi.addClass(convertClassName(Test3Field.class));  // adding this to help test filtering
	pmi.addClass(convertClassName(Test4Package.class));  
	Package p = this.getClass().getPackage();
	pmi.addPackageClass(convertPackageName(p), convertClassName(Test3Field.class));
	pmi.addPackageClass(convertPackageName(p), convertClassName(Test4Package.class));
	Package pack2= Format.class.getPackage();
	PackageHandle p1 = new PackageHandle(convertPackageName(p), convertClassName(Test4Package.class));
	PackageHandle p2 = new PackageHandle(convertPackageName(pack2), convertClassName(Format.class));
	pmi.addPackageDependency(p1, p2);
    }
}
