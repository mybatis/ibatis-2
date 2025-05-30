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
package com.ibatis.sqlmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Category;
import testdomain.Item;
import testdomain.NestedIterateParameterObject;
import testdomain.Person;
import testdomain.Product;
import testdomain.SimpleNestedParameterObject;

class NestedIterateTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/person-init.sql");
    initScript("scripts/jpetstore-hsqldb-schema.sql");
    initScript("scripts/jpetstore-hsqldb-dataload.sql");
  }

  /**
   * This test should return 9 rows: ids 1-9 This method works as expected
   */
  @Test
  void testShouldReturn9Rows() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest1", po);
      assertEquals(9, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(4, ((Person) results.get(3)).getId().intValue());
      assertEquals(5, ((Person) results.get(4)).getId().intValue());
      assertEquals(6, ((Person) results.get(5)).getId().intValue());
      assertEquals(7, ((Person) results.get(6)).getId().intValue());
      assertEquals(8, ((Person) results.get(7)).getId().intValue());
      assertEquals(9, ((Person) results.get(8)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test shoud return 1 row: id 4
   */
  @Test
  void test02() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest2", po);
      assertEquals(1, results.size());
      assertEquals(4, ((Person) results.get(0)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 8 rows: ids 1-3, 5-9
   */
  @Test
  void test03() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(4)); // put first to make the test fail
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest3", po);
      assertEquals(8, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(5, ((Person) results.get(3)).getId().intValue());
      assertEquals(6, ((Person) results.get(4)).getId().intValue());
      assertEquals(7, ((Person) results.get(5)).getId().intValue());
      assertEquals(8, ((Person) results.get(6)).getId().intValue());
      assertEquals(9, ((Person) results.get(7)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 5 rows: ids 5-9
   */
  @Test
  void test04() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest4", po);
      assertEquals(5, results.size());
      assertEquals(5, ((Person) results.get(0)).getId().intValue());
      assertEquals(6, ((Person) results.get(1)).getId().intValue());
      assertEquals(7, ((Person) results.get(2)).getId().intValue());
      assertEquals(8, ((Person) results.get(3)).getId().intValue());
      assertEquals(9, ((Person) results.get(4)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 6 rows: ids 4-9
   */
  @Test
  void test05() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest5", po);
      assertEquals(6, results.size());
      assertEquals(4, ((Person) results.get(0)).getId().intValue());
      assertEquals(5, ((Person) results.get(1)).getId().intValue());
      assertEquals(6, ((Person) results.get(2)).getId().intValue());
      assertEquals(7, ((Person) results.get(3)).getId().intValue());
      assertEquals(8, ((Person) results.get(4)).getId().intValue());
      assertEquals(9, ((Person) results.get(5)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 3 rows: ids 1-3
   */
  @Test
  void test06() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    // go backwards to make the test fail
    po.addId(Integer.valueOf(9));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(1));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest6", po);
      assertEquals(3, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 4 rows: ids 1-4
   */
  @Test
  void test07() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    // go backwards to make the test fail
    po.addId(Integer.valueOf(9));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(1));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest7", po);
      assertEquals(4, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(4, ((Person) results.get(3)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return four rows: ids 1, 2, 7, 8
   */
  @Test
  void test08() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addFirstName("Jeff");
    po.addFirstName("Matt");
    po.addLastName("Jones");
    po.addLastName("Smith");

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest8", po);
      assertEquals(4, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(7, ((Person) results.get(2)).getId().intValue());
      assertEquals(8, ((Person) results.get(3)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method works when Christian's IBATIS-281 patches are applied
   */
  @Test
  void test09() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest9", po);
      assertEquals(2, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void test09a() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest9a", po);
      assertEquals(2, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test shoud return 1 row: id 4
   */
  @Test
  void test10() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest10", po);
      assertEquals(1, results.size());
      assertEquals(4, ((Person) results.get(0)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 8 rows: ids 1-3, 5-9
   */
  @Test
  void test11() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(4)); // put first to make the test fail
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest11", po);
      assertEquals(8, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(5, ((Person) results.get(3)).getId().intValue());
      assertEquals(6, ((Person) results.get(4)).getId().intValue());
      assertEquals(7, ((Person) results.get(5)).getId().intValue());
      assertEquals(8, ((Person) results.get(6)).getId().intValue());
      assertEquals(9, ((Person) results.get(7)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 5 rows: ids 5-9
   */
  @Test
  void test12() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest12", po);
      assertEquals(5, results.size());
      assertEquals(5, ((Person) results.get(0)).getId().intValue());
      assertEquals(6, ((Person) results.get(1)).getId().intValue());
      assertEquals(7, ((Person) results.get(2)).getId().intValue());
      assertEquals(8, ((Person) results.get(3)).getId().intValue());
      assertEquals(9, ((Person) results.get(4)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 6 rows: ids 4-9
   */
  @Test
  void test13() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest13", po);
      assertEquals(6, results.size());
      assertEquals(4, ((Person) results.get(0)).getId().intValue());
      assertEquals(5, ((Person) results.get(1)).getId().intValue());
      assertEquals(6, ((Person) results.get(2)).getId().intValue());
      assertEquals(7, ((Person) results.get(3)).getId().intValue());
      assertEquals(8, ((Person) results.get(4)).getId().intValue());
      assertEquals(9, ((Person) results.get(5)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 3 rows: ids 1-3
   */
  @Test
  void test14() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    // go backwards to make the test fail
    po.addId(Integer.valueOf(9));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(1));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest14", po);
      assertEquals(3, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This test should return 4 rows: ids 1-4
   */
  @Test
  void test15() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    // go backwards to make the test fail
    po.addId(Integer.valueOf(9));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(1));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest15", po);
      assertEquals(4, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(4, ((Person) results.get(3)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return six rows: ids 1-6
   * <p>
   * This method works when Christian's IBATIS-281 patches are applied
   */
  @Test
  void test16() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest16", po);
      assertEquals(6, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(4, ((Person) results.get(3)).getId().intValue());
      assertEquals(5, ((Person) results.get(4)).getId().intValue());
      assertEquals(6, ((Person) results.get(5)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return six rows: ids 1-6.
   * <p>
   * This method works when Christian's IBATIS-281 patches are applied
   */
  @Test
  void test17() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest17", po);
      assertEquals(6, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(4, ((Person) results.get(3)).getId().intValue());
      assertEquals(5, ((Person) results.get(4)).getId().intValue());
      assertEquals(6, ((Person) results.get(5)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method tests <isNotPropertyAvailable> inside in <iterate>
   */
  @Test
  void test18() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest18", po);
      assertEquals(2, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method tests <isNotNull> inside an <iterate>
   */
  @Test
  void test19() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest19", po);
      assertEquals(2, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method tests <isNotEmpty> inside an <iterate>
   */
  @Test
  void test20() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest20", po);
      assertEquals(2, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return nine rows: ids 1-9
   * <p>
   * This method tests the open, close, and prepend attributes when no sub elements satisfy - so no where clause should
   * be generated
   */
  @Test
  void test21() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest21", po);
      assertEquals(9, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
      assertEquals(4, ((Person) results.get(3)).getId().intValue());
      assertEquals(5, ((Person) results.get(4)).getId().intValue());
      assertEquals(6, ((Person) results.get(5)).getId().intValue());
      assertEquals(7, ((Person) results.get(6)).getId().intValue());
      assertEquals(8, ((Person) results.get(7)).getId().intValue());
      assertEquals(9, ((Person) results.get(8)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return one rows: id 4
   * <p>
   * This method tests the open, close, and prepend attributes when the first element doesn't satisfy
   */
  @Test
  void test22() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest22", po);
      assertEquals(1, results.size());
      assertEquals(4, ((Person) results.get(0)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return one rows: id 1
   */
  @Test
  void test23() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest23", po);
      assertEquals(1, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return one rows: id 9
   */
  @Test
  void test24() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest24", po);
      assertEquals(1, results.size());
      assertEquals(9, ((Person) results.get(0)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This method should return three rows: id 1-3
   */
  @Test
  void test25() {
    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));
    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest25", po);
      assertEquals(3, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list
   */
  @Test
  void test26() {

    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));

    Map<String, NestedIterateParameterObject> params = new HashMap<>();
    params.put("po", po);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest26", params);
      assertEquals(3, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }

  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list same as test26 except deeper
   */
  @Test
  void test27() {

    Map<String, List<Map<String, NestedIterateParameterObject>>> firstMap = new HashMap<>();

    List<Map<String, NestedIterateParameterObject>> firstList = new ArrayList<>();

    Map<String, NestedIterateParameterObject> params = new HashMap<>();

    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));

    params.put("po", po);

    firstList.add(params);

    firstMap.put("firstList", firstList);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest27", firstMap);
      assertEquals(3, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }

  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list
   */
  @Test
  void test28() {

    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));

    SimpleNestedParameterObject simpleNestedParameterObject = new SimpleNestedParameterObject();

    simpleNestedParameterObject.setNestedIterateParameterObject(po);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest28", simpleNestedParameterObject);
      assertEquals(3, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }

  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list same as test26 except deeper
   */
  @Test
  void test29() {

    SimpleNestedParameterObject firstParameterObject = new SimpleNestedParameterObject();

    SimpleNestedParameterObject secondParameterObject = new SimpleNestedParameterObject();

    List<SimpleNestedParameterObject> parameterObjectList = new ArrayList<>();

    NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));

    secondParameterObject.setNestedIterateParameterObject(po);

    parameterObjectList.add(secondParameterObject);

    firstParameterObject.setNestedList(parameterObjectList);

    try {
      List<?> results = sqlMap.queryForList("NestedIterateTest29", firstParameterObject);
      assertEquals(3, results.size());
      assertEquals(1, ((Person) results.get(0)).getId().intValue());
      assertEquals(2, ((Person) results.get(1)).getId().intValue());
      assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * This tests nesting when a list is initially nested in a bean. so it tests
   * [bean]->[list]->[property_of_object_on_exposed_index]
   */
  @Test
  void test30() {

    try {

      // prepare item list
      Item item1 = new Item();
      item1.setItemId("EST-1");
      item1.setProductId("FI-SW-01");

      List<Item> itemList = new ArrayList<>();
      itemList.add(item1);

      // prepare product list
      Product product1 = new Product();
      product1.setProductId("FI-SW-01");
      product1.setCategoryId("DOGS");
      product1.setItemList(itemList);

      List<Product> productList = new ArrayList<>();
      productList.add(product1);

      // prepare parent category
      Category parentCategory = new Category();
      parentCategory.setCategoryId("DOGS");
      parentCategory.setProductList(productList);

      // setup Category
      Category category = new Category();
      category.setCategoryId("FISH");
      category.setParentCategory(parentCategory);

      List<?> results = sqlMap.queryForList("NestedIterateTest30", category);
      assertEquals(1, results.size());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

}
