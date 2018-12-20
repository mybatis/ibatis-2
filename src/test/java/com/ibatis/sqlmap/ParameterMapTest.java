/**
 * Copyright 2004-2018 the original author or authors.
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

import testdomain.Account;

import java.sql.SQLException;

public class ParameterMapTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  @Override
  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  @Override
  protected void tearDown() throws Exception {
  }

  // PARAMETER MAP FEATURE TESTS

  public void testNullValueReplacementMap() throws SQLException {
    Account account = newAccount6();

    sqlMap.update("insertAccountViaParameterMap", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", new Integer(6));

    assertAccount6(account);
  }

  public void testNullValueReplacementInline() throws SQLException {
    Account account = newAccount6();

    sqlMap.update("insertAccountViaInlineParameters", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", new Integer(6));

    assertAccount6(account);
  }

  public void testNullValueReplacementInlineWithDynamic() throws SQLException {
    Account account = newAccount6();

    account.setId(0);

    Exception expected = null;
    try {
      sqlMap.update("insertAccountViaInlineParametersWithDynamic", account);
    } catch (SQLException e) {
      expected = e;
    }

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", new Integer(0));

    assertNotNull(expected);
    assertMessageIsNullValueNotAllowed(expected.getMessage());
    assertNull(account);
  }

  protected void assertMessageIsNullValueNotAllowed(String message) {
    assertTrue("Invalid exception message", message
        .indexOf("Attempt to insert null into a non-nullable column: column: ACC_ID table: ACCOUNT in statement") > -1);
  }

  public void testSpecifiedType() throws SQLException {
    Account account = newAccount6();
    account.setEmailAddress(null);

    sqlMap.update("insertAccountNullableEmail", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", new Integer(6));

    assertAccount6(account);
  }

  public void testUnknownParameterClass() throws SQLException {
    Account account = newAccount6();
    account.setEmailAddress(null);

    sqlMap.update("insertAccountUknownParameterClass", account);

    account = (Account) sqlMap.queryForObject("getAccountNullableEmail", new Integer(6));

    assertAccount6(account);
  }

  public void testNullParameter() throws SQLException {

    Account account = (Account) sqlMap.queryForObject("getAccountNullParameter", null);

    assertNull(account);
  }

  public void testNullParameter2() throws SQLException {

    Account account = (Account) sqlMap.queryForObject("getAccountNullParameter");

    assertNull(account);
  }
}
