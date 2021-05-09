/*
 * Copyright 2004-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibatis.sqlmap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;

public class IterateTest extends BaseSqlMapTest {

  @BeforeEach
  public void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  // Iterate

  @Test
  public void testIterate() throws SQLException {
    List params = Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) });
    List list = sqlMap.queryForList("dynamicIterate", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  // Iterate

  @Test
  public void testIterateInConditional() throws SQLException {
    List params = Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) });
    List list = sqlMap.queryForList("dynamicIterateInConditional", params);
    assertEquals(2, list.size());
    assertAccount1((Account) list.get(0));
    assertEquals(1, ((Account) list.get(0)).getId());
    assertEquals(3, ((Account) list.get(1)).getId());
  }

  @Test
  public void testIterateLiteral() throws SQLException {
    List params = Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) });
    List list = sqlMap.queryForList("dynamicIterateLiteral", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testMultiIterate() throws SQLException {
    List params = Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) });
    List list = sqlMap.queryForList("multiDynamicIterate", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testMultiIterateLiteral() throws SQLException {
    List params = Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) });
    List list = sqlMap.queryForList("multiDynamicIterateLiteral", params);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  // ARRAY

  @Test
  public void testArrayPropertyIterate() throws SQLException {
    Account account = new Account();
    account.setIds(new int[] { 1, 2, 3 });
    List list = sqlMap.queryForList("dynamicQueryByExample", account);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testArrayPropertyIterate2() throws SQLException {
    Account account = new Account();
    account.setAge(4);
    account.setIds(new int[] { 1, 2, 3 });
    List list = sqlMap.queryForList("dynamicQueryByExample2", account);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testArrayPropertyIterate2Literal() throws SQLException {
    Account account = new Account();
    account.setAge(4);
    account.setIds(new int[] { 1, 2, 3 });
    List list = sqlMap.queryForList("dynamicQueryByExample2Literal", account);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  // LIST IN MAP

  @Test
  public void testListInMap() throws SQLException {
    List paramList = new ArrayList();
    paramList.add(Integer.valueOf(1));
    paramList.add(Integer.valueOf(2));
    paramList.add(Integer.valueOf(3));

    Map paramMap = new HashMap();
    paramMap.put("paramList", paramList);

    List list = sqlMap.queryForList("iterateListInMap", paramMap);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testListDirect() throws SQLException {
    List paramList = new ArrayList();
    paramList.add(Integer.valueOf(1));
    paramList.add(Integer.valueOf(2));
    paramList.add(Integer.valueOf(3));

    List list = sqlMap.queryForList("iterateListDirect", paramList);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testIterateNestedListProperty() throws SQLException {
    Account account = new Account();
    account.setAccountList(new ArrayList());
    account.getAccountList().add(new Account(1));
    account.getAccountList().add(new Account(2));
    account.getAccountList().add(new Account(3));

    List list = sqlMap.queryForList("iterateNestedListProperty", account);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testIterateNestedListPropertyB() throws SQLException {
    Account account = new Account();
    account.setId(99);
    account.setAccountList(new ArrayList());
    account.getAccountList().add(new Account(1));
    account.getAccountList().add(new Account(2));
    account.getAccountList().add(new Account(3));

    List list = sqlMap.queryForList("iterateNestedListPropertyB", account);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void testIterateNestedMapListProperty() throws SQLException {
    Map account = new HashMap();
    List accountList = new ArrayList();
    account.put("accountList", accountList);
    accountList.add(new Account(1));
    accountList.add(new Account(2));
    accountList.add(new Account(3));

    List list = sqlMap.queryForList("iterateNestedMapListProperty", account);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

  @Test
  public void xtestArrayPropertyIterate2() throws SQLException {
    Account account = new Account();
    account.setIds(new int[] { 1, 2, 3 });
    List list = sqlMap.queryForList("dynamicQueryByExample2", account);
    assertAccount1((Account) list.get(0));
    assertEquals(3, list.size());
  }

}
