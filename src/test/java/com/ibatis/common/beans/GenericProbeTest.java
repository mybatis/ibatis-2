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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests {@link GenericProbe} dispatch routing to DOM, indexed-collection, and JavaBean paths.
 */
class GenericProbeTest {

  private Probe probe;
  private BaseProbe baseProbe;

  @BeforeEach
  void setUp() {
    probe = ProbeFactory.getProbe(); // returns GenericProbe (no-arg overload)
    baseProbe = (BaseProbe) probe;
  }

  // --- DOM dispatch --------------------------------------------------------

  @Test
  void shouldDispatchGetObjectToDomProbe() throws Exception {
    Document doc = buildDoc("greeting", "Hello");
    Object value = probe.getObject(doc, "greeting");
    Assertions.assertEquals("Hello", value);
  }

  @Test
  void shouldDispatchSetObjectToDomProbe() throws Exception {
    Document doc = buildDoc("greeting", "Hello");
    probe.setObject(doc, "greeting", "Hi");
    Assertions.assertEquals("Hi", probe.getObject(doc, "greeting"));
  }

  @Test
  void shouldDispatchGetReadablePropertyNamesToDomProbe() throws Exception {
    Document doc = buildDoc("greeting", "Hello");
    String[] names = baseProbe.getReadablePropertyNames(doc);
    Assertions.assertTrue(List.of(names).contains("greeting"));
  }

  @Test
  void shouldDispatchGetWriteablePropertyNamesToDomProbe() throws Exception {
    Document doc = buildDoc("greeting", "Hello");
    String[] names = baseProbe.getWriteablePropertyNames(doc);
    Assertions.assertTrue(List.of(names).contains("greeting"));
  }

  @Test
  void shouldDispatchHasReadablePropertyToDomProbeTrue() throws Exception {
    Document doc = buildDoc("key", "val");
    Assertions.assertTrue(probe.hasReadableProperty(doc, "key"));
  }

  @Test
  void shouldDispatchHasReadablePropertyToDomProbeFalse() throws Exception {
    Document doc = buildDoc("key", "val");
    Assertions.assertFalse(probe.hasReadableProperty(doc, "absent"));
  }

  @Test
  void shouldDispatchHasWritablePropertyToDomProbeTrue() throws Exception {
    Document doc = buildDoc("key", "val");
    Assertions.assertTrue(probe.hasWritableProperty(doc, "key"));
  }

  @Test
  void shouldDispatchGetPropertyTypeForGetterToDomProbe() throws Exception {
    Document doc = buildDoc("key", "val");
    Class<?> type = probe.getPropertyTypeForGetter(doc, "key");
    Assertions.assertEquals(Object.class, type);
  }

  @Test
  void shouldDispatchGetPropertyTypeForSetterToDomProbe() throws Exception {
    Document doc = buildDoc("key", "val");
    Class<?> type = probe.getPropertyTypeForSetter(doc, "key");
    Assertions.assertEquals(Object.class, type);
  }

  // --- indexed dispatch (List and arrays) ----------------------------------

  @Test
  void shouldDispatchGetObjectToIndexedProperty() {
    List<String> list = new ArrayList<>(List.of("alpha", "beta"));
    Object value = probe.getObject(list, "[0]");
    Assertions.assertEquals("alpha", value);
  }

  @Test
  void shouldDispatchGetObjectForIntArray() {
    int[] arr = { 10, 20, 30 };
    Object value = probe.getObject(arr, "[2]");
    Assertions.assertEquals(Integer.valueOf(30), value);
  }

  @Test
  void shouldDispatchGetObjectForObjectArray() {
    Object[] arr = { "x", "y" };
    Object value = probe.getObject(arr, "[1]");
    Assertions.assertEquals("y", value);
  }

  @Test
  void shouldDispatchGetObjectForCharArray() {
    char[] arr = { 'a', 'b' };
    Object value = probe.getObject(arr, "[0]");
    Assertions.assertEquals(Character.valueOf('a'), value);
  }

  @Test
  void shouldDispatchGetObjectForBooleanArray() {
    boolean[] arr = { true, false };
    Object value = probe.getObject(arr, "[1]");
    Assertions.assertEquals(Boolean.FALSE, value);
  }

  @Test
  void shouldDispatchGetObjectForByteArray() {
    byte[] arr = { 1, 2 };
    Object value = probe.getObject(arr, "[0]");
    Assertions.assertEquals(Byte.valueOf((byte) 1), value);
  }

  @Test
  void shouldDispatchGetObjectForDoubleArray() {
    double[] arr = { 1.1, 2.2 };
    Object value = probe.getObject(arr, "[1]");
    Assertions.assertEquals(Double.valueOf(2.2), value);
  }

  @Test
  void shouldDispatchGetObjectForFloatArray() {
    float[] arr = { 0.1f, 0.2f };
    Object value = probe.getObject(arr, "[0]");
    Assertions.assertEquals(Float.valueOf(0.1f), value);
  }

  @Test
  void shouldDispatchGetObjectForLongArray() {
    long[] arr = { 100L, 200L };
    Object value = probe.getObject(arr, "[1]");
    Assertions.assertEquals(Long.valueOf(200L), value);
  }

  @Test
  void shouldDispatchGetObjectForShortArray() {
    short[] arr = { 3, 4 };
    Object value = probe.getObject(arr, "[0]");
    Assertions.assertEquals(Short.valueOf((short) 3), value);
  }

  // --- JavaBean dispatch ---------------------------------------------------

  @Test
  void shouldDispatchGetObjectToJavaBeanProbe() {
    MyBean bean = new MyBean();
    bean.name = "Alice";
    Object value = probe.getObject(bean, "name");
    Assertions.assertEquals("Alice", value);
  }

  @Test
  void shouldDispatchSetObjectToJavaBeanProbe() {
    MyBean bean = new MyBean();
    probe.setObject(bean, "name", "Bob");
    Assertions.assertEquals("Bob", bean.name);
  }

  @Test
  void shouldDispatchGetReadablePropertyNamesToJavaBeanProbe() {
    MyBean bean = new MyBean();
    String[] names = baseProbe.getReadablePropertyNames(bean);
    Assertions.assertTrue(List.of(names).contains("name"));
  }

  @Test
  void shouldDispatchGetWriteablePropertyNamesToJavaBeanProbe() {
    MyBean bean = new MyBean();
    String[] names = baseProbe.getWriteablePropertyNames(bean);
    Assertions.assertTrue(List.of(names).contains("name"));
  }

  @Test
  void shouldDispatchHasReadablePropertyToJavaBeanProbeTrue() {
    MyBean bean = new MyBean();
    Assertions.assertTrue(probe.hasReadableProperty(bean, "name"));
  }

  @Test
  void shouldDispatchHasReadablePropertyToJavaBeanProbeFalse() {
    MyBean bean = new MyBean();
    Assertions.assertFalse(probe.hasReadableProperty(bean, "absent"));
  }

  @Test
  void shouldDispatchHasWritablePropertyToJavaBeanProbeTrue() {
    MyBean bean = new MyBean();
    Assertions.assertTrue(probe.hasWritableProperty(bean, "name"));
  }

  @Test
  void shouldDispatchGetPropertyTypeForGetterToJavaBeanProbe() {
    MyBean bean = new MyBean();
    Class<?> type = probe.getPropertyTypeForGetter(bean, "name");
    Assertions.assertEquals(String.class, type);
  }

  @Test
  void shouldDispatchGetPropertyTypeForSetterToJavaBeanProbe() {
    MyBean bean = new MyBean();
    Class<?> type = probe.getPropertyTypeForSetter(bean, "name");
    Assertions.assertEquals(String.class, type);
  }

  // --- Map dispatch --------------------------------------------------------

  @Test
  void shouldGetFromMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("key", "value");
    Object value = probe.getObject(map, "key");
    Assertions.assertEquals("value", value);
  }

  @Test
  void shouldSetInMap() {
    Map<String, Object> map = new HashMap<>();
    probe.setObject(map, "key", "val");
    Assertions.assertEquals("val", map.get("key"));
  }

  // ---- helper classes / methods -------------------------------------------

  private static Document buildDoc(String elementName, String textValue) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.newDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);
    Element child = doc.createElement(elementName);
    child.appendChild(doc.createTextNode(textValue));
    root.appendChild(child);
    return doc;
  }

  public static class MyBean {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
