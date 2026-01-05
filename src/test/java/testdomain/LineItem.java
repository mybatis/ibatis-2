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
package testdomain;

import java.io.Serializable;
import java.math.BigDecimal;

public class LineItem implements Serializable {

  private static final long serialVersionUID = 1L;
  private int id;
  private int orderId;
  private String itemCode;
  private int quantity;
  private BigDecimal price;

  public int getId() {
    return this.id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public int getOrderId() {
    return this.orderId;
  }

  public void setOrderId(final int orderId) {
    this.orderId = orderId;
  }

  public String getItemCode() {
    return this.itemCode;
  }

  public void setItemCode(final String itemCode) {
    this.itemCode = itemCode;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(final int quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return this.price;
  }

  public void setPrice(final BigDecimal price) {
    this.price = price;
  }

}
