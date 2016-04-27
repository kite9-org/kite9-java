package org.kite9.framework.serialization;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.kite9.diagram.builders.id.Address;
import org.kite9.diagram.builders.id.AddressImpl;

public class Test4CompoundIDs {

	@Test
	public void test_4_1_PackAndUnpack() {
		String someId = "{c[someClassName]p[org.kite9.java]}";
		Address done = new AddressImpl(someId);
		Assert.assertEquals("someClassName", done.get("c"));
		Assert.assertEquals("org.kite9.java", done.get("p"));
		String newId = done.toString();
		Assert.assertEquals(someId, newId);
	}
	
	@Test
	public void test_4_2_Process() {
		String someId = "{x[in]y[some stuff]}";
		String out = new AddressImpl(someId).extend("u", "http://bobbins").toString();
		Assert.assertEquals("{u[http://bobbins]x[in]y[some stuff]}", out);
	}
	
	@Test
	public void test_4_3_HandleSpaces() {
		String someId = "{x [in] y  [some stuff]}";
		String out = new AddressImpl(someId).extend("u ", "http://bobbins").toString();
		Assert.assertEquals("{u[http://bobbins]x[in]y[some stuff]}", out);
	}
	
	@Test
	public void test_4_4_UpgradesLocalIds() {
		String someId = "some local ][{id";
		String out = new AddressImpl(someId).extend("u", "http://bobbins").toString();
		Assert.assertEquals("{u[http://bobbins]z[some local id]}", out);
	}
}
