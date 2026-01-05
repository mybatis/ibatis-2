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
package com.ibatis.common.beans;

import com.ibatis.sqlmap.engine.accessplan.AccessPlan;
import com.ibatis.sqlmap.engine.accessplan.AccessPlanFactory;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import testdomain.Order;

class PropertyAccessPlanTest {

  private static final String[] properties = { "id", "id", "account.firstName", "account.lastName",
      "account.emailAddress", "cardType", "cardNumber", "cardExpiry", "favouriteLineItem.itemCode",
      "favouriteLineItem.quantity", "favouriteLineItem.price" };

  private static final Object[] values = { Integer.valueOf(100), Integer.valueOf(100), "Clinton", "Begin",
      "clinton@ibatis.com", "VISA", "1234567890", "05/06", "M100", Integer.valueOf(3), new BigDecimal(150) };

  @Test
  void testSetAndGetProperties() {

    final AccessPlan plan = AccessPlanFactory.getAccessPlan(Order.class, PropertyAccessPlanTest.properties);

    Order order = new Order();

    plan.setProperties(order, PropertyAccessPlanTest.values);
    this.assertOrder(order);

    final Object[] newValues = plan.getProperties(order);

    order = new Order();
    plan.setProperties(order, newValues);

    this.assertOrder(order);

  }

  private void assertOrder(final Order order) {
    Assertions.assertEquals(PropertyAccessPlanTest.values[0], Integer.valueOf(order.getId()));
    Assertions.assertEquals(PropertyAccessPlanTest.values[1], Integer.valueOf(order.getId()));
    Assertions.assertEquals(PropertyAccessPlanTest.values[2], order.getAccount().getFirstName());
    Assertions.assertEquals(PropertyAccessPlanTest.values[3], order.getAccount().getLastName());
    Assertions.assertEquals(PropertyAccessPlanTest.values[4], order.getAccount().getEmailAddress());
    Assertions.assertEquals(PropertyAccessPlanTest.values[5], order.getCardType());
    Assertions.assertEquals(PropertyAccessPlanTest.values[6], order.getCardNumber());
    Assertions.assertEquals(PropertyAccessPlanTest.values[7], order.getCardExpiry());
    Assertions.assertEquals(PropertyAccessPlanTest.values[8], order.getFavouriteLineItem().getItemCode());
    Assertions.assertEquals(PropertyAccessPlanTest.values[9],
        Integer.valueOf(order.getFavouriteLineItem().getQuantity()));
    Assertions.assertEquals(PropertyAccessPlanTest.values[10], order.getFavouriteLineItem().getPrice());
  }

}
