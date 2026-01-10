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
import java.util.Date;
import java.util.List;

public class Account implements Serializable {

  private static final long serialVersionUID = 1L;
  private int id;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private int[] ids;
  private int age;
  private Date dateAdded;
  private Account account;
  private List<Account> accountList;
  private boolean bannerOption;
  private boolean cartOption;

  public Account() {
  }

  public Account(final int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public int[] getIds() {
    return this.ids;
  }

  public void setIds(final int[] ids) {
    this.ids = ids;
  }

  /**
   * @return Returns the age.
   */
  public int getAge() {
    return this.age;
  }

  /**
   * @param age
   *          The age to set.
   */
  public void setAge(final int age) {
    this.age = age;
  }

  /**
   * @return Returns the dateAdded.
   */
  public Date getDateAdded() {
    return this.dateAdded;
  }

  /**
   * @param dateAdded
   *          The dateAdded to set.
   */
  public void setDateAdded(final Date dateAdded) {
    this.dateAdded = dateAdded;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public String getEmailAddress() {
    return this.emailAddress;
  }

  public void setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Account getAccount() {
    return this.account;
  }

  public void setAccount(final Account account) {
    this.account = account;
  }

  public List<Account> getAccountList() {
    return this.accountList;
  }

  public void setAccountList(final List<Account> accountList) {
    this.accountList = accountList;
  }

  public boolean isBannerOption() {
    return this.bannerOption;
  }

  public void setBannerOption(final boolean bannerOption) {
    this.bannerOption = bannerOption;
  }

  public boolean isCartOption() {
    return this.cartOption;
  }

  public void setCartOption(final boolean cartOption) {
    this.cartOption = cartOption;
  }
}
