package com.ibatis.common.beans;

import com.ibatis.sqlmap.engine.accessplan.AccessPlan;
import com.ibatis.sqlmap.engine.accessplan.AccessPlanFactory;
import junit.framework.TestCase;
import testdomain.Order;

import java.math.BigDecimal;

public class PropertyAccessPlanTest extends TestCase {

  private static final String[] properties = {
    "id",
    "id",
    "account.firstName",
    "account.lastName",
    "account.emailAddress",
    "cardType",
    "cardNumber",
    "cardExpiry",
    "favouriteLineItem.itemCode",
    "favouriteLineItem.quantity",
    "favouriteLineItem.price"
  };

  private static final Object[] values = {
    new Integer(100),
    new Integer(100),
    "Clinton",
    "Begin",
    "clinton@ibatis.com",
    "VISA",
    "1234567890",
    "05/06",
    "M100",
    new Integer(3),
    new BigDecimal(150)
  };


  public void testSetAndGetProperties() {

    AccessPlan plan = AccessPlanFactory.getAccessPlan(Order.class, properties);

    Order order = new Order();

    plan.setProperties(order, values);
    assertOrder(order);

    Object[] newValues = plan.getProperties(order);

    order = new Order();
    plan.setProperties(order, newValues);

    assertOrder(order);

  }

  private void assertOrder(Order order) {
    assertEquals(values[0], new Integer(order.getId()));
    assertEquals(values[1], new Integer(order.getId()));
    assertEquals(values[2], order.getAccount().getFirstName());
    assertEquals(values[3], order.getAccount().getLastName());
    assertEquals(values[4], order.getAccount().getEmailAddress());
    assertEquals(values[5], order.getCardType());
    assertEquals(values[6], order.getCardNumber());
    assertEquals(values[7], order.getCardExpiry());
    assertEquals(values[8], order.getFavouriteLineItem().getItemCode());
    assertEquals(values[9], new Integer(order.getFavouriteLineItem().getQuantity()));
    assertEquals(values[10], order.getFavouriteLineItem().getPrice());
  }

}
