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

public class FieldAccount implements Serializable {

  private static final long serialVersionUID = 1L;
  private int id;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private FieldAccount account;

  public int id() {
    return this.id;
  }

  public void id(final int id) {
    this.id = id;
  }

  public String firstName() {
    return this.firstName;
  }

  public void firstName(final String firstName) {
    this.firstName = firstName;
  }

  public String lastName() {
    return this.lastName;
  }

  public void lastName(final String lastName) {
    this.lastName = lastName;
  }

  public String emailAddress() {
    return this.emailAddress;
  }

  public void emailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public FieldAccount account() {
    return this.account;
  }

  public void account(final FieldAccount account) {
    this.account = account;
  }
}
