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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;

class IterateTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
  }

  // Iterate

  @Test
  void testIterate() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterate", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  // Iterate

  @Test
  void testIterateInConditional() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateInConditional", params);
    Assertions.assertEquals(2, list.size());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(3, ((Account) list.get(1)).getId());
  }

  @Test
  void testIterateLiteral() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicIterateLiteral", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testMultiIterate() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("multiDynamicIterate", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testMultiIterateLiteral() throws SQLException {
    final List<Integer> params = Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3));
    final List<?> list = BaseSqlMap.sqlMap.queryForList("multiDynamicIterateLiteral", params);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  // ARRAY

  @Test
  void testArrayPropertyIterate() throws SQLException {
    final Account account = new Account();
    account.setIds(new int[] { 1, 2, 3 });
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicQueryByExample", account);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testArrayPropertyIterate2() throws SQLException {
    final Account account = new Account();
    account.setAge(4);
    account.setIds(new int[] { 1, 2, 3 });
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicQueryByExample2", account);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testArrayPropertyIterate2Literal() throws SQLException {
    final Account account = new Account();
    account.setAge(4);
    account.setIds(new int[] { 1, 2, 3 });
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicQueryByExample2Literal", account);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  // LIST IN MAP

  @Test
  void testListInMap() throws SQLException {
    final List<Integer> paramList = new ArrayList<>();
    paramList.add(Integer.valueOf(1));
    paramList.add(Integer.valueOf(2));
    paramList.add(Integer.valueOf(3));

    final Map<String, List<Integer>> paramMap = new HashMap<>();
    paramMap.put("paramList", paramList);

    final List<?> list = BaseSqlMap.sqlMap.queryForList("iterateListInMap", paramMap);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testListDirect() throws SQLException {
    final List<Integer> paramList = new ArrayList<>();
    paramList.add(Integer.valueOf(1));
    paramList.add(Integer.valueOf(2));
    paramList.add(Integer.valueOf(3));

    final List<?> list = BaseSqlMap.sqlMap.queryForList("iterateListDirect", paramList);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testIterateNestedListProperty() throws SQLException {
    final Account account = new Account();
    account.setAccountList(new ArrayList<>());
    account.getAccountList().add(new Account(1));
    account.getAccountList().add(new Account(2));
    account.getAccountList().add(new Account(3));

    final List<?> list = BaseSqlMap.sqlMap.queryForList("iterateNestedListProperty", account);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testIterateNestedListPropertyB() throws SQLException {
    final Account account = new Account();
    account.setId(99);
    account.setAccountList(new ArrayList<>());
    account.getAccountList().add(new Account(1));
    account.getAccountList().add(new Account(2));
    account.getAccountList().add(new Account(3));

    final List<?> list = BaseSqlMap.sqlMap.queryForList("iterateNestedListPropertyB", account);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void testIterateNestedMapListProperty() throws SQLException {
    final Map<String, List<Account>> account = new HashMap<>();
    final List<Account> accountList = new ArrayList<>();
    account.put("accountList", accountList);
    accountList.add(new Account(1));
    accountList.add(new Account(2));
    accountList.add(new Account(3));

    final List<?> list = BaseSqlMap.sqlMap.queryForList("iterateNestedMapListProperty", account);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

  @Test
  void xtestArrayPropertyIterate2() throws SQLException {
    final Account account = new Account();
    account.setIds(new int[] { 1, 2, 3 });
    final List<?> list = BaseSqlMap.sqlMap.queryForList("dynamicQueryByExample2", account);
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
  }

}
