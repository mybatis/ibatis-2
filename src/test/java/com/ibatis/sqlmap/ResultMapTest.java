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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.ibatis.common.jdbc.exception.NestedSQLException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;
import testdomain.Order;

public class ResultMapTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  @BeforeEach
  public void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
    initScript("scripts/order-init.sql");
    initScript("scripts/line_item-init.sql");
  }

  // RESULT MAP FEATURE TESTS

  @Test
  public void testColumnsByName() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderLiteByColumnName", Integer.valueOf(1));
    assertOrder1(order);
  }

  @Test
  public void testExtendedResultMap() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderLiteByColumnName", Integer.valueOf(1));
    assertOrder1(order);
  }

  @Test
  public void testColumnsByIndex() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderLiteByColumnIndex", Integer.valueOf(1));
    assertOrder1(order);
  }

  @Test
  public void testNullValueReplacement() throws SQLException {
    Account account = (Account) sqlMap.queryForObject("getAccountViaColumnName", Integer.valueOf(5));
    assertEquals("no_email@provided.com", account.getEmailAddress());
  }

  @Test
  public void testTypeSpecified() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderWithTypes", Integer.valueOf(1));
    assertOrder1(order);
  }

  @Test
  public void testComplexObjectMapping() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderWithAccount", Integer.valueOf(1));
    assertOrder1(order);
    assertAccount1(order.getAccount());
  }

  @Test
  public void testCollectionMappingAndExtends() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderWithLineItemsCollection", Integer.valueOf(1));

    assertOrder1(order);
    assertNotNull(order.getLineItems());
    assertEquals(2, order.getLineItems().size());
  }

  @Test
  public void testListMapping() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderWithLineItems", Integer.valueOf(1));

    assertOrder1(order);
    assertNotNull(order.getLineItemsList());
    assertEquals(2, order.getLineItemsList().size());
  }

  @Test
  public void testGetAllLineItemProps() throws SQLException {
    List<?> list = sqlMap.queryForList("getAllLineItemProps", Integer.valueOf(1));

    assertNotNull(list);
    assertEquals(2, list.size());
  }

  @Test
  public void testGetSomeLineItemProps() throws SQLException {
    try {
      List<?> list = sqlMap.queryForList("getSomeLineItemProps", Integer.valueOf(1));

      fail("Expected exception because column was missing.");
    } catch (NestedSQLException e) {
      // expected
    }
  }

  @Test
  public void testArrayMapping() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderWithLineItemArray", Integer.valueOf(1));

    assertOrder1(order);
    assertNotNull(order.getLineItemArray());
    assertEquals(2, order.getLineItemArray().length);
  }

  @Test
  public void testHashMapMapping() throws SQLException {
    Map<?, ?> order = (Map<?, ?>) sqlMap.queryForObject("getOrderAsMap", Integer.valueOf(1));
    assertOrder1(order);
  }

  @Test
  public void testNestedObjects() throws SQLException {
    Order order = (Order) sqlMap.queryForObject("getOrderJoinedFavourite", Integer.valueOf(1));
    assertOrder1(order);
  }

  @Test
  public void testSimpleTypeMapping() throws SQLException {
    List<?> list = sqlMap.queryForList("getAllCreditCardNumbersFromOrders", null);

    assertEquals(5, list.size());
    assertEquals("555555555555", list.get(0));
  }

  @Test
  public void testCompositeKeyMapping() throws SQLException {

    Order order1 = (Order) sqlMap.queryForObject("getOrderWithFavouriteLineItem", Integer.valueOf(1));
    Order order2 = (Order) sqlMap.queryForObject("getOrderWithFavouriteLineItem", Integer.valueOf(2));

    assertNotNull(order1);
    assertNotNull(order1.getFavouriteLineItem());
    assertEquals(2, order1.getFavouriteLineItem().getId());
    assertEquals(1, order1.getFavouriteLineItem().getOrderId());

    assertNotNull(order2);
    assertNotNull(order2.getFavouriteLineItem());
    assertEquals(1, order2.getFavouriteLineItem().getId());
    assertEquals(2, order2.getFavouriteLineItem().getOrderId());

  }

  @Test
  public void testDynCompositeKeyMapping() throws SQLException {

    Order order1 = (Order) sqlMap.queryForObject("getOrderWithDynFavouriteLineItem", Integer.valueOf(1));

    assertNotNull(order1);
    assertNotNull(order1.getFavouriteLineItem());
    assertEquals(2, order1.getFavouriteLineItem().getId());
    assertEquals(1, order1.getFavouriteLineItem().getOrderId());

  }

  @Test
  public void testGetDoubleNestedResult() throws SQLException {
    Account account = (Account) sqlMap.queryForObject("getNestedAccountViaColumnName", Integer.valueOf(1));
    assertAccount1(account);
  }

}
