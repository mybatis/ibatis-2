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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;
import testdomain.MyBean;

class DynamicPrependTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
  }

  // Iterate with prepend

  @Test
  void testIterateWithPrepend1() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend1", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testIterateWithPrepend2() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend2", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
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

    final List<Account> params = Arrays.asList(account1, account2, account3);
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend2b", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
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

    final List<?> params = Arrays.asList(account1, account2, account3);

    final MyBean x = new MyBean();
    x.setMyList(params);

    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend2c", x);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend2d() throws SQLException {

    final List<?> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));

    final MyBean x = new MyBean();
    x.setMyList(params);

    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend2d", x);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(3, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend2e() throws SQLException {

    final Object[] params = { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) };

    final MyBean x = new MyBean();
    x.setMyArray(params);

    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend2e", x);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(3, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend2f() throws SQLException {

    final int[] params = { 1, 2, 3 };

    final MyBean x = new MyBean();
    x.setIntArray(params);

    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend2f", x);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(3, ((Account) list.get(1)).getId());

  }

  @Test
  void testIterateWithPrepend3() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateWithPrepend3", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testDynamicWithPrepend1() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account = (Account) BaseSqlMap.sqlMap.queryForObject("dynamicWithPrepend", account);
    this.assertAccount1(account);
  }

  @Test
  void testDynamicWithPrepend2() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account = (Account) BaseSqlMap.sqlMap.queryForObject("dynamicWithPrepend", account);
    this.assertAccount1(account);
  }

  @Test
  void testDynamicWithPrepend3() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account.setLastName("Begin");
    account = (Account) BaseSqlMap.sqlMap.queryForObject("dynamicWithPrepend", account);
    this.assertAccount1(account);
  }

  @Test
  void testIterateWithPrepend4() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicWithPrepend", null);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(5, list.size());
  }

  @Test
  void testIterateWithTwoPrepends() throws SQLException {
    Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account = (Account) BaseSqlMap.sqlMap.queryForObject("dynamicWithPrepend", account);
    Assertions.assertNotNull(account);
    this.assertAccount1(account);

    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicWithTwoDynamicElements", account);
    this.assertAccount1((Account) list.get(0));
  }

  @Test
  void testComplexDynamic() throws SQLException {
    final Account account = new Account();
    account.setId(1);
    account.setFirstName("Clinton");
    account.setLastName("Begin");
    final List<?> list = BaseSqlMap.sqlMap.queryForList("complexDynamicStatement", account);
    this.assertAccount1((Account) list.get(0));
  }
}
