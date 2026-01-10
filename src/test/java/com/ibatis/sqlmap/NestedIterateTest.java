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
package com.ibatis.sqlmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
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
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/person-init.sql");
    BaseSqlMap.initScript("scripts/jpetstore-hsqldb-schema.sql");
    BaseSqlMap.initScript("scripts/jpetstore-hsqldb-dataload.sql");
  }

  /**
   * This test should return 9 rows: ids 1-9 This method works as expected
   */
  @Test
  void testShouldReturn9Rows() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest1", po);
      Assertions.assertEquals(9, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(4, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(5)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(6)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(7)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(8)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test shoud return 1 row: id 4
   */
  @Test
  void test02() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest2", po);
      Assertions.assertEquals(1, results.size());
      Assertions.assertEquals(4, ((Person) results.get(0)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 8 rows: ids 1-3, 5-9
   */
  @Test
  void test03() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest3", po);
      Assertions.assertEquals(8, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(5)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(6)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(7)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 5 rows: ids 5-9
   */
  @Test
  void test04() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest4", po);
      Assertions.assertEquals(5, results.size());
      Assertions.assertEquals(5, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(4)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 6 rows: ids 4-9
   */
  @Test
  void test05() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest5", po);
      Assertions.assertEquals(6, results.size());
      Assertions.assertEquals(4, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(5)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 3 rows: ids 1-3
   */
  @Test
  void test06() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest6", po);
      Assertions.assertEquals(3, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 4 rows: ids 1-4
   */
  @Test
  void test07() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest7", po);
      Assertions.assertEquals(4, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(4, ((Person) results.get(3)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return four rows: ids 1, 2, 7, 8
   */
  @Test
  void test08() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addFirstName("Jeff");
    po.addFirstName("Matt");
    po.addLastName("Jones");
    po.addLastName("Smith");

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest8", po);
      Assertions.assertEquals(4, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(3)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method works when Christian's IBATIS-281 patches are applied
   */
  @Test
  void test09() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest9", po);
      Assertions.assertEquals(2, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void test09a() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest9a", po);
      Assertions.assertEquals(2, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test shoud return 1 row: id 4
   */
  @Test
  void test10() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest10", po);
      Assertions.assertEquals(1, results.size());
      Assertions.assertEquals(4, ((Person) results.get(0)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 8 rows: ids 1-3, 5-9
   */
  @Test
  void test11() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest11", po);
      Assertions.assertEquals(8, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(5)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(6)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(7)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 5 rows: ids 5-9
   */
  @Test
  void test12() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest12", po);
      Assertions.assertEquals(5, results.size());
      Assertions.assertEquals(5, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(4)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 6 rows: ids 4-9
   */
  @Test
  void test13() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest13", po);
      Assertions.assertEquals(6, results.size());
      Assertions.assertEquals(4, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(5)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 3 rows: ids 1-3
   */
  @Test
  void test14() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest14", po);
      Assertions.assertEquals(3, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This test should return 4 rows: ids 1-4
   */
  @Test
  void test15() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest15", po);
      Assertions.assertEquals(4, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(4, ((Person) results.get(3)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return six rows: ids 1-6
   * <p>
   * This method works when Christian's IBATIS-281 patches are applied
   */
  @Test
  void test16() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest16", po);
      Assertions.assertEquals(6, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(4, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(5)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return six rows: ids 1-6.
   * <p>
   * This method works when Christian's IBATIS-281 patches are applied
   */
  @Test
  void test17() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest17", po);
      Assertions.assertEquals(6, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(4, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(5)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method tests <isNotPropertyAvailable> inside in <iterate>
   */
  @Test
  void test18() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest18", po);
      Assertions.assertEquals(2, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method tests <isNotNull> inside an <iterate>
   */
  @Test
  void test19() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest19", po);
      Assertions.assertEquals(2, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return two rows: ids 1, 2
   * <p>
   * This method tests <isNotEmpty> inside an <iterate>
   */
  @Test
  void test20() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();

    NestedIterateParameterObject.AndCondition andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Jeff", Boolean.valueOf(false));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    andCondition = new NestedIterateParameterObject.AndCondition();
    andCondition.addCondition("first_name =", "Matt", Boolean.valueOf(true));
    andCondition.addCondition("last_name =", "Jones", Boolean.valueOf(true));
    po.addOrCondition(andCondition);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest20", po);
      Assertions.assertEquals(2, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
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
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest21", po);
      Assertions.assertEquals(9, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
      Assertions.assertEquals(4, ((Person) results.get(3)).getId().intValue());
      Assertions.assertEquals(5, ((Person) results.get(4)).getId().intValue());
      Assertions.assertEquals(6, ((Person) results.get(5)).getId().intValue());
      Assertions.assertEquals(7, ((Person) results.get(6)).getId().intValue());
      Assertions.assertEquals(8, ((Person) results.get(7)).getId().intValue());
      Assertions.assertEquals(9, ((Person) results.get(8)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return one rows: id 4
   * <p>
   * This method tests the open, close, and prepend attributes when the first element doesn't satisfy
   */
  @Test
  void test22() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest22", po);
      Assertions.assertEquals(1, results.size());
      Assertions.assertEquals(4, ((Person) results.get(0)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return one rows: id 1
   */
  @Test
  void test23() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest23", po);
      Assertions.assertEquals(1, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return one rows: id 9
   */
  @Test
  void test24() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest24", po);
      Assertions.assertEquals(1, results.size());
      Assertions.assertEquals(9, ((Person) results.get(0)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This method should return three rows: id 1-3
   */
  @Test
  void test25() {
    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest25", po);
      Assertions.assertEquals(3, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list
   */
  @Test
  void test26() {

    final NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));

    final Map<String, NestedIterateParameterObject> params = new HashMap<>();
    params.put("po", po);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest26", params);
      Assertions.assertEquals(3, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }

  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list same as test26 except deeper
   */
  @Test
  void test27() {

    final Map<String, List<Map<String, NestedIterateParameterObject>>> firstMap = new HashMap<>();

    final List<Map<String, NestedIterateParameterObject>> firstList = new ArrayList<>();

    final Map<String, NestedIterateParameterObject> params = new HashMap<>();

    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest27", firstMap);
      Assertions.assertEquals(3, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }

  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list
   */
  @Test
  void test28() {

    final NestedIterateParameterObject po = new NestedIterateParameterObject();
    po.addId(Integer.valueOf(1));
    po.addId(Integer.valueOf(2));
    po.addId(Integer.valueOf(3));
    po.addId(Integer.valueOf(4));
    po.addId(Integer.valueOf(5));
    po.addId(Integer.valueOf(6));
    po.addId(Integer.valueOf(7));
    po.addId(Integer.valueOf(8));
    po.addId(Integer.valueOf(9));

    final SimpleNestedParameterObject simpleNestedParameterObject = new SimpleNestedParameterObject();

    simpleNestedParameterObject.setNestedIterateParameterObject(po);

    try {
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest28", simpleNestedParameterObject);
      Assertions.assertEquals(3, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }

  }

  /**
   * This tests nesting when objects are maps and not a list nested in a list same as test26 except deeper
   */
  @Test
  void test29() {

    final SimpleNestedParameterObject firstParameterObject = new SimpleNestedParameterObject();

    final SimpleNestedParameterObject secondParameterObject = new SimpleNestedParameterObject();

    final List<SimpleNestedParameterObject> parameterObjectList = new ArrayList<>();

    final NestedIterateParameterObject po = new NestedIterateParameterObject();
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
      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest29", firstParameterObject);
      Assertions.assertEquals(3, results.size());
      Assertions.assertEquals(1, ((Person) results.get(0)).getId().intValue());
      Assertions.assertEquals(2, ((Person) results.get(1)).getId().intValue());
      Assertions.assertEquals(3, ((Person) results.get(2)).getId().intValue());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
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
      final Item item1 = new Item();
      item1.setItemId("EST-1");
      item1.setProductId("FI-SW-01");

      final List<Item> itemList = new ArrayList<>();
      itemList.add(item1);

      // prepare product list
      final Product product1 = new Product();
      product1.setProductId("FI-SW-01");
      product1.setCategoryId("DOGS");
      product1.setItemList(itemList);

      final List<Product> productList = new ArrayList<>();
      productList.add(product1);

      // prepare parent category
      final Category parentCategory = new Category();
      parentCategory.setCategoryId("DOGS");
      parentCategory.setProductList(productList);

      // setup Category
      final Category category = new Category();
      category.setCategoryId("FISH");
      category.setParentCategory(parentCategory);

      final List<?> results = BaseSqlMap.sqlMap.queryForList("NestedIterateTest30", category);
      Assertions.assertEquals(1, results.size());
    } catch (final Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

}
