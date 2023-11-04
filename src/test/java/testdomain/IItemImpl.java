/*
 * Copyright 2004-2023 the original author or authors.
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
    super();
  }

  @Override
  public String getAttribute1() {
    return attribute1;
  }

  @Override
  public void setAttribute1(String attribute1) {
    this.attribute1 = attribute1;
  }

  @Override
  public String getAttribute2() {
    return attribute2;
  }

  @Override
  public void setAttribute2(String attribute2) {
    this.attribute2 = attribute2;
  }

  @Override
  public String getAttribute3() {
    return attribute3;
  }

  @Override
  public void setAttribute3(String attribute3) {
    this.attribute3 = attribute3;
  }

  @Override
  public String getAttribute4() {
    return attribute4;
  }

  @Override
  public void setAttribute4(String attribute4) {
    this.attribute4 = attribute4;
  }

  @Override
  public String getAttribute5() {
    return attribute5;
  }

  @Override
  public void setAttribute5(String attribute5) {
    this.attribute5 = attribute5;
  }

  @Override
  public String getItemId() {
    return itemId;
  }

  @Override
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  @Override
  public BigDecimal getListPrice() {
    return listPrice;
  }

  @Override
  public void setListPrice(BigDecimal listPrice) {
    this.listPrice = listPrice;
  }

  @Override
  public String getProductId() {
    return productId;
  }

  @Override
  public void setProductId(String productId) {
    this.productId = productId;
  }

  @Override
  public String getStatus() {
    return status;
  }

  @Override
  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public ISupplier getSupplier() {
    return supplier;
  }

  @Override
  public void setSupplier(ISupplier supplier) {
    this.supplier = supplier;
  }

  @Override
  public BigDecimal getUnitCost() {
    return unitCost;
  }

  @Override
  public void setUnitCost(BigDecimal unitCost) {
    this.unitCost = unitCost;
  }

}
