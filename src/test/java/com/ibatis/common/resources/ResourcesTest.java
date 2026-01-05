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
/*
 * Created on Apr 18, 2004 To change the template for this generated file go to Window - Preferences - Java - Code
 * Generation - Code and Comments
 */
package com.ibatis.common.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and
 * Comments
 */
class ResourcesTest {
  private boolean isUsingPrivateClassloader = false;

  @Test
  void testSetDefaultClassLoader() {
    final ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());
    final ClassLoader tmp = Resources.getDefaultClassLoader();
    Resources.setDefaultClassLoader(classLoader);
    Assertions.assertEquals(classLoader, Resources.getDefaultClassLoader());
    Resources.setDefaultClassLoader(tmp);
    Assertions.assertNotSame(classLoader, Resources.getDefaultClassLoader());
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
      Assertions.assertNotNull(url);
      url = null;
    } catch (final IOException e) {
      Assertions.fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    url = null;
    try {
      Resources.getResourceURL(resourceName);
      Assertions.fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (final IOException e) {
      // This is expected...
      Assertions.assertNull(url);
    }
  }

  /*
   * Class to test for URL getResourceURL(ClassLoader, String)
   */
  @Test
  void testGetResourceURLClassLoaderString() {
    String resourceName;
    URL url;
    final ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());

    resourceName = "java/lang/String.class";
    url = null;
    this.isUsingPrivateClassloader = false;
    try {
      Assertions.assertFalse(this.isUsingPrivateClassloader);
      url = Resources.getResourceURL(classLoader, resourceName);
      Assertions.assertNotNull(url);
      Assertions.assertTrue(this.isUsingPrivateClassloader);
      url = null;
    } catch (final IOException e) {
      Assertions.fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    url = null;
    this.isUsingPrivateClassloader = false;
    try {
      Assertions.assertFalse(this.isUsingPrivateClassloader);
      Resources.getResourceURL(classLoader, resourceName);
      Assertions.fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (final IOException e) {
      // This is expected...
      Assertions.assertNull(url);
      Assertions.assertTrue(this.isUsingPrivateClassloader);
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
      Assertions.assertNotNull(inputStream);
    } catch (final IOException e) {
      Assertions.fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    inputStream = null;
    try {
      Resources.getResourceURL(resourceName);
      Assertions.fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (final IOException e) {
      // This is expected...
      Assertions.assertNull(inputStream);
    }
  }

  /*
   * Class to test for InputStream getResourceAsStream(ClassLoader, String)
   */
  @Test
  void testGetResourceAsStreamClassLoaderString() {
    InputStream inputStream;
    String resourceName;
    final ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());

    resourceName = "java/lang/String.class";
    inputStream = null;
    this.isUsingPrivateClassloader = false;
    try {
      Assertions.assertFalse(this.isUsingPrivateClassloader);
      inputStream = Resources.getResourceAsStream(classLoader, resourceName);
      Assertions.assertNotNull(inputStream);
      Assertions.assertTrue(this.isUsingPrivateClassloader);
    } catch (final IOException e) {
      Assertions.fail("Could not load resource " + resourceName);
    }

    resourceName = "java.lang.YouAreAMonkeyCoderIfYouHaveThisClass";
    inputStream = null;
    this.isUsingPrivateClassloader = false;
    try {
      Assertions.assertFalse(this.isUsingPrivateClassloader);
      Resources.getResourceURL(classLoader, resourceName);
      Assertions.fail("You are a monkey coder, and the Resources class loaded a bogus class.");
    } catch (final IOException e) {
      // This is expected...
      Assertions.assertNull(inputStream);
      Assertions.assertTrue(this.isUsingPrivateClassloader);
    }
  }

  /*
   * Class to test for Properties getResourceAsProperties(String)
   */
  @Test
  void testGetResourceAsPropertiesString() {
    String resourceName;
    final String testProp = "name";
    final String testPropValue = "value";
    final String testProp2 = "name2";
    final String testPropValue2 = "value2";
    Properties properties;

    resourceName = "com/ibatis/common/resources/resourcestest.properties";
    properties = null;
    try {
      properties = Resources.getResourceAsProperties(resourceName);
      Assertions.assertNotNull(properties);
      Assertions.assertEquals(properties.get(testProp), testPropValue);
      Assertions.assertEquals(properties.get(testProp2), testPropValue2);
    } catch (final IOException e) {
      Assertions.fail("Could not read test properties file: " + resourceName);
    }

    resourceName = "com/ibatis/common/resources/dont-create-this-file-or-the-test-will-fail.properties";
    properties = null;

    try {
      properties = Resources.getResourceAsProperties(resourceName);
      Assertions
          .fail("Are you TRYING to break this test? If not, Resources loaded a bad properties file: " + resourceName);
    } catch (final IOException e) {
      Assertions.assertNull(properties);
    }
  }

  /*
   * Class to test for Properties getResourceAsProperties(ClassLoader, String)
   */
  @Test
  void testGetResourceAsPropertiesClassLoaderString() {
    String resourceName;
    final String testProp = "name";
    final String testPropValue = "value";
    final String testProp2 = "name2";
    final String testPropValue2 = "value2";
    Properties properties;
    final ClassLoader classLoader = new TestCL(this.getClass().getClassLoader());

    resourceName = "com/ibatis/common/resources/resourcestest.properties";
    properties = null;
    this.isUsingPrivateClassloader = false;
    try {
      Assertions.assertNull(properties);
      Assertions.assertFalse(this.isUsingPrivateClassloader);
      properties = Resources.getResourceAsProperties(classLoader, resourceName);
      Assertions.assertNotNull(properties);
      Assertions.assertTrue(this.isUsingPrivateClassloader);
      Assertions.assertEquals(properties.get(testProp), testPropValue);
      Assertions.assertEquals(properties.get(testProp2), testPropValue2);
    } catch (final IOException e) {
      Assertions.fail("Could not read test properties file: " + resourceName);
    }

    resourceName = "com/ibatis/common/resources/dont-create-this-file-or-the-test-will-fail.properties";
    properties = null;
    this.isUsingPrivateClassloader = false;
    try {
      Assertions.assertFalse(this.isUsingPrivateClassloader);
      properties = Resources.getResourceAsProperties(classLoader, resourceName);
      Assertions
          .fail("Are you TRYING to break this test? If not, Resources loaded a bad properties file: " + resourceName);
    } catch (final IOException e) {
      Assertions.assertNull(properties);
      Assertions.assertTrue(this.isUsingPrivateClassloader);
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

    public TestCL(final ClassLoader parent) {
      super(parent);
    }

    @Override
    public synchronized void clearAssertionStatus() {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      super.clearAssertionStatus();
    }

    @Override
    protected Package definePackage(final String name, final String specTitle, final String specVersion,
        final String specVendor, final String implTitle, final String implVersion, final String implVendor,
        final URL sealBase) throws IllegalArgumentException {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor,
          sealBase);
    }

    @Override
    protected Class findClass(final String name) throws ClassNotFoundException {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.findClass(name);
    }

    @Override
    protected String findLibrary(final String libname) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.findLibrary(libname);
    }

    @Override
    protected URL findResource(final String name) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.findResource(name);
    }

    @Override
    protected Enumeration findResources(final String name) throws IOException {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.findResources(name);
    }

    @Override
    protected Package getPackage(final String name) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.getPackage(name);
    }

    @Override
    protected Package[] getPackages() {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.getPackages();
    }

    @Override
    public URL getResource(final String name) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.getResource(name);
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.getResourceAsStream(name);
    }

    @Override
    protected synchronized Class loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.loadClass(name, resolve);
    }

    @Override
    public Class loadClass(final String name) throws ClassNotFoundException {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      return super.loadClass(name);
    }

    @Override
    public synchronized void setClassAssertionStatus(final String className, final boolean enabled) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      super.setClassAssertionStatus(className, enabled);
    }

    @Override
    public synchronized void setDefaultAssertionStatus(final boolean enabled) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      super.setDefaultAssertionStatus(enabled);
    }

    @Override
    public synchronized void setPackageAssertionStatus(final String packageName, final boolean enabled) {
      ResourcesTest.this.isUsingPrivateClassloader = true;
      super.setPackageAssertionStatus(packageName, enabled);
    }
  }
}
