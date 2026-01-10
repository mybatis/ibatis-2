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

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;

import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class DomStatementTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    BaseSqlMap.initScript("scripts/account-init.sql");
    BaseSqlMap.initScript("scripts/order-init.sql");
    BaseSqlMap.initScript("scripts/line_item-init.sql");
  }

  @Test
  void testExecuteQueryForDom() throws SQLException {
    final Document account = (Document) BaseSqlMap.sqlMap.queryForObject("getAccountDom", this.newParameter("1"));
    Assertions.assertNotNull(account);

    final Probe dom = ProbeFactory.getProbe(account);

    Assertions.assertEquals("1", dom.getObject(account, "ID"));
    Assertions.assertEquals("Clinton", dom.getObject(account, "FIRSTNAME"));
    Assertions.assertEquals("Begin", dom.getObject(account, "LASTNAME"));
    Assertions.assertEquals("clinton.begin@ibatis.com", dom.getObject(account, "EMAILADDRESS"));
  }

  @Test
  void testExecuteQueryForDomSpecialChars() throws SQLException {
    final Document account = (Document) BaseSqlMap.sqlMap.queryForObject("getAccountDom", this.newParameter("5"));
    Assertions.assertNotNull(account);

    final Probe dom = ProbeFactory.getProbe(account);

    Assertions.assertEquals("5", dom.getObject(account, "ID"));
    Assertions.assertEquals("&manda", dom.getObject(account, "FIRSTNAME"));
  }

  @Test
  void testExecuteQueryForDomExternalMaps() throws SQLException {
    final Document account = (Document) BaseSqlMap.sqlMap.queryForObject("getAccountDomExternalMaps",
        this.newParameter("1"));
    Assertions.assertNotNull(account);

    final Probe dom = ProbeFactory.getProbe(account);

    Assertions.assertEquals("1", dom.getObject(account, "id"));
    Assertions.assertEquals("Clinton", dom.getObject(account, "firstName"));
    Assertions.assertEquals("Begin", dom.getObject(account, "lastName"));
    Assertions.assertEquals("clinton.begin@ibatis.com", dom.getObject(account, "emailAddress"));
    Assertions.assertEquals("1", dom.getObject(account, "account.ID"));
    Assertions.assertEquals("Clinton", dom.getObject(account, "account.FIRSTNAME"));
    Assertions.assertEquals("Begin", dom.getObject(account, "account.LASTNAME"));
    Assertions.assertEquals("clinton.begin@ibatis.com", dom.getObject(account, "account.EMAILADDRESS"));
  }

  @Test
  void testExecuteQueryForOrderDom() throws SQLException {

    final Document order = (Document) BaseSqlMap.sqlMap.queryForObject("getOrderDom", this.newParameter("1"));
    Assertions.assertNotNull(order);

    final Probe dom = ProbeFactory.getProbe(order);

    Assertions.assertEquals("1", dom.getObject(order, "id"));
    Assertions.assertEquals("2", dom.getObject(order, "lineItems.lineItem[1].ID"));
  }

  private Document newParameter(final String val) {
    final Document param = this.newDocument("parameter");
    final Probe dom = ProbeFactory.getProbe(param);
    dom.setObject(param, "id", val);
    return param;
  }

  private Document newDocument(final String root) {
    try {
      final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      doc.appendChild(doc.createElement(root));
      return doc;
    } catch (final ParserConfigurationException e) {
      throw new RuntimeException("Error creating XML document.  Cause: " + e);
    }
  }

}
