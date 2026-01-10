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

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;
import testdomain.LineItem;
import testdomain.Order;
import testdomain.SuperAccount;

class StatementTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
    BaseSqlMap.initScript("scripts/order-init.sql");
    BaseSqlMap.initScript("scripts/line_item-init.sql");
  }

  // OBJECT QUERY TESTS

  @Test
  void testExecuteQueryForObjectViaColumnName() throws SQLException {
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(1));
    this.assertAccount1(account);
  }

  @Test
  void testUserConnection() throws SQLException {
    final DataSource ds = BaseSqlMap.sqlMap.getDataSource();
    final Connection conn = ds.getConnection();
    ((SqlMapClientImpl) BaseSqlMap.sqlMap).getDelegate().getTxManager().getConfig().setDataSource(null);
    BaseSqlMap.sqlMap.setUserConnection(conn);
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(1));
    conn.close();
    this.assertAccount1(account);
    ((SqlMapClientImpl) BaseSqlMap.sqlMap).getDelegate().getTxManager().getConfig().setDataSource(ds);
  }

  @Test
  void testSessionUserConnection() throws SQLException {
    final DataSource ds = BaseSqlMap.sqlMap.getDataSource();
    final Connection conn = ds.getConnection();
    ((SqlMapClientImpl) BaseSqlMap.sqlMap).getDelegate().getTxManager().getConfig().setDataSource(null);
    final SqlMapSession session = BaseSqlMap.sqlMap.openSession(conn);
    final Account account = (Account) session.queryForObject("getAccountViaColumnName", Integer.valueOf(1));
    session.close();
    conn.close();
    this.assertAccount1(account);
    ((SqlMapClientImpl) BaseSqlMap.sqlMap).getDelegate().getTxManager().getConfig().setDataSource(ds);
  }

  @Test
  void testSessionUserConnectionFailures() throws SQLException {
    final DataSource ds = BaseSqlMap.sqlMap.getDataSource();
    final Connection conn = ds.getConnection();
    ((SqlMapClientImpl) BaseSqlMap.sqlMap).getDelegate().getTxManager().getConfig().setDataSource(null);
    final SqlMapSession session = BaseSqlMap.sqlMap.openSession(conn);

    Exception expected = null;
    try {
      session.startTransaction();
    } catch (final Exception e) {
      expected = e;
    }
    Assertions.assertNotNull(expected, "Expected exception from startTransaction() was not detected.");
    expected = null;
    try {
      session.commitTransaction();
    } catch (final Exception e) {
      expected = e;
    }
    Assertions.assertNotNull(expected, "Expected exception from commitTransaction() was not detected.");
    expected = null;
    try {
      session.endTransaction();
    } catch (final Exception e) {
      expected = e;
    }
    Assertions.assertNotNull(expected, "Expected exception from endTransaction() was not detected.");
    expected = null;

    final Account account = (Account) session.queryForObject("getAccountViaColumnName", Integer.valueOf(1));
    session.close();
    conn.close();
    this.assertAccount1(account);
    ((SqlMapClientImpl) BaseSqlMap.sqlMap).getDelegate().getTxManager().getConfig().setDataSource(ds);
  }

  @Test
  void testExecuteQueryForObjectViaColumnIndex() throws SQLException {
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnIndex", Integer.valueOf(1));
    this.assertAccount1(account);
  }

  @Test
  void testExecuteQueryForObjectViaResultClass() throws SQLException {
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaResultClass", Integer.valueOf(1));
    this.assertAccount1(account);
  }

  @Test
  void testExecuteQueryForObjectViaResultClassIgnoreCaseTypeAliasCase() throws SQLException {
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaResultClassIgnoreCaseTypeAlias",
        Integer.valueOf(1));
    this.assertAccount1(account);
  }

  @Test
  void testExecuteQueryForObjectViaResultClassPlusOne() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAccountViaResultClassPlusOne", Integer.valueOf(1));
    this.assertList(list);
  }

  @Test
  void testExecuteQueryForObjectAsHashMap() throws SQLException {
    final Map<?, ?> account = (HashMap<?, ?>) BaseSqlMap.sqlMap.queryForObject("getAccountAsHashMap",
        Integer.valueOf(1));
    this.assertAccount1(account);
  }

  @Test
  void testExecuteQueryForObjectAsHashMapResultClass() throws SQLException {
    final Map<?, ?> account = (HashMap<?, ?>) BaseSqlMap.sqlMap.queryForObject("getAccountAsHashMapResultClass",
        Integer.valueOf(1));
    this.assertAccount1(account);
  }

  @Test
  void testExecuteQueryForObjectWithSimpleResultClass() throws SQLException {
    final String email = (String) BaseSqlMap.sqlMap.queryForObject("getEmailAddressViaResultClass", Integer.valueOf(1));
    Assertions.assertEquals("clinton.begin@ibatis.com", email);
  }

  @Test
  void testExecuteQueryForObjectWithSimpleResultMap() throws SQLException {
    final String email = (String) BaseSqlMap.sqlMap.queryForObject("getEmailAddressViaResultMap", Integer.valueOf(1));
    Assertions.assertEquals("clinton.begin@ibatis.com", email);
  }

  @Test
  void testExecuteQueryForObjectWithResultObject() throws SQLException {
    final Account account = new Account();
    final Account testAccount = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName",
        Integer.valueOf(1), account);
    this.assertAccount1(account);
    Assertions.assertTrue(account == testAccount);
  }

  @Test
  void testGetSubclass() throws SQLException {
    SuperAccount account = new SuperAccount();
    account.setId(1);
    account = (SuperAccount) BaseSqlMap.sqlMap.queryForObject("getSuperAccount", account);
    this.assertAccount1(account);
  }

  // LIST QUERY TESTS

  @Test
  void testExecuteQueryForListWithResultMap() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllAccountsViaResultMap", null);

    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(5, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());
    Assertions.assertEquals(3, ((Account) list.get(2)).getId());
    Assertions.assertEquals(4, ((Account) list.get(3)).getId());
    Assertions.assertEquals(5, ((Account) list.get(4)).getId());

  }

  @Test
  void testExecuteQueryWithCustomTypeHandler() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllAccountsViaCustomTypeHandler", null);

    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(5, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());
    Assertions.assertEquals(3, ((Account) list.get(2)).getId());
    Assertions.assertEquals(4, ((Account) list.get(3)).getId());
    Assertions.assertEquals(5, ((Account) list.get(4)).getId());

    Assertions.assertFalse(((Account) list.get(0)).isCartOption());
    Assertions.assertFalse(((Account) list.get(1)).isCartOption());
    Assertions.assertTrue(((Account) list.get(2)).isCartOption());
    Assertions.assertTrue(((Account) list.get(3)).isCartOption());
    Assertions.assertTrue(((Account) list.get(4)).isCartOption());

    Assertions.assertTrue(((Account) list.get(0)).isBannerOption());
    Assertions.assertTrue(((Account) list.get(1)).isBannerOption());
    Assertions.assertFalse(((Account) list.get(2)).isBannerOption());
    Assertions.assertFalse(((Account) list.get(3)).isBannerOption());
    Assertions.assertTrue(((Account) list.get(4)).isBannerOption());
  }

  /**
   * for bug 976614 - bug squashed 07-14-04 By Brandon Goodin
   */
  /*
   * @Test void testBrokenExecuteQueryForListWithResultMap() throws SQLException { List list =
   * sqlMap.queryForList("getBrokenAllAccountsViaResultMap", null); assertAccount1((Account) list.get(0));
   * assertEquals(5, list.size()); assertEquals(1, ((Account) list.get(0)).getId()); assertEquals(2, ((Account)
   * list.get(1)).getId()); assertEquals(3, ((Account) list.get(2)).getId()); assertEquals(4, ((Account)
   * list.get(3)).getId()); assertEquals(5, ((Account) list.get(4)).getId()); }
   */

  @Test
  void testExecuteQueryForPaginatedList() throws SQLException {

    // Get List of all 5
    PaginatedList list = BaseSqlMap.sqlMap.queryForPaginatedList("getAllAccountsViaResultMap", null, 2);

    // Test initial state (page 0)
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test illegal previous page (no effect, state should be same)
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test next (page 1)
    list.nextPage();
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(3, ((Account) list.get(0)).getId());
    Assertions.assertEquals(4, ((Account) list.get(1)).getId());

    // Test next (page 2 -last)
    list.nextPage();
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(5, ((Account) list.get(0)).getId());

    // Test previous (page 1)
    list.previousPage();
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(3, ((Account) list.get(0)).getId());
    Assertions.assertEquals(4, ((Account) list.get(1)).getId());

    // Test previous (page 0 -first)
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test goto (page 0)
    list.gotoPage(0);
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test goto (page 1)
    list.gotoPage(1);
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(3, ((Account) list.get(0)).getId());
    Assertions.assertEquals(4, ((Account) list.get(1)).getId());

    // Test goto (page 2)
    list.gotoPage(2);
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(5, ((Account) list.get(0)).getId());

    // Test illegal goto (page 0)
    list.gotoPage(3);
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    list = BaseSqlMap.sqlMap.queryForPaginatedList("getNoAccountsViaResultMap", null, 2);

    // Test empty list
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    // Test next
    list.nextPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    // Test previous
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    // Test previous
    list.gotoPage(0);
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    list = BaseSqlMap.sqlMap.queryForPaginatedList("getFewAccountsViaResultMap", null, 2);

    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test next
    list.nextPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test previous
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test previous
    list.gotoPage(0);
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test Even - Two Pages
    try {
      BaseSqlMap.initScript("scripts/more-account-records.sql");
    } catch (final Exception e) {
      Assertions.fail(e.toString());
    }

    list = BaseSqlMap.sqlMap.queryForPaginatedList("getAllAccountsViaResultMap", null, 5);

    Assertions.assertEquals(5, list.size());

    list.nextPage();
    Assertions.assertEquals(5, list.size());

    list.isPreviousPageAvailable();
    list.previousPage();
    Assertions.assertEquals(5, list.size());

  }

  @Test
  void testExecuteQueryForPaginatedList2() throws SQLException {
    // tests methods that don't require a parameter object

    // Get List of all 5
    PaginatedList list = BaseSqlMap.sqlMap.queryForPaginatedList("getAllAccountsViaResultMap", 2);

    // Test initial state (page 0)
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test illegal previous page (no effect, state should be same)
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test next (page 1)
    list.nextPage();
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(3, ((Account) list.get(0)).getId());
    Assertions.assertEquals(4, ((Account) list.get(1)).getId());

    // Test next (page 2 -last)
    list.nextPage();
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(5, ((Account) list.get(0)).getId());

    // Test previous (page 1)
    list.previousPage();
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(3, ((Account) list.get(0)).getId());
    Assertions.assertEquals(4, ((Account) list.get(1)).getId());

    // Test previous (page 0 -first)
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test goto (page 0)
    list.gotoPage(0);
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());

    // Test goto (page 1)
    list.gotoPage(1);
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertTrue(list.isNextPageAvailable());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(3, ((Account) list.get(0)).getId());
    Assertions.assertEquals(4, ((Account) list.get(1)).getId());

    // Test goto (page 2)
    list.gotoPage(2);
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(5, ((Account) list.get(0)).getId());

    // Test illegal goto (page 0)
    list.gotoPage(3);
    Assertions.assertTrue(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    list = BaseSqlMap.sqlMap.queryForPaginatedList("getNoAccountsViaResultMap", 2);

    // Test empty list
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    // Test next
    list.nextPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    // Test previous
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    // Test previous
    list.gotoPage(0);
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(0, list.size());

    list = BaseSqlMap.sqlMap.queryForPaginatedList("getFewAccountsViaResultMap", 2);

    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test next
    list.nextPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test previous
    list.previousPage();
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test previous
    list.gotoPage(0);
    Assertions.assertFalse(list.isPreviousPageAvailable());
    Assertions.assertFalse(list.isNextPageAvailable());
    Assertions.assertEquals(1, list.size());

    // Test Even - Two Pages
    try {
      BaseSqlMap.initScript("scripts/more-account-records.sql");
    } catch (final Exception e) {
      Assertions.fail(e.toString());
    }

    list = BaseSqlMap.sqlMap.queryForPaginatedList("getAllAccountsViaResultMap", 5);

    Assertions.assertEquals(5, list.size());

    list.nextPage();
    Assertions.assertEquals(5, list.size());

    list.isPreviousPageAvailable();
    list.previousPage();
    Assertions.assertEquals(5, list.size());

  }

  @Test
  void testExecuteQueryForListWithResultMapWithDynamicElement() throws SQLException {

    List<?> list = BaseSqlMap.sqlMap.queryForList("getAllAccountsViaResultMapWithDynamicElement", "LIKE");

    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());
    Assertions.assertEquals(4, ((Account) list.get(2)).getId());

    list = BaseSqlMap.sqlMap.queryForList("getAllAccountsViaResultMapWithDynamicElement", "=");

    Assertions.assertEquals(0, list.size());

  }

  @Test
  void testExecuteQueryForListResultClass() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllAccountsViaResultClass", null);

    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(5, list.size());
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());
    Assertions.assertEquals(3, ((Account) list.get(2)).getId());
    Assertions.assertEquals(4, ((Account) list.get(3)).getId());
    Assertions.assertEquals(5, ((Account) list.get(4)).getId());
  }

  @Test
  void testExecuteQueryForListWithHashMapResultMap() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllAccountsAsHashMapViaResultMap", null);

    this.assertAccount1((Map<?, ?>) list.get(0));
    Assertions.assertEquals(5, list.size());
    Assertions.assertEquals(Integer.valueOf(1), ((Map<?, ?>) list.get(0)).get("id"));
    Assertions.assertEquals(Integer.valueOf(2), ((Map<?, ?>) list.get(1)).get("id"));
    Assertions.assertEquals(Integer.valueOf(3), ((Map<?, ?>) list.get(2)).get("id"));
    Assertions.assertEquals(Integer.valueOf(4), ((Map<?, ?>) list.get(3)).get("id"));
    Assertions.assertEquals(Integer.valueOf(5), ((Map<?, ?>) list.get(4)).get("id"));
  }

  @Test
  void testExecuteQueryForListWithHashMapResultClass() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllAccountsAsHashMapViaResultClass", null);

    this.assertAccount1((Map<?, ?>) list.get(0));
    Assertions.assertEquals(5, list.size());
    Assertions.assertEquals(Integer.valueOf(1), ((Map<?, ?>) list.get(0)).get("ID"));
    Assertions.assertEquals(Integer.valueOf(2), ((Map<?, ?>) list.get(1)).get("ID"));
    Assertions.assertEquals(Integer.valueOf(3), ((Map<?, ?>) list.get(2)).get("ID"));
    Assertions.assertEquals(Integer.valueOf(4), ((Map<?, ?>) list.get(3)).get("ID"));
    Assertions.assertEquals(Integer.valueOf(5), ((Map<?, ?>) list.get(4)).get("ID"));
  }

  @Test
  void testExecuteQueryForListWithSimpleResultClass() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllEmailAddressesViaResultClass", null);

    Assertions.assertEquals("clinton.begin@ibatis.com", list.get(0));
    Assertions.assertEquals(5, list.size());
  }

  @Test
  void testExecuteQueryForListWithSimpleResultMap() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllEmailAddressesViaResultMap", null);

    Assertions.assertEquals("clinton.begin@ibatis.com", list.get(0));
    Assertions.assertEquals(5, list.size());
  }

  @Test
  void testExecuteQueryForListWithSkipAndMax() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllAccountsViaResultMap", null, 2, 2);

    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(3, ((Account) list.get(0)).getId());
    Assertions.assertEquals(4, ((Account) list.get(1)).getId());
  }

  @Test
  void testExecuteQueryForListWithRowHandler() throws SQLException {
    final TestRowHandler handler = new TestRowHandler();
    BaseSqlMap.sqlMap.queryWithRowHandler("getAllAccountsViaResultMap", null, handler);
    final List<Object> list = handler.getList();
    Assertions.assertEquals(5, handler.getIndex());
    Assertions.assertEquals(5, list.size());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());
    Assertions.assertEquals(3, ((Account) list.get(2)).getId());
    Assertions.assertEquals(4, ((Account) list.get(3)).getId());
    Assertions.assertEquals(5, ((Account) list.get(4)).getId());

  }

  @Test
  void testExecuteQueryForListWithRowHandler2() throws SQLException {
    // tests method that does not require a parameter object
    final TestRowHandler handler = new TestRowHandler();
    BaseSqlMap.sqlMap.queryWithRowHandler("getAllAccountsViaResultMap", handler);
    final List<Object> list = handler.getList();
    Assertions.assertEquals(5, handler.getIndex());
    Assertions.assertEquals(5, list.size());
    this.assertAccount1((Account) list.get(0));
    Assertions.assertEquals(1, ((Account) list.get(0)).getId());
    Assertions.assertEquals(2, ((Account) list.get(1)).getId());
    Assertions.assertEquals(3, ((Account) list.get(2)).getId());
    Assertions.assertEquals(4, ((Account) list.get(3)).getId());
    Assertions.assertEquals(5, ((Account) list.get(4)).getId());

  }

  @Test
  void testLegacyExecuteQueryForListWithRowHandler() throws SQLException {
    final TestRowHandler handler = new TestRowHandler();
    BaseSqlMap.sqlMap.queryWithRowHandler("getAllAccountsViaResultMap", null, handler);
    Assertions.assertEquals(5, handler.getIndex());
    /*
     * assertEquals(5, list.size()); assertAccount1((Account) list.get(0)); assertEquals(1, ((Account)
     * list.get(0)).getId()); assertEquals(2, ((Account) list.get(1)).getId()); assertEquals(3, ((Account)
     * list.get(2)).getId()); assertEquals(4, ((Account) list.get(3)).getId()); assertEquals(5, ((Account)
     * list.get(4)).getId());
     */
  }

  // MAP TESTS

  @Test
  void testExecuteQueryForMap() throws SQLException {
    final Map<?, ?> map = BaseSqlMap.sqlMap.queryForMap("getAllAccountsViaResultClass", null, "lastName");

    this.assertAccount1((Account) map.get("Begin"));
    Assertions.assertEquals(5, map.size());
    Assertions.assertEquals(1, ((Account) map.get("Begin")).getId());
    Assertions.assertEquals(2, ((Account) map.get("Smith")).getId());
    Assertions.assertEquals(3, ((Account) map.get("Jones")).getId());
    Assertions.assertEquals(4, ((Account) map.get("Jackson")).getId());
    Assertions.assertEquals(5, ((Account) map.get("Goodman")).getId());
  }

  @Test
  void testExecuteQueryForMapWithValueProperty() throws SQLException {
    final Map<?, ?> map = BaseSqlMap.sqlMap.queryForMap("getAllAccountsViaResultClass", null, "lastName", "firstName");

    Assertions.assertEquals(5, map.size());
    Assertions.assertEquals("Clinton", map.get("Begin"));
    Assertions.assertEquals("Jim", map.get("Smith"));
    Assertions.assertEquals("Elizabeth", map.get("Jones"));
    Assertions.assertEquals("Bob", map.get("Jackson"));
    Assertions.assertEquals("&manda", map.get("Goodman"));
  }

  // UPDATE TESTS

  @Test
  void testInsertGeneratedKey() throws SQLException {
    final LineItem item = new LineItem();

    item.setId(10);
    item.setItemCode("blah");
    item.setOrderId(333);
    item.setPrice(new BigDecimal("44.00"));
    item.setQuantity(1);

    final Object key = BaseSqlMap.sqlMap.insert("insertLineItem", item);

    Assertions.assertEquals(Integer.valueOf(99), key);
    Assertions.assertEquals(99, item.getId());

    final Map<String, Integer> param = new HashMap<>();
    param.put("orderId", Integer.valueOf(333));
    param.put("lineId", Integer.valueOf(10));
    final LineItem testItem = (LineItem) BaseSqlMap.sqlMap.queryForObject("getSpecificLineItem", param);
    Assertions.assertNotNull(testItem);
    Assertions.assertEquals(10, testItem.getId());
  }

  @Test
  void testInsertGeneratedKeyFailure() throws SQLException {
    final LineItem item = new LineItem();

    item.setId(0);
    item.setItemCode("blah");
    item.setOrderId(333);
    item.setPrice(new BigDecimal("44.00"));
    item.setQuantity(1);

    Object key = Integer.valueOf(-1);

    try {
      key = BaseSqlMap.sqlMap.insert("insertLineItemOrDie", item);
    } catch (final SQLException e) {
      // this is expected
    }

    Assertions.assertEquals(key, Integer.valueOf(-1)); // this should not be changed from above
    Assertions.assertEquals(0, item.getId()); // this should not be changed from above

  }

  @Test
  void testInsertPreKey() throws SQLException {
    final LineItem item = new LineItem();

    item.setId(10);
    item.setItemCode("blah");
    item.setOrderId(333);
    item.setPrice(new BigDecimal("44.00"));
    item.setQuantity(1);

    final Object key = BaseSqlMap.sqlMap.insert("insertLineItemPreKey", item);

    Assertions.assertEquals(Integer.valueOf(99), key);
    Assertions.assertEquals(99, item.getId());

    final Map<String, Integer> param = new HashMap<>();
    param.put("orderId", Integer.valueOf(333));
    param.put("lineId", Integer.valueOf(99));
    final LineItem testItem = (LineItem) BaseSqlMap.sqlMap.queryForObject("getSpecificLineItem", param);
    Assertions.assertNotNull(testItem);
    Assertions.assertEquals(99, testItem.getId());

  }

  @Test
  void testInsertNoKey() throws SQLException {
    final LineItem item = new LineItem();

    item.setId(100);
    item.setItemCode("blah");
    item.setOrderId(333);
    item.setPrice(new BigDecimal("44.00"));
    item.setQuantity(1);

    final Object key = BaseSqlMap.sqlMap.insert("insertLineItemNoKey", item);

    Assertions.assertNull(key);
    Assertions.assertEquals(100, item.getId());

    final Map<String, Integer> param = new HashMap<>();
    param.put("orderId", Integer.valueOf(333));
    param.put("lineId", Integer.valueOf(100));
    final LineItem testItem = (LineItem) BaseSqlMap.sqlMap.queryForObject("getSpecificLineItem", param);
    Assertions.assertNotNull(testItem);
    Assertions.assertEquals(100, testItem.getId());

  }

  @Test
  void testExecuteUpdateWithParameterMap() throws SQLException {
    Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(1));

    account.setId(6);
    account.setEmailAddress("new.clinton@ibatis.com");
    account.setBannerOption(true);
    account.setCartOption(true);
    BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);

    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(6));

    Assertions.assertEquals(true, account.isBannerOption());
    Assertions.assertEquals(true, account.isCartOption());
    Assertions.assertEquals("new.clinton@ibatis.com", account.getEmailAddress());

  }

  @Test
  void testExecuteUpdateWithInlineParameters() throws SQLException {
    Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(1));

    account.setEmailAddress("new.clinton@ibatis.com");
    try {
      BaseSqlMap.sqlMap.startTransaction();
      BaseSqlMap.sqlMap.update("updateAccountViaInlineParameters", account);
      BaseSqlMap.sqlMap.commitTransaction();
    } finally {
      BaseSqlMap.sqlMap.endTransaction();
    }
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(1));

    Assertions.assertEquals("new.clinton@ibatis.com", account.getEmailAddress());

  }

  @Test
  void testExecuteUpdateWithParameterClass() throws SQLException {
    Account account = new Account();
    account.setId(5);

    boolean checkForInvalidTypeFailedAppropriately = false;
    try {
      BaseSqlMap.sqlMap.update("deleteAccount", new Object());
    } catch (final SQLException e) {
      checkForInvalidTypeFailedAppropriately = true;
    }

    BaseSqlMap.sqlMap.update("deleteAccount", account);

    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(5));

    Assertions.assertNull(account);
    Assertions.assertTrue(checkForInvalidTypeFailedAppropriately);
  }

  /**
   * For bug 959140
   */
  // @Test
  // void testExecuteUpdateWithDuplicateParams() throws SQLException {
  //
  // sqlMap.update("deleteAccountByDuplicateInteger", new Integer (5));
  //
  // Account account = (Account) sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(5));
  //
  // assertNull(account);
  // }

  // DYNAMIC SQL

  @Test
  void testQueryDynamicSqlElement() throws SQLException {
    List<?> list = BaseSqlMap.sqlMap.queryForList("getDynamicOrderedEmailAddressesViaResultMap", "ACC_ID");

    Assertions.assertEquals("clinton.begin@ibatis.com", list.get(0));

    list = BaseSqlMap.sqlMap.queryForList("getDynamicOrderedEmailAddressesViaResultMap", "ACC_FIRST_NAME");

    Assertions.assertNull(list.get(0));

  }

  // INNER CLASSES

  class TestRowHandler implements RowHandler {
    private int index = 0;

    private final List<Object> list = new ArrayList<>();

    @Override
    public void handleRow(final Object object) {
      this.index++;
      Assertions.assertEquals(this.index, ((Account) object).getId());
      this.list.add(object);
    }

    void handleRow(final Object valueObject, final List<Object> list) {
      this.index++;
      Assertions.assertEquals(this.index, ((Account) valueObject).getId());
      list.add(valueObject);
    }

    public int getIndex() {
      return this.index;
    }

    public List<Object> getList() {
      return this.list;
    }

  }

  @Test
  void testNestedResultMaps() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllOrdersWithNestedResultMaps");

    Assertions.assertEquals(10, list.size());

    Order order = (Order) list.get(0);
    Assertions.assertEquals(1, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(1);
    Assertions.assertEquals(4, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(2);
    Assertions.assertEquals(3, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(3);
    Assertions.assertEquals(2, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(4);
    Assertions.assertEquals(5, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(5);
    Assertions.assertEquals(5, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(6);
    Assertions.assertEquals(4, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(7);
    Assertions.assertEquals(3, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(8);
    Assertions.assertEquals(2, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());

    order = (Order) list.get(9);
    Assertions.assertEquals(1, order.getAccount().getId());
    Assertions.assertEquals(2, order.getLineItems().size());
  }
}
