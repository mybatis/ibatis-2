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

import xmltester.MiniDom;
import xmltester.MiniParser;

class XmlStatementTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
    BaseSqlMap.initScript("scripts/order-init.sql");
    BaseSqlMap.initScript("scripts/line_item-init.sql");
  }

  @Test
  void testExecuteQueryForXml() throws SQLException {
    final String account = (String) BaseSqlMap.sqlMap.queryForObject("getAccountXml",
        "<parameter><id>1</id></parameter>");
    Assertions.assertNotNull(account);
    final MiniDom dom = new MiniParser(account).getDom();
    Assertions.assertEquals("1", dom.getValue("ID"));
    Assertions.assertEquals("Clinton", dom.getValue("FIRSTNAME"));
    Assertions.assertEquals("Begin", dom.getValue("LASTNAME"));
    Assertions.assertEquals("clinton.begin@ibatis.com", dom.getValue("EMAILADDRESS"));
  }

  @Test
  void testExecuteQueryForXmlExternalMaps() throws SQLException {
    final String account = (String) BaseSqlMap.sqlMap.queryForObject("getAccountXmlExternalMaps",
        "<parameter><id>1</id></parameter>");
    Assertions.assertNotNull(account);
    final MiniDom dom = new MiniParser(account).getDom();
    Assertions.assertEquals("1", dom.getValue("id"));
    Assertions.assertEquals("Clinton", dom.getValue("firstName"));
    Assertions.assertEquals("Begin", dom.getValue("lastName"));
    Assertions.assertEquals("clinton.begin@ibatis.com", dom.getValue("emailAddress"));
    Assertions.assertEquals("1", dom.getValue("account.ID"));
    Assertions.assertEquals("Clinton", dom.getValue("account.FIRSTNAME"));
    Assertions.assertEquals("Begin", dom.getValue("account.LASTNAME"));
    Assertions.assertEquals("clinton.begin@ibatis.com", dom.getValue("account.EMAILADDRESS"));
  }

  @Test
  void testExecuteQueryForOrderXml() throws SQLException {
    final String order = (String) BaseSqlMap.sqlMap.queryForObject("getOrderXml", "<parameter><id>1</id></parameter>");
    Assertions.assertNotNull(order);
    final MiniDom dom = new MiniParser(order).getDom();
    Assertions.assertEquals("1", dom.getValue("id"));
    Assertions.assertEquals("2", dom.getValue("lineItems.lineItem.ID"));
  }

  @Test
  void testExecuteQueryForXmlSpecialChars() throws SQLException {
    final String account = (String) BaseSqlMap.sqlMap.queryForObject("getAccountXml",
        "<parameter><id>5</id></parameter>");
    Assertions.assertNotNull(account);
    final MiniDom dom = new MiniParser(account).getDom();
    Assertions.assertEquals("5", dom.getValue("ID"));
    Assertions.assertEquals("&manda", dom.getValue("FIRSTNAME"));
  }

}
