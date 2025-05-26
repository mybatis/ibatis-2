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
package com.ibatis.sqlmap.builder.xml;

import com.ibatis.sqlmap.engine.builder.xml.SqlMapClasspathEntityResolver;

import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class SqlMapClasspathEntityResolverTest {

  // ibatis.apache.org

  @Test
  void testOrgConfigSystemId() {
    String id = "https://ibatis.apache.org/dtd/sql-map-config-2.dtd";
    assertSystemIdCanBeResolved(id);
  }

  @Test
  void testOrgConfigPublicId() {
    String id = "-//ibatis.apache.org//DTD SQL Map Config 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  @Test
  void testOrgMapSystemId() {
    String id = "https://ibatis.apache.org/dtd/sql-map-2.dtd";
    assertSystemIdCanBeResolved(id);
  }

  @Test
  void testOrgMapPublicId() {
    String id = "-//ibatis.apache.org//DTD SQL Map 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  @Test
  void testOddCase() {
    String id = "-//iBATIS.apache.org//DTD SQL Map 2.0//EN";
    assertPublicIdCanBeResolved(id);
  }

  private void assertSystemIdCanBeResolved(String id) {
    SqlMapClasspathEntityResolver resolver = new SqlMapClasspathEntityResolver();
    try {
      InputSource source = resolver.resolveEntity(null, id);
      if (source == null) {
        throw new RuntimeException("Could not resolve System ID  " + id);
      }
    } catch (SAXException e) {
      throw new RuntimeException("Error.  Cause: " + e, e);
    }
  }

  private void assertPublicIdCanBeResolved(String id) {
    SqlMapClasspathEntityResolver resolver = new SqlMapClasspathEntityResolver();
    try {
      InputSource source = resolver.resolveEntity(id, null);
      if (source == null) {
        throw new RuntimeException("Could not resolve System ID  " + id);
      }
    } catch (SAXException e) {
      throw new RuntimeException("Error.  Cause: " + e, e);
    }
  }

}
