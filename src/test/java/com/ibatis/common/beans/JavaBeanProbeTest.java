/*
 * Copyright 2004-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibatis.common.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JavaBeanProbeTest {

  @Test
  void testGetPropertyTypeForSetterObjectString() {
    final TestBean b = this.getBean();
    final Probe p = this.getProbe(b);
    Assertions.assertTrue(p.getPropertyTypeForSetter(b, "testBean").equals(TestBean.class));
    Assertions.assertTrue(p.getPropertyTypeForSetter(b, "testBean.testBean").equals(TestBean.class));
  }

  @Test
  void testGetPropertyTypeForGetterObjectString() {
    final TestBean b = this.getBean();
    final Probe p = this.getProbe(b);
    Assertions.assertTrue(p.getPropertyTypeForGetter(b, "testBean").equals(TestBean.class));
    Assertions.assertTrue(p.getPropertyTypeForGetter(b, "testBean.testBean").equals(TestBean.class));
  }

  @Test
  void testHasWritableProperty() {
    final TestBean b = this.getBean();
    final Probe p = this.getProbe(b);
    Assertions.assertTrue(p.hasWritableProperty(b, "testBean"));
    Assertions.assertTrue(p.hasWritableProperty(b, "testBean.testBean"));
  }

  @Test
  void testHasReadableProperty() {
    final TestBean b = this.getBean();
    final Probe p = this.getProbe(b);
    Assertions.assertTrue(p.hasReadableProperty(b, "testBean"));
    Assertions.assertTrue(p.hasReadableProperty(b, "testBean.testBean"));
  }

  @Test
  void testSetAndGetObject() {
    final TestBean b = this.getBean();
    final Probe p = this.getProbe(b);
    final float f[] = new float[3];
    f[0] = 1.0f;
    f[0] = 2.1f;
    f[0] = 3.2f;
    p.setObject(b, "floatArray", f);
    final float g[] = (float[]) p.getObject(b, "floatArray");
    Assertions.assertTrue(p.getObject(b, "floatArray").equals(f));
    Assertions.assertEquals(g[0], f[0], .01);
    Assertions.assertEquals(g[1], f[1], .01);
    Assertions.assertEquals(g[2], f[2], .01);
    Assertions.assertEquals(((Float) p.getObject(b, "floatArray[1]")).floatValue(), g[1], .01);
    p.setObject(b, "testBean.testBean", null);
    Assertions.assertNull(p.getObject(b, "testBean.testBean"));
    p.setObject(b, "testBean.testBean.testBean", null);
    Assertions.assertNull(p.getObject(b, "testBean.testBean.testBean"));
  }

  private Probe getProbe(final Object o) {
    return ProbeFactory.getProbe(o);
  }

  private TestBean getBean() {
    return new TestBean();
  }

  public class TestBean {
    private float[] floatArray;
    private long[] longArray;
    private short[] shortArray;
    private TestBean testBean;

    public TestBean getTestBean() {
      return this.testBean;
    }

    public void setTestBean(final TestBean testBean) {
      this.testBean = testBean;
    }

    public float[] getFloatArray() {
      return this.floatArray;
    }

    public void setFloatArray(final float[] floatArray) {
      this.floatArray = floatArray;
    }

    public long[] getLongArray() {
      return this.longArray;
    }

    public void setLongArray(final long[] longArray) {
      this.longArray = longArray;
    }

    public short[] getShortArray() {
      return this.shortArray;
    }

    public void setShortArray(final short[] shortArray) {
      this.shortArray = shortArray;
    }
  }

}
