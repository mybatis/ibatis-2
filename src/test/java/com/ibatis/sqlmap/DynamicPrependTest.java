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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;
import testdomain.MyBean;

class DynamicPrependTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  // Iterate with prepend

  @Test
  void testIterateWithPrepend1() throws SQLException {
    List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend1", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  void testIterateWithPrepend2() throws SQLException {
    List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend2", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  void testIterateWithPrepend2b() throws SQLException {

    Account account1, account2, account3;
    account1 = new Account();
    account1.setId(1);

    account2 = new Account();
    account2.setId(2);

    account3 = new Account();
    account3.setId(3);

    List<Account> params = Arrays.asList(account1, account2, account3);
    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend2b", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  void testIterateWithPrepend2c() throws SQLException {

    Account account1, account2, account3;
    account1 = new Account();
    account1.setId(1);

    account2 = new Account();
    account2.setId(2);

    account3 = new Account();
    account3.setId(3);

    List<?> params = Arrays.asList(account1, account2, account3);

    MyBean x = new MyBean();
    x.setMyList(params);

    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend2c", x);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
    assertEquals(1, ((Account) list.get(0)).getId());
    assertEquals(2, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend2d() throws SQLException {

    List<?> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));

    MyBean x = new MyBean();
    x.setMyList(params);

    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend2d", x);
    assertAccount1((Account) list.get(0));
    assertEquals(2, list.size());
    assertEquals(1, ((Account) list.get(0)).getId());
    assertEquals(3, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend2e() throws SQLException {

    Object[] params = { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) };

    MyBean x = new MyBean();
    x.setMyArray(params);

    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend2e", x);
    assertAccount1((Account) list.get(0));
    assertEquals(2, list.size());
    assertEquals(1, ((Account) list.get(0)).getId());
    assertEquals(3, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend2f() throws SQLException {

    int[] params = { 1, 2, 3 };

    MyBean x = new MyBean();
    x.setIntArray(params);

    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend2f", x);
    assertAccount1((Account) list.get(0));
    assertEquals(2, list.size());
    assertEquals(1, ((Account) list.get(0)).getId());
    assertEquals(3, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend3() throws SQLException {
    List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    List<?> list = sqlMap.queryForList("dynamicIterateWithPrepend3", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  void testDynamicWithPrepend1() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account = (Account) sqlMap.queryForObject("dynamicWithPrepend", account);
    assertAccount1(account);
  }

  @Test
  void testDynamicWithPrepend2() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account = (Account) sqlMap.queryForObject("dynamicWithPrepend", account);
    assertAccount1(account);
  }

  @Test
  void testDynamicWithPrepend3() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account.setLastName("Begin");
    account = (Account) sqlMap.queryForObject("dynamicWithPrepend", account);
    assertAccount1(account);
  }

  @Test
  void testIterateWithPrepend4() throws SQLException {
    List<?> list = sqlMap.queryForList("dynamicWithPrepend", null);
    assertAccount1((Account) list.get(0));
    assertEquals(5, list.size());
  }

  @Test
  void testIterateWithTwoPrepends() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account = (Account) sqlMap.queryForObject("dynamicWithPrepend", account);
    assertNotNull(account);
    assertAccount1(account);

    List<?> list = sqlMap.queryForList("dynamicWithTwoDynamicElements", account);
    assertAccount1((Account) list.get(0));
  }

  @Test
  void testComplexDynamic() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account.setLastName("Begin");
    List<?> list = sqlMap.queryForList("complexDynamicStatement", account);
    assertAccount1((Account) list.get(0));
  }
}
