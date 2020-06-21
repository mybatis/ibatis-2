/**
 * Copyright 2004-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibatis.sqlmap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import testdomain.Account;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParameterMapTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  @BeforeEach
  public void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  // PARAMETER MAP FEATURE TESTS

  @Test
  public void testNullValueReplacementMap() throws SQLException {
    Account account = newAccount6();

    sqlMap.update("insertAccountViaParameterMap", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    assertAccount6(account);
  }

  @Test
  public void testNullValueReplacementInline() throws SQLException {
    Account account = newAccount6();

    sqlMap.update("insertAccountViaInlineParameters", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    assertAccount6(account);
  }

  @Test
  public void testNullValueReplacementInlineWithDynamic() throws SQLException {
    Account account = newAccount6();

    account.setId(0);

    Exception expected = null;
    try {
      sqlMap.update("insertAccountViaInlineParametersWithDynamic", account);
    } catch (SQLException e) {
      expected = e;
    }

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(0));

    assertNotNull(expected);
    assertMessageIsNullValueNotAllowed(expected.getMessage());
    assertNull(account);
  }

  protected void assertMessageIsNullValueNotAllowed(String message) {
    assertTrue(
        message.indexOf(
            "integrity constraint violation: NOT NULL check constraint; SYS_CT_10442 table: ACCOUNT column: ACC_ID") > -1,
        "Invalid exception message");
  }

  @Test
  public void testSpecifiedType() throws SQLException {
    Account account = newAccount6();
    account.setEmailAddress(null);

    sqlMap.update("insertAccountNullableEmail", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    assertAccount6(account);
  }

  @Test
  public void testUnknownParameterClass() throws SQLException {
    Account account = newAccount6();
    account.setEmailAddress(null);

    sqlMap.update("insertAccountUknownParameterClass", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", Integer.valueOf(6));

    assertAccount6(account);
  }

  @Test
  public void testNullParameter() throws SQLException {

    Account account = (Account) sqlMap.queryForObject("getAccountNullParameter", null);

    assertNull(account);
  }

  @Test
  public void testNullParameter2() throws SQLException {

    Account account = (Account) sqlMap.queryForObject("getAccountNullParameter");

    assertNull(account);
  }
}
