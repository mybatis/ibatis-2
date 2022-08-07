/*
 * Copyright 2004-2021 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;

class TransactionTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  // TRANSACTION TESTS

  @Test
  void testStartCommitTransaction() throws SQLException {
    Account account = newAccount6();

    try {
      sqlMap.startTransaction();
      sqlMap.update("insertAccountViaParameterMap", account);
      sqlMap.commitTransaction();
    } finally {
      sqlMap.endTransaction();
    }

    // This will use autocommit...
    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    assertAccount6(account);
  }

  @Test
  void testTransactionAlreadyStarted() throws SQLException {
    Account account = newAccount6();
    boolean exceptionThrownAsExpected = false;

    try {
      sqlMap.startTransaction();
      sqlMap.update("insertAccountViaParameterMap", account);

      try {
        sqlMap.startTransaction(); // transaction already started
      } catch (SQLException e) {
        exceptionThrownAsExpected = true;
      }

      sqlMap.commitTransaction();
    } finally {
      sqlMap.endTransaction();
    }

    // This will use autocommit...
    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    assertAccount6(account);
    assertTrue(exceptionThrownAsExpected);
  }

  @Test
  void testNoTransactionStarted() throws SQLException {
    Account account = newAccount6();

    sqlMap.update("insertAccountViaParameterMap", account);

    boolean exceptionThrownAsExpected = false;
    try {
      sqlMap.commitTransaction(); // No transaction started
    } catch (SQLException e) {
      exceptionThrownAsExpected = true;
    }

    // This will use autocommit...
    assertTrue(exceptionThrownAsExpected);
    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    assertAccount6(account);
  }

  @Test
  void testTransactionFailed() throws SQLException {
    Account account = newAccount6();

    boolean exceptionThrownAsExpected = false;
    try {
      sqlMap.update("insertAccountViaParameterMap", null);
    } catch (SQLException e) {
      exceptionThrownAsExpected = true;
    }

    sqlMap.update("insertAccountViaParameterMap", account);

    // This will use autocommit...
    assertTrue(exceptionThrownAsExpected);
    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    assertAccount6(account);
  }

  @Test
  void testTransactionFailed2() throws SQLException {
    // testes method that does not require a parameter object
    Account account = newAccount6();

    boolean exceptionThrownAsExpected = false;
    try {
      sqlMap.update("insertAccountViaParameterMap");
    } catch (SQLException e) {
      exceptionThrownAsExpected = true;
    }

    sqlMap.update("insertAccountViaParameterMap", account);

    // This will use autocommit...
    assertTrue(exceptionThrownAsExpected);
    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    assertAccount6(account);
  }

  @Test
  void testStartRollbackTransaction() throws SQLException {
    Account account = newAccount6();

    try {
      sqlMap.startTransaction();
      sqlMap.update("insertAccountViaParameterMap", account);
      // sqlMap.commitTransaction();
    } finally {
      sqlMap.endTransaction();
    }

    // This will use autocommit...
    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    assertNull(account);
  }

  // AUTOCOMMIT TESTS

  @Test
  void testAutoCommitUpdate() throws SQLException {
    Account account = newAccount6();
    sqlMap.update("insertAccountViaParameterMap", account);
    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));
    assertAccount6(account);
  }

  @Test
  void testAutoCommitQuery() throws SQLException {
    Account account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(1));
    assertAccount1(account);
  }

}
