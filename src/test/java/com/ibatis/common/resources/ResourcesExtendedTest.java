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
package com.ibatis.common.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Extended tests for the {@link Resources} utility class, covering paths not exercised by the original
 * {@link ResourcesTest}.
 */
class ResourcesExtendedTest {

  private static final String EXISTING_RESOURCE = "com/ibatis/common/resources/resourcestest.properties";
  private static final ClassLoader CL = ResourcesExtendedTest.class.getClassLoader();

  // --- getResourceAsStream (ClassLoader overload) --------------------------

  @Test
  void shouldLoadStreamWithExplicitClassLoader() throws IOException {
    InputStream in = Resources.getResourceAsStream(CL, EXISTING_RESOURCE);
    Assertions.assertNotNull(in);
    in.close();
  }

  @Test
  void shouldThrowForMissingStreamWithExplicitClassLoader() {
    Assertions.assertThrows(IOException.class, () -> Resources.getResourceAsStream(CL, "nonexistent.properties"));
  }

  // --- getResourceAsProperties (ClassLoader overload) ----------------------

  @Test
  void shouldLoadPropertiesViaStream() throws IOException {
    Properties props = Resources.getResourceAsProperties(EXISTING_RESOURCE);
    Assertions.assertNotNull(props);
  }

  @Test
  void shouldLoadPropertiesWithExplicitClassLoader() throws IOException {
    Properties props = Resources.getResourceAsProperties(CL, EXISTING_RESOURCE);
    Assertions.assertNotNull(props);
  }

  // --- getResourceAsReader (both overloads) --------------------------------

  @Test
  void shouldLoadReaderForResource() throws IOException {
    Reader reader = Resources.getResourceAsReader(EXISTING_RESOURCE);
    Assertions.assertNotNull(reader);
    reader.close();
  }

  @Test
  void shouldLoadReaderWithExplicitClassLoader() throws IOException {
    Reader reader = Resources.getResourceAsReader(CL, EXISTING_RESOURCE);
    Assertions.assertNotNull(reader);
    reader.close();
  }

  @Test
  void shouldLoadReaderWithCharset() throws IOException {
    Charset original = Resources.getCharset();
    try {
      Resources.setCharset(StandardCharsets.UTF_8);
      Reader reader = Resources.getResourceAsReader(EXISTING_RESOURCE);
      Assertions.assertNotNull(reader);
      reader.close();

      reader = Resources.getResourceAsReader(CL, EXISTING_RESOURCE);
      Assertions.assertNotNull(reader);
      reader.close();
    } finally {
      Resources.setCharset(original);
    }
  }

  // --- charset getter / setter ---------------------------------------------

  @Test
  void shouldSetAndGetCharset() {
    Charset original = Resources.getCharset();
    try {
      Resources.setCharset(StandardCharsets.ISO_8859_1);
      Assertions.assertEquals(StandardCharsets.ISO_8859_1, Resources.getCharset());
    } finally {
      Resources.setCharset(original);
    }
  }

  // --- classForName --------------------------------------------------------

  @Test
  void shouldLoadKnownClass() throws ClassNotFoundException {
    Class<?> clazz = Resources.classForName("java.lang.String");
    Assertions.assertEquals(String.class, clazz);
  }

  @Test
  void shouldThrowForUnknownClass() {
    Assertions.assertThrows(ClassNotFoundException.class, () -> Resources.classForName("com.totally.NoSuchClass"));
  }

  // --- instantiate ---------------------------------------------------------

  @Test
  void shouldInstantiateByClassName() throws Exception {
    Object obj = Resources.instantiate("java.util.ArrayList");
    Assertions.assertNotNull(obj);
  }

  @Test
  void shouldInstantiateByClass() throws Exception {
    Object obj = Resources.instantiate(java.util.HashMap.class);
    Assertions.assertNotNull(obj);
  }

  // --- getResourceURL (ClassLoader overload) --------------------------------

  @Test
  void shouldGetResourceURLWithExplicitClassLoader() throws IOException {
    java.net.URL url = Resources.getResourceURL(CL, EXISTING_RESOURCE);
    Assertions.assertNotNull(url);
  }

  @Test
  void shouldThrowForMissingURLWithExplicitClassLoader() {
    Assertions.assertThrows(IOException.class, () -> Resources.getResourceURL(CL, "no/such/resource.xml"));
  }
}
