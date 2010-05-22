package com.ibatis.sqlmap;

import testdomain.Account;

import java.sql.SQLException;

/**
 * TestCase for validating PreparedStatement.setNull calls for Derby.
 * See IBATIS-536 for more information.
 */
public class DerbyParameterMapTest extends ParameterMapTest {

  // SETUP & TEARDOWN

  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/DerbySqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  // PARAMETER MAP FEATURE TESTS

  protected void assertMessageIsNullValueNotAllowed(String message) {
    assertTrue("Invalid exception message", message.indexOf("Column 'ACC_ID'  cannot accept a NULL value.") > -1);
  }
}
