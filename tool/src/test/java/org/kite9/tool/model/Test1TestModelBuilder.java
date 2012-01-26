package org.kite9.tool.model;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.kite9.diagram.annotation.K9OnDiagram;
import org.kite9.framework.Kite9Item;
import org.kite9.framework.model.AnnotationHandle;
import org.kite9.framework.model.ConstructorHandle;
import org.kite9.framework.model.FieldHandle;
import org.kite9.framework.model.MemberHandle;
import org.kite9.framework.model.MethodHandle;
import org.kite9.framework.model.PackageHandle;
import org.kite9.framework.model.ProjectModel;
import org.kite9.tool.AbstractRunnerTest;
import org.kite9.tool.model.Construct.Con1;
import org.kite9.tool.model.Construct.Con2;
import org.kite9.tool.model.Refs.Reference;
import org.kite9.tool.model.SubClassing.SomeInterface;
import org.kite9.tool.model.SubClassing.SomeSubClass;
import org.kite9.tool.model.SubClassing.SomeSubClass2;
import org.kite9.tool.model.sub1.SubPackageDependency;
import org.kite9.tool.model.sub2.SubPackageDependency2;

public class Test1TestModelBuilder extends AbstractRunnerTest {

	static ProjectModel pm;

	@BeforeClass
	public static void buildModel() throws IOException {
		pm = createModel();
	}

	@Test
	public void test_1_1_TestMethodBCallersContainsA() throws IOException, SecurityException, NoSuchMethodException {
		Method mb = CallerCalling.class.getMethod("methodB");
		Method ma = CallerCalling.class.getMethod("methodA");

		Assert.assertTrue(pm.getCalledBy(new MethodHandle(mb)).contains(new MethodHandle(ma)));
	}

	@Test
	public void test_1_2_TestMethodACallingContainsB() throws IOException, SecurityException, NoSuchMethodException {
		Method mb = CallerCalling.class.getMethod("methodB");
		Method ma = CallerCalling.class.getMethod("methodA");

		Assert.assertTrue(pm.getCalls(new MethodHandle(ma)).contains(new MethodHandle(mb)));
	}

	@Test
	public void test_1_3_TestMethodBCallingField() throws IOException, SecurityException, NoSuchMethodException,
			NoSuchFieldException {
		Method mb = CallerCalling.class.getMethod("methodB");
		Field sub = CallerCalling.class.getDeclaredField("sub");
		Assert.assertTrue(pm.getCalls(new MethodHandle(mb)).contains(new FieldHandle(sub)));
	}

	@Test
	public void test_1_4_TestFieldCalledByMethodB() throws IOException, SecurityException, NoSuchFieldException,
			NoSuchMethodException {
		Method mb = CallerCalling.class.getMethod("methodB");
		Field sub = CallerCalling.class.getDeclaredField("sub");
		Assert.assertTrue(pm.getCalledBy(new FieldHandle(sub)).contains(new MethodHandle(mb)));
	}

	@Test
	public void test_1_5_TestSubClassing() throws IOException {
		Set<String> subs = pm.getSubclasses(convertName(SubClassing.class));
		Assert.assertTrue(subs.contains(convertName(SomeSubClass.class)));
		Assert.assertTrue(subs.contains(convertName(SomeSubClass2.class)));
	}

	@Test
	public void test_1_6_TestImplementation() throws IOException {
		Set<String> subs = pm.getSubclasses(convertName(SomeInterface.class));
		Assert.assertTrue(subs.contains(convertName(SomeSubClass2.class)));
	}

	@Test
	public void test_1_7_ClassHavingAnnotation() throws IOException {
		Set<String> subs = pm.getClassesWithAnnotation(convertName(Kite9Item.class));
		Assert.assertTrue(subs.contains(convertName(Annotated.class)));
	}

	@Test
	public void test_1_8_MethodHavingAnnotation() throws IOException, SecurityException, NoSuchMethodException {
		Set<MemberHandle> subs = pm.getMembersWithAnnotation(convertName(Kite9Item.class));
		Method sm = Annotated.class.getMethod("someMethod");
		Assert.assertTrue(subs.contains(new MethodHandle(sm)));
	}

	@Test
	public void test_1_9_ClassHavingAnnotation() throws IOException, SecurityException, NoSuchFieldException {
		Set<MemberHandle> subs = pm.getMembersWithAnnotation(convertName(K9OnDiagram.class));
		Field sf = Annotated.class.getField("someField");
		Assert.assertTrue(subs.contains(new FieldHandle(sf)));
	}

	@Test
	public void test_1_10_ClassInModel() throws IOException {
		Assert.assertTrue(pm.withinModel(convertName(SubClassing.class)));
		Assert.assertFalse(pm.withinModel(convertName(Object.class)));
	}

	@Test
	public void test_1_11_ClassInPackage() throws IOException {
		Package p = this.getClass().getPackage();
		Set<String> contents = pm.getClassesInPackage(convertName(p));
		Assert.assertTrue(contents.contains(convertName(CallerCalling.class)));
		Assert.assertFalse(contents.contains(convertName(Object.class)));
	}

	@Test
	public void test_1_12_ClassDependency1() throws IOException {
		Set<String> deps = pm.getDependsOnClasses(convertName(Dependency.class));
		Assert.assertTrue(deps.contains(convertName(TreeMap.class)));
		Assert.assertTrue(deps.contains(convertName(URL.class)));
		Assert.assertFalse(deps.contains(convertName(Map.class)));
	}

	@Test
	public void test_1_13_PackageDependency() throws IOException {
		PackageHandle sub1 = new PackageHandle(SubPackageDependency.class);
		PackageHandle sub2 = new PackageHandle(SubPackageDependency2.class);
		PackageHandle current = new PackageHandle(this.getClass());

		Assert.assertTrue(pm.getDependsOnPackages(sub1).contains(current));
		Assert.assertTrue(pm.getDependsOnPackages(sub2).contains(current));
		Assert.assertTrue(pm.getDependedOnPackages(current).contains(sub1));
		Assert.assertTrue(pm.getDependedOnPackages(current).contains(sub2));	
	}

	@Test
	public void test_1_14_AnnotationReference() throws Exception {
		Set<AnnotationHandle> refs = pm.getAnnotationReferences(convertName(Refs.Referenced.class));
		Method m = Refs.class.getMethod("referencer");
		MethodHandle mh = new MethodHandle(m);
		Reference ref = m.getAnnotation(Reference.class);
		AnnotationHandle expected = new AnnotationHandle(ref, mh, "refs");
		Assert.assertTrue(refs.contains(expected));
	}
	
	@Test
	public void test_1_15_ClassDependency2() throws IOException {
		Set<String> deps = pm.getDependsOnClasses(convertName(SomeSubClass2.class));
		Assert.assertTrue(deps.contains(convertName(SomeInterface.class)));
		Assert.assertTrue(deps.contains(convertName(SubClassing.class)));
		Assert.assertFalse(deps.contains(convertName(SomeSubClass.class)));
	}
	
	@Test
	public void test_1_16_ClassDependency3() throws IOException {
		Set<String> deps = pm.getDependedOnClasses(convertName(SomeInterface.class));
		Assert.assertFalse(deps.contains(convertName(SomeSubClass.class)));
		Assert.assertTrue(deps.contains(convertName(SomeSubClass2.class)));
	}
	
	@Ignore
	@Test
	public void test_1_17_TestConstructor1CallingContainsConstructor2() throws IOException, SecurityException, NoSuchMethodException {
		Constructor<?> c1 = Con1.class.getConstructor();
		Constructor<?> c2 = Con2.class.getConstructor();
		
		Assert.assertTrue(pm.getCalls(new ConstructorHandle(c1)).contains(new ConstructorHandle(c2)));
	}

	private String convertName(Class<?> c) {
		return MemberHandle.convertClassName(c);
	}

	private String convertName(Package p) {
		return MemberHandle.convertPackageName(p);
	}
}
