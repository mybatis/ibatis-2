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
package com.ibatis.sqlmap;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;

class ParameterMapTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
  }

  // PARAMETER MAP FEATURE TESTS

  @Test
  void testNullValueReplacementMap() throws SQLException {
    Account account = this.newAccount6();

    BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);

    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    this.assertAccount6(account);
  }

  @Test
  void testNullValueReplacementInline() throws SQLException {
    Account account = this.newAccount6();

    BaseSqlMap.sqlMap.update("insertAccountViaInlineParameters", account);

    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    this.assertAccount6(account);
  }

  @Test
  void testNullValueReplacementInlineWithDynamic() throws SQLException {
    Account account = this.newAccount6();

    account.setId(0);

    Exception expected = null;
    try {
      BaseSqlMap.sqlMap.update("insertAccountViaInlineParametersWithDynamic", account);
    } catch (final SQLException e) {
      expected = e;
    }

    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(0));

    Assertions.assertNotNull(expected);
    this.assertMessageIsNullValueNotAllowed(expected.getMessage());
    Assertions.assertNull(account);
  }

  protected void assertMessageIsNullValueNotAllowed(final String message) {
    Assertions.assertTrue(message.indexOf("integrity constraint violation: NOT NULL check constraint") > -1
        && message.indexOf("ACCOUNT column: ACC_ID") > -1, "Invalid exception message");
  }

  @Test
  void testSpecifiedType() throws SQLException {
    Account account = this.newAccount6();
    account.setEmailAddress(null);

    BaseSqlMap.sqlMap.update("insertAccountNullableEmail", account);

    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    this.assertAccount6(account);
  }

  @Test
  void testUnknownParameterClass() throws SQLException {
    Account account = this.newAccount6();
    account.setEmailAddress(null);

    BaseSqlMap.sqlMap.update("insertAccountUknownParameterClass", account);

    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    this.assertAccount6(account);
  }

  @Test
  void testNullParameter() throws SQLException {

    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullParameter", null);

    Assertions.assertNull(account);
  }

  @Test
  void testNullParameter2() throws SQLException {

    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullParameter");

    Assertions.assertNull(account);
  }
}
