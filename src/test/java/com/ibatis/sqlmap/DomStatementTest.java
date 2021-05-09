/*
 * Copyright 2004-2021 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;

import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class DomStatementTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
    initScript("scripts/order-init.sql");
    initScript("scripts/line_item-init.sql");
  }

  @Test
  void testExecuteQueryForDom() throws SQLException {
    Document account = (Document) sqlMap.queryForObject("getAccountDom", newParameter("1"));
    assertNotNull(account);

    Probe dom = ProbeFactory.getProbe(account);

    assertEquals("1", dom.getObject(account, "ID"));
    assertEquals("Clinton", dom.getObject(account, "FIRSTNAME"));
    assertEquals("Begin", dom.getObject(account, "LASTNAME"));
    assertEquals("clinton.begin@ibatis.com", dom.getObject(account, "EMAILADDRESS"));
  }

  @Test
  void testExecuteQueryForDomSpecialChars() throws SQLException {
    Document account = (Document) sqlMap.queryForObject("getAccountDom", newParameter("5"));
    assertNotNull(account);

    Probe dom = ProbeFactory.getProbe(account);

    assertEquals("5", dom.getObject(account, "ID"));
    assertEquals("&manda", dom.getObject(account, "FIRSTNAME"));
  }

  @Test
  void testExecuteQueryForDomExternalMaps() throws SQLException {
    Document account = (Document) sqlMap.queryForObject("getAccountDomExternalMaps", newParameter("1"));
    assertNotNull(account);

    Probe dom = ProbeFactory.getProbe(account);

    assertEquals("1", dom.getObject(account, "id"));
    assertEquals("Clinton", dom.getObject(account, "firstName"));
    assertEquals("Begin", dom.getObject(account, "lastName"));
    assertEquals("clinton.begin@ibatis.com", dom.getObject(account, "emailAddress"));
    assertEquals("1", dom.getObject(account, "account.ID"));
    assertEquals("Clinton", dom.getObject(account, "account.FIRSTNAME"));
    assertEquals("Begin", dom.getObject(account, "account.LASTNAME"));
    assertEquals("clinton.begin@ibatis.com", dom.getObject(account, "account.EMAILADDRESS"));
  }

  @Test
  void testExecuteQueryForOrderDom() throws SQLException {

    Document order = (Document) sqlMap.queryForObject("getOrderDom", newParameter("1"));
    assertNotNull(order);

    Probe dom = ProbeFactory.getProbe(order);

    assertEquals("1", dom.getObject(order, "id"));
    assertEquals("2", dom.getObject(order, "lineItems.lineItem[1].ID"));
  }

  private Document newParameter(String val) {
    Document param = newDocument("parameter");
    Probe dom = ProbeFactory.getProbe(param);
    dom.setObject(param, "id", val);
    return param;
  }

  private Document newDocument(String root) {
    try {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      doc.appendChild(doc.createElement(root));
      return doc;
    } catch (ParserConfigurationException e) {
      throw new RuntimeException("Error creating XML document.  Cause: " + e);
    }
  }

}
