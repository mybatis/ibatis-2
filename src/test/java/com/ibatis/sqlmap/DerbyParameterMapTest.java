/**
 * Copyright 2004-2017 the original author or authors.
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

/**
 * TestCase for validating PreparedStatement.setNull calls for Derby. See IBATIS-536 for more information.
 */
public class DerbyParameterMapTest extends ParameterMapTest {

  // SETUP & TEARDOWN

  @Override
  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/DerbySqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  // PARAMETER MAP FEATURE TESTS

  @Override
  protected void assertMessageIsNullValueNotAllowed(String message) {
    assertTrue("Invalid exception message", message.indexOf("Column 'ACC_ID'  cannot accept a NULL value.") > -1);
  }
}
