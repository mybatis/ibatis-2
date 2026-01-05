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

import java.math.BigDecimal;

/**
 * Used in testing the ResultObjectFactory
 *
 * @author Jeff Butler
 */
public class IItemImpl implements IItem {
  private String itemId;
  private String productId;
  private BigDecimal listPrice;
  private BigDecimal unitCost;
  private String status;
  private String attribute1;
  private String attribute2;
  private String attribute3;
  private String attribute4;
  private String attribute5;
  private ISupplier supplier;

  /**
   *
   */
  public IItemImpl() {
  }

  @Override
  public String getAttribute1() {
    return this.attribute1;
  }

  @Override
  public void setAttribute1(final String attribute1) {
    this.attribute1 = attribute1;
  }

  @Override
  public String getAttribute2() {
    return this.attribute2;
  }

  @Override
  public void setAttribute2(final String attribute2) {
    this.attribute2 = attribute2;
  }

  @Override
  public String getAttribute3() {
    return this.attribute3;
  }

  @Override
  public void setAttribute3(final String attribute3) {
    this.attribute3 = attribute3;
  }

  @Override
  public String getAttribute4() {
    return this.attribute4;
  }

  @Override
  public void setAttribute4(final String attribute4) {
    this.attribute4 = attribute4;
  }

  @Override
  public String getAttribute5() {
    return this.attribute5;
  }

  @Override
  public void setAttribute5(final String attribute5) {
    this.attribute5 = attribute5;
  }

  @Override
  public String getItemId() {
    return this.itemId;
  }

  @Override
  public void setItemId(final String itemId) {
    this.itemId = itemId;
  }

  @Override
  public BigDecimal getListPrice() {
    return this.listPrice;
  }

  @Override
  public void setListPrice(final BigDecimal listPrice) {
    this.listPrice = listPrice;
  }

  @Override
  public String getProductId() {
    return this.productId;
  }

  @Override
  public void setProductId(final String productId) {
    this.productId = productId;
  }

  @Override
  public String getStatus() {
    return this.status;
  }

  @Override
  public void setStatus(final String status) {
    this.status = status;
  }

  @Override
  public ISupplier getSupplier() {
    return this.supplier;
  }

  @Override
  public void setSupplier(final ISupplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public BigDecimal getUnitCost() {
    return this.unitCost;
  }

  @Override
  public void setUnitCost(final BigDecimal unitCost) {
    this.unitCost = unitCost;
  }

}
