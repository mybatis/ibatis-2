/*
 * Copyright 2004-2023 the original author or authors.
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
package com.ibatis.sqlmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Category;
import testdomain.Product;

class RepeatingGroupMappingTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/jpetstore-hsqldb-schema.sql");
    initScript("scripts/jpetstore-hsqldb-dataload.sql");
  }

  @Test
  void testGroupBy() throws Exception {
    List<?> list = sqlMap.queryForList("getAllCategories", null);
    assertEquals(5, list.size());
  }

  @Test
  void testGroupByExtended() throws Exception {
    List<?> list = sqlMap.queryForList("getAllCategoriesExtended", null);
    assertEquals(5, list.size());
  }

  @Test
  void testNestedProperties() throws Exception {
    List<?> list = sqlMap.queryForList("getFish", null);
    assertEquals(1, list.size());

    Category cat = (Category) list.get(0);
    assertEquals("FISH", cat.getCategoryId());
    assertEquals("Fish", cat.getName());
    assertNotNull(cat.getProductList(), "Expected product list.");
    assertEquals(4, cat.getProductList().size());

    Product product = (Product) cat.getProductList().get(0);
    assertEquals(2, product.getItemList().size());
  }

  /**
   * This is a test case for iBATIS JIRA-250 "SELECT statement returns unexpected result when 'groupBy' and 'nullValue'
   * are specified in resultMaps."
   * <p>
   * The problem was that when a child object in a resultmap only contained null values it would still be created when
   * one of the properties of the child object contained a nullValue attribute in the ResultMap. The nullValue would be
   * applied before checking whether all properties of the child were 'null', so at least 1 property would always be
   * non-null (the reason for the nullValue attribute).
   * <p>
   * The fix is to first check whether all properties of the child are 'null', and if the child object contains at least
   * 1 non-null property to then only create the child object and apply the nullValue attribute.
   *
   * @throws Exception
   *           none should be thrown.
   */
  @Test
  void testGroupByJIRA250() throws Exception {
    List<?> list = sqlMap.queryForList("getAllProductCategoriesJIRA250", null);
    Category cat = (Category) list.get(0);
    assertEquals(0, cat.getProductList().size());
  }
}
