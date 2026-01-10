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

public class Item implements Serializable {

  private static final long serialVersionUID = 1L;
  private String itemId;
  private String productId;
  private BigDecimal listPrice;
  private BigDecimal unitCost;
  private int supplierId;
  private String status;
  private String attribute1;
  private int quantity;

  public String getItemId() {
    return this.itemId;
  }

  public void setItemId(final String itemId) {
    this.itemId = itemId;
  }

  public String getProductId() {
    return this.productId;
  }

  public void setProductId(final String productId) {
    this.productId = productId;
  }

  public BigDecimal getListPrice() {
    return this.listPrice;
  }

  public void setListPrice(final BigDecimal listPrice) {
    this.listPrice = listPrice;
  }

  public BigDecimal getUnitCost() {
    return this.unitCost;
  }

  public void setUnitCost(final BigDecimal unitCost) {
    this.unitCost = unitCost;
  }

  public int getSupplierId() {
    return this.supplierId;
  }

  public void setSupplierId(final int supplierId) {
    this.supplierId = supplierId;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public String getAttribute1() {
    return this.attribute1;
  }

  public void setAttribute1(final String attribute1) {
    this.attribute1 = attribute1;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(final int quantity) {
    this.quantity = quantity;
  }
}
