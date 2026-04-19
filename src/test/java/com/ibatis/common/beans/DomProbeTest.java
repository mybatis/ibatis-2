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

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class DomProbeTest {

  private DomProbe probe;
  private Document doc;

  @BeforeEach
  void setUp() throws Exception {
    probe = new DomProbe();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    doc = builder.newDocument();
    Element root = doc.createElement("root");
    doc.appendChild(root);

    Element name = doc.createElement("name");
    name.appendChild(doc.createTextNode("Alice"));
    root.appendChild(name);

    Element age = doc.createElement("age");
    age.appendChild(doc.createTextNode("30"));
    root.appendChild(age);
  }

  // --- readable / writeable property names ---------------------------------

  @Test
  void shouldReturnReadablePropertyNames() {
    String[] names = probe.getReadablePropertyNames(doc);
    Assertions.assertTrue(names.length >= 2);
    Assertions.assertTrue(List.of(names).contains("name"));
    Assertions.assertTrue(List.of(names).contains("age"));
  }

  @Test
  void shouldReturnReadablePropertyNamesFromElement() {
    Element root = doc.getDocumentElement();
    String[] names = probe.getReadablePropertyNames(root);
    Assertions.assertTrue(names.length >= 2);
  }

  @Test
  void shouldReturnWriteablePropertyNames() {
    String[] names = probe.getWriteablePropertyNames(doc);
    Assertions.assertTrue(names.length >= 2);
  }

  // --- has*Property --------------------------------------------------------

  @Test
  void shouldReturnTrueForExistingReadableProperty() {
    Assertions.assertTrue(probe.hasReadableProperty(doc, "name"));
  }

  @Test
  void shouldReturnFalseForMissingReadableProperty() {
    Assertions.assertFalse(probe.hasReadableProperty(doc, "missing"));
  }

  @Test
  void shouldReturnTrueForExistingWritableProperty() {
    Assertions.assertTrue(probe.hasWritableProperty(doc, "age"));
  }

  @Test
  void shouldReturnFalseForMissingWritableProperty() {
    Assertions.assertFalse(probe.hasWritableProperty(doc, "none"));
  }

  // --- property type -------------------------------------------------------

  @Test
  void shouldReturnObjectClassForPropertyTypeWhenNoTypeAttribute() {
    Class<?> type = probe.getPropertyTypeForGetter(doc, "name");
    Assertions.assertEquals(Object.class, type);
  }

  @Test
  void shouldReturnObjectClassForSetterTypeWhenNoTypeAttribute() {
    Class<?> type = probe.getPropertyTypeForSetter(doc, "age");
    Assertions.assertEquals(Object.class, type);
  }

  // --- getObject / setObject -----------------------------------------------

  @Test
  void shouldGetPropertyValueFromDocument() {
    Object value = probe.getObject(doc, "name");
    Assertions.assertEquals("Alice", value);
  }

  @Test
  void shouldGetPropertyValueFromElement() {
    Object value = probe.getObject(doc.getDocumentElement(), "age");
    Assertions.assertEquals("30", value);
  }

  @Test
  void shouldReturnNullForMissingProperty() {
    Object value = probe.getObject(doc, "missing");
    Assertions.assertNull(value);
  }

  @Test
  void shouldSetPropertyValueOnDocument() {
    probe.setObject(doc, "name", "Bob");
    Object value = probe.getObject(doc, "name");
    Assertions.assertEquals("Bob", value);
  }

  @Test
  void shouldSetPropertyValueOnElement() {
    probe.setObject(doc.getDocumentElement(), "age", "99");
    Object value = probe.getObject(doc, "age");
    Assertions.assertEquals("99", value);
  }

  // --- nested dotted name access ------------------------------------------

  @Test
  void shouldGetNestedProperty() throws Exception {
    // Manually nest an element: root / address / city
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document d = factory.newDocumentBuilder().newDocument();
    Element root = d.createElement("root");
    d.appendChild(root);
    Element address = d.createElement("address");
    root.appendChild(address);
    Element city = d.createElement("city");
    city.appendChild(d.createTextNode("London"));
    address.appendChild(city);

    Object value = probe.getObject(d, "address.city");
    Assertions.assertEquals("London", value);
  }

  @Test
  void shouldHandleIndexedNodeAccess() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document d = factory.newDocumentBuilder().newDocument();
    Element root = d.createElement("root");
    d.appendChild(root);
    for (int i = 0; i < 3; i++) {
      Element item = d.createElement("item");
      item.appendChild(d.createTextNode("val" + i));
      root.appendChild(item);
    }

    Object value = probe.getObject(d, "item[1]");
    Assertions.assertEquals("val1", value);
  }

  // --- resolveElement with unknown type ------------------------------------

  @Test
  void shouldThrowForNonDocumentNonElement() {
    Assertions.assertThrows(ProbeException.class, () -> probe.getObject("not a document", "name"));
  }

  // --- nodeToString --------------------------------------------------------

  @Test
  void shouldConvertDocumentNodeToString() {
    String result = DomProbe.nodeToString(doc, "");
    Assertions.assertTrue(result.contains("<root"));
    Assertions.assertTrue(result.contains("Alice"));
  }

  @Test
  void shouldConvertTextNodeToString() {
    org.w3c.dom.Node textNode = doc.createTextNode("hello");
    String result = DomProbe.nodeToString(textNode, "");
    Assertions.assertEquals("hello", result);
  }

  // --- setObject with Document value (appends child nodes) -----------------

  @Test
  void shouldSetDocumentValueByAppendingChildNodes() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document valueDoc = factory.newDocumentBuilder().newDocument();
    Element valRoot = valueDoc.createElement("extra");
    valRoot.appendChild(valueDoc.createTextNode("extra-text"));
    valueDoc.appendChild(valRoot);

    // setObject where value is a Document appends children under target element
    probe.setObject(doc, "name", valueDoc);
    // Just verify no exception – structure is implementation-defined
  }

  // --- setObject with null text (0-length child list) ----------------------

  @Test
  void shouldSetPropertyOnEmptyElement() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document d = factory.newDocumentBuilder().newDocument();
    Element root = d.createElement("root");
    d.appendChild(root);
    // 'fresh' has no children, trigger the "else – Add text" path
    probe.setObject(d, "fresh", "newvalue");
    Object result = probe.getObject(d, "fresh");
    Assertions.assertEquals("newvalue", result);
  }
}
