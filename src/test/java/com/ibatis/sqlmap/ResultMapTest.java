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

import com.ibatis.common.jdbc.exception.NestedSQLException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;
import testdomain.Order;

class ResultMapTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
    BaseSqlMap.initScript("scripts/order-init.sql");
    BaseSqlMap.initScript("scripts/line_item-init.sql");
  }

  // RESULT MAP FEATURE TESTS

  @Test
  void testColumnsByName() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderLiteByColumnName", Integer.valueOf(1));
    this.assertOrder1(order);
  }

  @Test
  void testExtendedResultMap() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderLiteByColumnName", Integer.valueOf(1));
    this.assertOrder1(order);
  }

  @Test
  void testColumnsByIndex() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderLiteByColumnIndex", Integer.valueOf(1));
    this.assertOrder1(order);
  }

  @Test
  void testNullValueReplacement() throws SQLException {
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(5));
    Assertions.assertEquals("no_email@provided.com", account.getEmailAddress());
  }

  @Test
  void testTypeSpecified() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithTypes", Integer.valueOf(1));
    this.assertOrder1(order);
  }

  @Test
  void testComplexObjectMapping() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithAccount", Integer.valueOf(1));
    this.assertOrder1(order);
    this.assertAccount1(order.getAccount());
  }

  @Test
  void testCollectionMappingAndExtends() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithLineItemsCollection", Integer.valueOf(1));

    this.assertOrder1(order);
    Assertions.assertNotNull(order.getLineItems());
    Assertions.assertEquals(2, order.getLineItems().size());
  }

  @Test
  void testListMapping() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithLineItems", Integer.valueOf(1));

    this.assertOrder1(order);
    Assertions.assertNotNull(order.getLineItemsList());
    Assertions.assertEquals(2, order.getLineItemsList().size());
  }

  @Test
  void testGetAllLineItemProps() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllLineItemProps", Integer.valueOf(1));

    Assertions.assertNotNull(list);
    Assertions.assertEquals(2, list.size());
  }

  @Test
  void testGetSomeLineItemProps() throws SQLException {
    try {
      BaseSqlMap.sqlMap.queryForList("getSomeLineItemProps", Integer.valueOf(1));

      Assertions.fail("Expected exception because column was missing.");
    } catch (final NestedSQLException e) {
      // expected
    }
  }

  @Test
  void testArrayMapping() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithLineItemArray", Integer.valueOf(1));

    this.assertOrder1(order);
    Assertions.assertNotNull(order.getLineItemArray());
    Assertions.assertEquals(2, order.getLineItemArray().length);
  }

  @Test
  void testHashMapMapping() throws SQLException {
    final Map<?, ?> order = (Map<?, ?>) BaseSqlMap.sqlMap.queryForObject("getOrderAsMap", Integer.valueOf(1));
    this.assertOrder1(order);
  }

  @Test
  void testNestedObjects() throws SQLException {
    final Order order = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderJoinedFavourite", Integer.valueOf(1));
    this.assertOrder1(order);
  }

  @Test
  void testSimpleTypeMapping() throws SQLException {
    final List<?> list = BaseSqlMap.sqlMap.queryForList("getAllCreditCardNumbersFromOrders", null);

    Assertions.assertEquals(5, list.size());
    Assertions.assertEquals("555555555555", list.get(0));
  }

  @Test
  void testCompositeKeyMapping() throws SQLException {

    final Order order1 = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithFavouriteLineItem", Integer.valueOf(1));
    final Order order2 = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithFavouriteLineItem", Integer.valueOf(2));

    Assertions.assertNotNull(order1);
    Assertions.assertNotNull(order1.getFavouriteLineItem());
    Assertions.assertEquals(2, order1.getFavouriteLineItem().getId());
    Assertions.assertEquals(1, order1.getFavouriteLineItem().getOrderId());

    Assertions.assertNotNull(order2);
    Assertions.assertNotNull(order2.getFavouriteLineItem());
    Assertions.assertEquals(1, order2.getFavouriteLineItem().getId());
    Assertions.assertEquals(2, order2.getFavouriteLineItem().getOrderId());

  }

  @Test
  void testDynCompositeKeyMapping() throws SQLException {

    final Order order1 = (Order) BaseSqlMap.sqlMap.queryForObject("getOrderWithDynFavouriteLineItem",
        Integer.valueOf(1));

    Assertions.assertNotNull(order1);
    Assertions.assertNotNull(order1.getFavouriteLineItem());
    Assertions.assertEquals(2, order1.getFavouriteLineItem().getId());
    Assertions.assertEquals(1, order1.getFavouriteLineItem().getOrderId());

  }

  @Test
  void testGetDoubleNestedResult() throws SQLException {
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getNestedAccountViaColumnName",
        Integer.valueOf(1));
    this.assertAccount1(account);
  }

}
