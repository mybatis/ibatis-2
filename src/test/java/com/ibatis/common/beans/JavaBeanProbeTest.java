package com.ibatis.common.beans;

import junit.framework.TestCase;

public class JavaBeanProbeTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(JavaBeanProbeTest.class);
  }

  public void testGetPropertyTypeForSetterObjectString() {
    TestBean b = getBean();
    Probe p = getProbe(b);
    assertTrue(p.getPropertyTypeForSetter(b, "testBean").equals(TestBean.class));
    assertTrue(p.getPropertyTypeForSetter(b, "testBean.testBean").equals(TestBean.class));
  }

  public void testGetPropertyTypeForGetterObjectString() {
    TestBean b = getBean();
    Probe p = getProbe(b);
    assertTrue(p.getPropertyTypeForGetter(b, "testBean").equals(TestBean.class));
    assertTrue(p.getPropertyTypeForGetter(b, "testBean.testBean").equals(TestBean.class));
  }

  public void testHasWritableProperty() {
    TestBean b = getBean();
    Probe p = getProbe(b);
    assertTrue(p.hasWritableProperty(b, "testBean"));
    assertTrue(p.hasWritableProperty(b, "testBean.testBean"));
  }

  public void testHasReadableProperty() {
    TestBean b = getBean();
    Probe p = getProbe(b);
    assertTrue(p.hasReadableProperty(b, "testBean"));
    assertTrue(p.hasReadableProperty(b, "testBean.testBean"));
  }

  public void testSetAndGetObject() {
    TestBean b = getBean();
    Probe p = getProbe(b);
    float f[] = new float[3];
    f[0] = 1.0f;
    f[0] = 2.1f;
    f[0] = 3.2f;
    p.setObject(b, "floatArray", f);
    float g[] = (float[]) p.getObject(b, "floatArray");
    assertTrue(p.getObject(b, "floatArray").equals(f));
    assertEquals(g[0], f[0], .01);
    assertEquals(g[1], f[1], .01);
    assertEquals(g[2], f[2], .01);
    assertEquals(((Float) p.getObject(b, "floatArray[1]")).floatValue(), g[1], .01);
    p.setObject(b, "testBean.testBean", null);
    assertNull(p.getObject(b, "testBean.testBean"));
    p.setObject(b, "testBean.testBean.testBean", null);
    assertNull(p.getObject(b, "testBean.testBean.testBean"));
  }

  private Probe getProbe(Object o) {
    return ProbeFactory.getProbe(o);
  }

  private TestBean getBean() {
    TestBean returnValue = new TestBean();
    return returnValue;
  }

  public class TestBean {
    private float[] floatArray;
    private long[] longArray;
    private short[] shortArray;
    private TestBean testBean;

    public TestBean getTestBean() {
      return testBean;
    }

    public void setTestBean(TestBean testBean) {
      this.testBean = testBean;
    }

    public float[] getFloatArray() {
      return floatArray;
    }

    public void setFloatArray(float[] floatArray) {
      this.floatArray = floatArray;
    }

    public long[] getLongArray() {
      return longArray;
    }

    public void setLongArray(long[] longArray) {
      this.longArray = longArray;
    }

    public short[] getShortArray() {
      return shortArray;
    }

    public void setShortArray(short[] shortArray) {
      this.shortArray = shortArray;
    }
  }

}
