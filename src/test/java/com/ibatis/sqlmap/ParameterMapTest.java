package com.ibatis.sqlmap;

import testdomain.Account;

import java.sql.SQLException;

public class ParameterMapTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

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
    assertTrue("Invalid exception message",
               message.indexOf("Attempt to insert null into a non-nullable column: column: ACC_ID table: ACCOUNT in statement") > -1);
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

    Account account = (Account)sqlMap.queryForObject("getAccountNullParameter", null);


    assertNull(account);
  }

  public void testNullParameter2() throws SQLException {

    Account account = (Account)sqlMap.queryForObject("getAccountNullParameter");


    assertNull(account);
  }
}
