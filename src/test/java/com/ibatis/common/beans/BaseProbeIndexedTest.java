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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Exercises the indexed-property paths in {@link BaseProbe} (via {@link GenericProbe}) for every array and List type.
 */
class BaseProbeIndexedTest {

  private final Probe probe = ProbeFactory.getProbe(new Object());

  // Helper bean that holds every supported array type
  public static class ArrayBean {
    public Object[] objects = new Object[] { "a", "b", "c" };
    public char[] chars = new char[] { 'x', 'y', 'z' };
    public boolean[] booleans = new boolean[] { true, false, true };
    public byte[] bytes = new byte[] { 1, 2, 3 };
    public double[] doubles = new double[] { 1.0, 2.0, 3.0 };
    public float[] floats = new float[] { 1.0f, 2.0f, 3.0f };
    public int[] ints = new int[] { 10, 20, 30 };
    public long[] longs = new long[] { 100L, 200L, 300L };
    public short[] shorts = new short[] { 4, 5, 6 };
    public List<String> strings = new ArrayList<>(List.of("one", "two", "three"));

    public Object[] getObjects() {
      return objects;
    }

    public char[] getChars() {
      return chars;
    }

    public boolean[] getBooleans() {
      return booleans;
    }

    public byte[] getBytes() {
      return bytes;
    }

    public double[] getDoubles() {
      return doubles;
    }

    public float[] getFloats() {
      return floats;
    }

    public int[] getInts() {
      return ints;
    }

    public long[] getLongs() {
      return longs;
    }

    public short[] getShorts() {
      return shorts;
    }

    public List<String> getStrings() {
      return strings;
    }
  }

  // --- getObject (getIndexedProperty) via GenericProbe ---------------------

  @Test
  void shouldGetFromObjectArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals("b", probe.getObject(bean, "objects[1]"));
  }

  @Test
  void shouldGetFromCharArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Character.valueOf('y'), probe.getObject(bean, "chars[1]"));
  }

  @Test
  void shouldGetFromBooleanArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Boolean.FALSE, probe.getObject(bean, "booleans[1]"));
  }

  @Test
  void shouldGetFromByteArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Byte.valueOf((byte) 2), probe.getObject(bean, "bytes[1]"));
  }

  @Test
  void shouldGetFromDoubleArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Double.valueOf(2.0), probe.getObject(bean, "doubles[1]"));
  }

  @Test
  void shouldGetFromFloatArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Float.valueOf(2.0f), probe.getObject(bean, "floats[1]"));
  }

  @Test
  void shouldGetFromIntArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Integer.valueOf(20), probe.getObject(bean, "ints[1]"));
  }

  @Test
  void shouldGetFromLongArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Long.valueOf(200L), probe.getObject(bean, "longs[1]"));
  }

  @Test
  void shouldGetFromShortArray() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals(Short.valueOf((short) 5), probe.getObject(bean, "shorts[1]"));
  }

  @Test
  void shouldGetFromList() {
    ArrayBean bean = new ArrayBean();
    Assertions.assertEquals("two", probe.getObject(bean, "strings[1]"));
  }

  // --- getObject with empty-name (direct List/array access) ----------------

  @Test
  void shouldGetFromRootList() {
    List<String> list = new ArrayList<>(List.of("alpha", "beta", "gamma"));
    Assertions.assertEquals("beta", probe.getObject(list, "[1]"));
  }

  @Test
  void shouldGetFromRootObjectArray() {
    Object[] arr = new Object[] { "p", "q", "r" };
    Assertions.assertEquals("q", probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootIntArray() {
    int[] arr = new int[] { 5, 6, 7 };
    Assertions.assertEquals(Integer.valueOf(6), probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootLongArray() {
    long[] arr = new long[] { 10L, 20L, 30L };
    Assertions.assertEquals(Long.valueOf(20L), probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootDoubleArray() {
    double[] arr = new double[] { 1.5, 2.5, 3.5 };
    Assertions.assertEquals(Double.valueOf(2.5), probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootFloatArray() {
    float[] arr = new float[] { 0.1f, 0.2f, 0.3f };
    Assertions.assertEquals(Float.valueOf(0.2f), probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootShortArray() {
    short[] arr = new short[] { 1, 2, 3 };
    Assertions.assertEquals(Short.valueOf((short) 2), probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootByteArray() {
    byte[] arr = new byte[] { 9, 8, 7 };
    Assertions.assertEquals(Byte.valueOf((byte) 8), probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootBooleanArray() {
    boolean[] arr = new boolean[] { false, true };
    Assertions.assertEquals(Boolean.TRUE, probe.getObject(arr, "[1]"));
  }

  @Test
  void shouldGetFromRootCharArray() {
    char[] arr = new char[] { 'a', 'b', 'c' };
    Assertions.assertEquals(Character.valueOf('b'), probe.getObject(arr, "[1]"));
  }

  // --- setObject (setIndexedProperty) -------------------------------------

  @Test
  void shouldSetOnObjectArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "objects[0]", "z");
    Assertions.assertEquals("z", bean.objects[0]);
  }

  @Test
  void shouldSetOnList() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "strings[0]", "updated");
    Assertions.assertEquals("updated", bean.strings.get(0));
  }

  @Test
  void shouldSetOnIntArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "ints[0]", Integer.valueOf(999));
    Assertions.assertEquals(999, bean.ints[0]);
  }

  @Test
  void shouldSetOnLongArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "longs[0]", Long.valueOf(9999L));
    Assertions.assertEquals(9999L, bean.longs[0]);
  }

  @Test
  void shouldSetOnDoubleArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "doubles[0]", Double.valueOf(3.14));
    Assertions.assertEquals(3.14, bean.doubles[0], 0.0001);
  }

  @Test
  void shouldSetOnFloatArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "floats[0]", Float.valueOf(2.71f));
    Assertions.assertEquals(2.71f, bean.floats[0], 0.0001f);
  }

  @Test
  void shouldSetOnShortArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "shorts[0]", Short.valueOf((short) 77));
    Assertions.assertEquals(77, bean.shorts[0]);
  }

  @Test
  void shouldSetOnByteArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "bytes[0]", Byte.valueOf((byte) 99));
    Assertions.assertEquals(99, bean.bytes[0]);
  }

  @Test
  void shouldSetOnBooleanArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "booleans[0]", Boolean.FALSE);
    Assertions.assertFalse(bean.booleans[0]);
  }

  @Test
  void shouldSetOnCharArray() {
    ArrayBean bean = new ArrayBean();
    probe.setObject(bean, "chars[0]", Character.valueOf('Z'));
    Assertions.assertEquals('Z', bean.chars[0]);
  }

  // --- error cases ---------------------------------------------------------

  @Test
  void shouldThrowForGetOnNonListNonArray() {
    Assertions.assertThrows(ProbeException.class, () -> probe.getObject(new ArrayBean(), "objects[99]")); // or a
                                                                                                          // non-collection
                                                                                                          // property
  }
}
