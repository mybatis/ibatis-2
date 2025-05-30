/*
 * Copyright 2004-2025 the original author or authors.
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
/*
 * Created on Apr 18, 2004 To change the template for this generated file go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
package com.ibatis.common.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.Test;

/**
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
class ResourcesTest {
  private boolean isUsingPrivateClassloader = false;

  @Test
  void testSetDefaultClassLoader() {
    ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());
    ClassLoader tmp = Resources.getDefaultClassLoader();
    Resources.setDefaultClassLoader(classLoader);
    assertEquals(classLoader, Resources.getDefaultClassLoader());
    Resources.setDefaultClassLoader(tmp);
    assertNotSame(classLoader, Resources.getDefaultClassLoader());
  }

  /*
   * Class to test for URL getResourceURL(String)
   */
  @Test
  void testGetResourceURLString() {
    String resourceName;
    URL url;

    resourceName = "java/lang/String.class";
    url = null;
    try {
      url = Resources.getResourceURL(resourceName);
      assertNotNull(url);
      url = null;
    } catch (IOException e) {
      fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    url = null;
    try {
      Resources.getResourceURL(resourceName);
      fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (IOException e) {
      // This is expected...
      assertNull(url);
    }
  }

  /*
   * Class to test for URL getResourceURL(ClassLoader, String)
   */
  @Test
  void testGetResourceURLClassLoaderString() {
    String resourceName;
    URL url;
    ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());

    resourceName = "java/lang/String.class";
    url = null;
    isUsingPrivateClassloader = false;
    try {
      assertFalse(isUsingPrivateClassloader);
      url = Resources.getResourceURL(classLoader, resourceName);
      assertNotNull(url);
      assertTrue(isUsingPrivateClassloader);
      url = null;
    } catch (IOException e) {
      fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    url = null;
    isUsingPrivateClassloader = false;
    try {
      assertFalse(isUsingPrivateClassloader);
      Resources.getResourceURL(classLoader, resourceName);
      fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (IOException e) {
      // This is expected...
      assertNull(url);
      assertTrue(isUsingPrivateClassloader);
    }
  }

  /*
   * Class to test for InputStream getResourceAsStream(String)
   */
  @Test
  void testGetResourceAsStreamString() {
    InputStream inputStream;
    String resourceName = "java/lang/String.class";

    inputStream = null;
    try {
      inputStream = Resources.getResourceAsStream(resourceName);
      assertNotNull(inputStream);
    } catch (IOException e) {
      fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    inputStream = null;
    try {
      Resources.getResourceURL(resourceName);
      fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (IOException e) {
      // This is expected...
      assertNull(inputStream);
    }
  }

  /*
   * Class to test for InputStream getResourceAsStream(ClassLoader, String)
   */
  @Test
  void testGetResourceAsStreamClassLoaderString() {
    InputStream inputStream;
    String resourceName;
    ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());

    resourceName = "java/lang/String.class";
    inputStream = null;
    isUsingPrivateClassloader = false;
    try {
      assertFalse(isUsingPrivateClassloader);
      inputStream = Resources.getResourceAsStream(classLoader, resourceName);
      assertNotNull(inputStream);
      assertTrue(isUsingPrivateClassloader);
    } catch (IOException e) {
      fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    inputStream = null;
    isUsingPrivateClassloader = false;
    try {
      assertFalse(isUsingPrivateClassloader);
      Resources.getResourceURL(classLoader, resourceName);
      fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (IOException e) {
      // This is expected...
      assertNull(inputStream);
      assertTrue(isUsingPrivateClassloader);
    }
  }

  /*
   * Class to test for Properties getResourceAsProperties(String)
   */
  @Test
  void testGetResourceAsPropertiesString() {
    String resourceName;
    String testProp = "name";
    String testPropValue = "value";
    String testProp2 = "name2";
    String testPropValue2 = "value2";
    Properties properties;

    resourceName = "com/ibatis/common/resources/resourcestest.properties";
    properties = null;
    try {
      properties = Resources.getResourceAsProperties(resourceName);
      assertNotNull(properties);
      assertEquals(properties.get(testProp), testPropValue);
      assertEquals(properties.get(testProp2), testPropValue2);
    } catch (IOException e) {
      fail("Could not read test properties file: " + resourceName);
    }

    resourceName = "com/ibatis/common/resources/dont-create-this-file-or-the-test-will-fail.properties";
    properties = null;

    try {
      properties = Resources.getResourceAsProperties(resourceName);
      fail("Are you TRYING to break this test? If not, Resources loaded a bad properties file: " + resourceName);
    } catch (IOException e) {
      assertNull(properties);
    }
  }

  /*
   * Class to test for Properties getResourceAsProperties(ClassLoader, String)
   */
  @Test
  void testGetResourceAsPropertiesClassLoaderString() {
    String resourceName;
    String testProp = "name";
    String testPropValue = "value";
    String testProp2 = "name2";
    String testPropValue2 = "value2";
    Properties properties;
    ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());

    resourceName = "com/ibatis/common/resources/resourcestest.properties";
    properties = null;
    isUsingPrivateClassloader = false;
    try {
      assertNull(properties);
      assertFalse(isUsingPrivateClassloader);
      properties = Resources.getResourceAsProperties(classLoader, resourceName);
      assertNotNull(properties);
      assertTrue(isUsingPrivateClassloader);
      assertEquals(properties.get(testProp), testPropValue);
      assertEquals(properties.get(testProp2), testPropValue2);
    } catch (IOException e) {
      fail("Could not read test properties file: " + resourceName);
    }

    resourceName = "com/ibatis/common/resources/dont-create-this-file-or-the-test-will-fail.properties";
    properties = null;
    isUsingPrivateClassloader = false;
    try {
      assertFalse(isUsingPrivateClassloader);
      properties = Resources.getResourceAsProperties(classLoader, resourceName);
      fail("Are you TRYING to break this test? If not, Resources loaded a bad properties file: " + resourceName);
    } catch (IOException e) {
      assertNull(properties);
      assertTrue(isUsingPrivateClassloader);
    }
  }

  /*
   * Class to test for Reader getResourceAsReader(String)
   */
  @Test
  void testGetResourceAsReaderString() {
  }

  /*
   * Class to test for Reader getResourceAsReader(ClassLoader, String)
   */
  @Test
  void testGetResourceAsReaderClassLoaderString() {
  }

  /*
   * Class to test for File getResourceAsFile(String)
   */
  @Test
  void testGetResourceAsFileString() {
  }

  /*
   * Class to test for File getResourceAsFile(ClassLoader, String)
   */
  @Test
  void testGetResourceAsFileClassLoaderString() {
  }

  @Test
  void testGetUrlAsStream() {
  }

  @Test
  void testGetUrlAsReader() {
  }

  @Test
  void testGetUrlAsProperties() {
  }

  @Test
  void testClassForName() {
  }

  /*
   * Class to test for Object instantiate(String)
   */
  @Test
  void testInstantiateString() {
  }

  /*
   * Class to test for Object instantiate(Class)
   */
  @Test
  void testInstantiateClass() {
  }

  /*
   * A stupid simple classloader for testing
   */
  private class TestCL extends ClassLoader {

    public TestCL(ClassLoader parent) {
      super(parent);
    }

    @Override
    public synchronized void clearAssertionStatus() {
      isUsingPrivateClassloader = true;
      super.clearAssertionStatus();
    }

    @Override
    protected Package definePackage(String name, String specTitle, String specVersion, String specVendor,
        String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
      isUsingPrivateClassloader = true;
      return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor,
          sealBase);
    }

    @Override
    protected Class findClass(String name) throws ClassNotFoundException {
      isUsingPrivateClassloader = true;
      return super.findClass(name);
    }

    @Override
    protected String findLibrary(String libname) {
      isUsingPrivateClassloader = true;
      return super.findLibrary(libname);
    }

    @Override
    protected URL findResource(String name) {
      isUsingPrivateClassloader = true;
      return super.findResource(name);
    }

    @Override
    protected Enumeration findResources(String name) throws IOException {
      isUsingPrivateClassloader = true;
      return super.findResources(name);
    }

    @Override
    protected Package getPackage(String name) {
      isUsingPrivateClassloader = true;
      return super.getPackage(name);
    }

    @Override
    protected Package[] getPackages() {
      isUsingPrivateClassloader = true;
      return super.getPackages();
    }

    @Override
    public URL getResource(String name) {
      isUsingPrivateClassloader = true;
      return super.getResource(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
      isUsingPrivateClassloader = true;
      return super.getResourceAsStream(name);
    }

    @Override
    protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
      isUsingPrivateClassloader = true;
      return super.loadClass(name, resolve);
    }

    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
      isUsingPrivateClassloader = true;
      return super.loadClass(name);
    }

    @Override
    public synchronized void setClassAssertionStatus(String className, boolean enabled) {
      isUsingPrivateClassloader = true;
      super.setClassAssertionStatus(className, enabled);
    }

    @Override
    public synchronized void setDefaultAssertionStatus(boolean enabled) {
      isUsingPrivateClassloader = true;
      super.setDefaultAssertionStatus(enabled);
    }

    @Override
    public synchronized void setPackageAssertionStatus(String packageName, boolean enabled) {
      isUsingPrivateClassloader = true;
      super.setPackageAssertionStatus(packageName, enabled);
    }
  }
}
