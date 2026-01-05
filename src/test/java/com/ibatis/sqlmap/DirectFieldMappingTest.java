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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.FieldAccount;
import testdomain.PrivateAccount;

class DirectFieldMappingTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
  }

  @Test
  void testInsertAndSelectDirectToFields() throws SQLException {
    FieldAccount account = this.newFieldAccount6();

    BaseSqlMap.sqlMap.insert("insertAccountFromFields", account);

    account = (FieldAccount) BaseSqlMap.sqlMap.queryForObject("getAccountToFields", Integer.valueOf(6));

    this.assertFieldAccount6(account);
    this.assertFieldAccount6(account.account());
  }

  @Test
  void testGetAccountWithPrivateConstructor() throws SQLException {
    final FieldAccount account = this.newFieldAccount6();

    BaseSqlMap.sqlMap.insert("insertAccountFromFields", account);

    final PrivateAccount pvt = (PrivateAccount) BaseSqlMap.sqlMap.queryForObject("getAccountWithPrivateConstructor",
        Integer.valueOf(6));

    this.assertPrivateAccount6(pvt);
  }

}
