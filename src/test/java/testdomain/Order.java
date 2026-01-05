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
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

  private static final long serialVersionUID = 1L;
  private int id;
  private Account account;
  private Date date;
  private String cardType;
  private String cardNumber;
  private String cardExpiry;
  private String street;
  private String city;
  private String province;
  private String postalCode;
  private Collection<?> lineItems;
  private LineItem[] lineItemArray;
  private LineItem favouriteLineItem;

  public int getId() {
    return this.id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public Account getAccount() {
    return this.account;
  }

  public void setAccount(final Account account) {
    this.account = account;
  }

  public Date getDate() {
    return this.date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }

  public String getCardType() {
    return this.cardType;
  }

  public void setCardType(final String cardType) {
    this.cardType = cardType;
  }

  public String getCardNumber() {
    return this.cardNumber;
  }

  public void setCardNumber(final String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getCardExpiry() {
    return this.cardExpiry;
  }

  public void setCardExpiry(final String cardExpiry) {
    this.cardExpiry = cardExpiry;
  }

  public String getStreet() {
    return this.street;
  }

  public void setStreet(final String street) {
    this.street = street;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public String getProvince() {
    return this.province;
  }

  public void setProvince(final String province) {
    this.province = province;
  }

  public String getPostalCode() {
    return this.postalCode;
  }

  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  public List<?> getLineItemsList() {
    return (List<?>) this.lineItems;
  }

  public void setLineItemsList(final List<?> lineItems) {
    this.lineItems = lineItems;
  }

  public Collection<?> getLineItems() {
    return this.lineItems;
  }

  public void setLineItems(final Collection<?> lineItems) {
    this.lineItems = lineItems;
  }

  public LineItem getFavouriteLineItem() {
    return this.favouriteLineItem;
  }

  public void setFavouriteLineItem(final LineItem favouriteLineItem) {
    this.favouriteLineItem = favouriteLineItem;
  }

  public LineItem[] getLineItemArray() {
    return this.lineItemArray;
  }

  public void setLineItemArray(final LineItem[] lineItemArray) {
    this.lineItemArray = lineItemArray;
  }

}
