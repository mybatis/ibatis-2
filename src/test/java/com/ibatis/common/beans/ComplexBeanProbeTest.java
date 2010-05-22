package com.ibatis.common.beans;

import junit.framework.TestCase;

public class ComplexBeanProbeTest extends TestCase {

	public void testSetObject() {
		SimpleClass mySimpleClass = new SimpleClass();
		Probe probe = ProbeFactory.getProbe(mySimpleClass);
		probe.setObject(mySimpleClass, "myInt", Integer.valueOf(1));
		assertEquals(1, mySimpleClass.getMyInt());
		probe.setObject(mySimpleClass, "myInt", Integer.valueOf(2));
		assertEquals(2, mySimpleClass.getMyInt());
		try {
			probe.setObject(mySimpleClass, "myInt", null);
			fail();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("'myInt' to value 'null'"));
		}
		try {
			probe.setObject(mySimpleClass, "myInt", Float.valueOf(1.2f));
			fail();
		} catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("'myInt' to value '1.2'"));
		}
	}

	public class SimpleClass {

		int	myInt;

		public int getMyInt() {
			return myInt;
		}

		public void setMyInt(int myInt) {
			this.myInt = myInt;
		}
	}

}
