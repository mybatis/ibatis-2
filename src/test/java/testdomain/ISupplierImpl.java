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

/**
 * Used in testing the ResultObjectFactory
 *
 * @author Jeff Butler
 */
public class ISupplierImpl extends ISupplierKeyImpl implements ISupplier {
  private String name;
  private String status;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String zip;
  private String phone;

  /**
   *
   */
  public ISupplierImpl() {
  }

  @Override
  public String getAddressLine1() {
    return this.addressLine1;
  }

  @Override
  public void setAddressLine1(final String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  @Override
  public String getAddressLine2() {
    return this.addressLine2;
  }

  @Override
  public void setAddressLine2(final String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  @Override
  public String getCity() {
    return this.city;
  }

  @Override
  public void setCity(final String city) {
    this.city = city;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String getPhone() {
    return this.phone;
  }

  @Override
  public void setPhone(final String phone) {
    this.phone = phone;
  }

  @Override
  public String getState() {
    return this.state;
  }

  @Override
  public void setState(final String state) {
    this.state = state;
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
  public String getZip() {
    return this.zip;
  }

  @Override
  public void setZip(final String zip) {
    this.zip = zip;
  }

}
