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

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.FieldAccount;
import testdomain.PrivateAccount;

public class DirectFieldMappingTest extends BaseSqlMapTest {

  @BeforeEach
  public void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  @Test
  public void testInsertAndSelectDirectToFields() throws SQLException {
    FieldAccount account = newFieldAccount6();

    sqlMap.insert("insertAccountFromFields", account);

    account = (FieldAccount) sqlMap.queryForObject("getAccountToFields", Integer.valueOf(6));

    assertFieldAccount6(account);
    assertFieldAccount6(account.account());
  }

  @Test
  public void testGetAccountWithPrivateConstructor() throws SQLException {
    FieldAccount account = newFieldAccount6();

    sqlMap.insert("insertAccountFromFields", account);

    PrivateAccount pvt = (PrivateAccount) sqlMap.queryForObject("getAccountWithPrivateConstructor", Integer.valueOf(6));

    assertPrivateAccount6(pvt);
  }

}
