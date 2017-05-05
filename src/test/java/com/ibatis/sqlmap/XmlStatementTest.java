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

import xmltester.*;
import xmltester.MiniParser;

import java.sql.SQLException;

public class XmlStatementTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  @Override
  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
    initScript("scripts/order-init.sql");
    initScript("scripts/line_item-init.sql");
  }

  @Override
  protected void tearDown() throws Exception {
  }

  public void testExecuteQueryForXml() throws SQLException {
    String account = (String) sqlMap.queryForObject("getAccountXml", "<parameter><id>1</id></parameter>");
    assertNotNull(account);
    MiniDom dom = new MiniParser(account).getDom();
    assertEquals("1", dom.getValue("ID"));
    assertEquals("Clinton", dom.getValue("FIRSTNAME"));
    assertEquals("Begin", dom.getValue("LASTNAME"));
    assertEquals("clinton.begin@ibatis.com", dom.getValue("EMAILADDRESS"));
  }

  public void testExecuteQueryForXmlExternalMaps() throws SQLException {
    String account = (String) sqlMap.queryForObject("getAccountXmlExternalMaps", "<parameter><id>1</id></parameter>");
    assertNotNull(account);
    MiniDom dom = new MiniParser(account).getDom();
    assertEquals("1", dom.getValue("id"));
    assertEquals("Clinton", dom.getValue("firstName"));
    assertEquals("Begin", dom.getValue("lastName"));
    assertEquals("clinton.begin@ibatis.com", dom.getValue("emailAddress"));
    assertEquals("1", dom.getValue("account.ID"));
    assertEquals("Clinton", dom.getValue("account.FIRSTNAME"));
    assertEquals("Begin", dom.getValue("account.LASTNAME"));
    assertEquals("clinton.begin@ibatis.com", dom.getValue("account.EMAILADDRESS"));
  }

  public void testExecuteQueryForOrderXml() throws SQLException {
    String order = (String) sqlMap.queryForObject("getOrderXml", "<parameter><id>1</id></parameter>");
    assertNotNull(order);
    MiniDom dom = new MiniParser(order).getDom();
    assertEquals("1", dom.getValue("id"));
    assertEquals("2", dom.getValue("lineItems.lineItem.ID"));
  }

  public void testExecuteQueryForXmlSpecialChars() throws SQLException {
    String account = (String) sqlMap.queryForObject("getAccountXml", "<parameter><id>5</id></parameter>");
    assertNotNull(account);
    MiniDom dom = new MiniParser(account).getDom();
    assertEquals("5", dom.getValue("ID"));
    assertEquals("&manda", dom.getValue("FIRSTNAME"));
  }

}
