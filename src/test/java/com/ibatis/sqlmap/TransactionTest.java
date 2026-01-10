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

class TransactionTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
  }

  // TRANSACTION TESTS

  @Test
  void testStartCommitTransaction() throws SQLException {
    Account account = this.newAccount6();

    try {
      BaseSqlMap.sqlMap.startTransaction();
      BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);
      BaseSqlMap.sqlMap.commitTransaction();
    } finally {
      BaseSqlMap.sqlMap.endTransaction();
    }

    // This will use autocommit...
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    this.assertAccount6(account);
  }

  @Test
  void testTransactionAlreadyStarted() throws SQLException {
    Account account = this.newAccount6();
    boolean exceptionThrownAsExpected = false;

    try {
      BaseSqlMap.sqlMap.startTransaction();
      BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);

      try {
        BaseSqlMap.sqlMap.startTransaction(); // transaction already started
      } catch (final SQLException e) {
        exceptionThrownAsExpected = true;
      }

      BaseSqlMap.sqlMap.commitTransaction();
    } finally {
      BaseSqlMap.sqlMap.endTransaction();
    }

    // This will use autocommit...
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    this.assertAccount6(account);
    Assertions.assertTrue(exceptionThrownAsExpected);
  }

  @Test
  void testNoTransactionStarted() throws SQLException {
    Account account = this.newAccount6();

    BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);

    boolean exceptionThrownAsExpected = false;
    try {
      BaseSqlMap.sqlMap.commitTransaction(); // No transaction started
    } catch (final SQLException e) {
      exceptionThrownAsExpected = true;
    }

    // This will use autocommit...
    Assertions.assertTrue(exceptionThrownAsExpected);
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    this.assertAccount6(account);
  }

  @Test
  void testTransactionFailed() throws SQLException {
    Account account = this.newAccount6();

    boolean exceptionThrownAsExpected = false;
    try {
      BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", null);
    } catch (final SQLException e) {
      exceptionThrownAsExpected = true;
    }

    BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);

    // This will use autocommit...
    Assertions.assertTrue(exceptionThrownAsExpected);
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    this.assertAccount6(account);
  }

  @Test
  void testTransactionFailed2() throws SQLException {
    // testes method that does not require a parameter object
    Account account = this.newAccount6();

    boolean exceptionThrownAsExpected = false;
    try {
      BaseSqlMap.sqlMap.update("insertAccountViaParameterMap");
    } catch (final SQLException e) {
      exceptionThrownAsExpected = true;
    }

    BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);

    // This will use autocommit...
    Assertions.assertTrue(exceptionThrownAsExpected);
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    this.assertAccount6(account);
  }

  @Test
  void testStartRollbackTransaction() throws SQLException {
    Account account = this.newAccount6();

    try {
      BaseSqlMap.sqlMap.startTransaction();
      BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);
      // sqlMap.commitTransaction();
    } finally {
      BaseSqlMap.sqlMap.endTransaction();
    }

    // This will use autocommit...
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    Assertions.assertNull(account);
  }

  // AUTOCOMMIT TESTS

  @Test
  void testAutoCommitUpdate() throws SQLException {
    Account account = this.newAccount6();
    BaseSqlMap.sqlMap.update("insertAccountViaParameterMap", account);
    account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    this.assertAccount6(account);
  }

  @Test
  void testAutoCommitQuery() throws SQLException {
    final Account account = (Account) BaseSqlMap.sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(1));
    this.assertAccount1(account);
  }

}
