package com.ibatis.sqlmap;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.SQLException;

public class DomStatementTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
    initScript("scripts/order-init.sql");
    initScript("scripts/line_item-init.sql");
  }

  protected void tearDown() throws Exception {
  }


  public void testExecuteQueryForDom() throws SQLException {
    Document account = (Document) sqlMap.queryForObject("getAccountDom", newParameter("1"));
    assertNotNull(account);

    Probe dom = ProbeFactory.getProbe(account);

    assertEquals("1", dom.getObject(account, "ID"));
    assertEquals("Clinton", dom.getObject(account, "FIRSTNAME"));
    assertEquals("Begin", dom.getObject(account, "LASTNAME"));
    assertEquals("clinton.begin@ibatis.com", dom.getObject(account, "EMAILADDRESS"));
  }

  public void testExecuteQueryForDomSpecialChars() throws SQLException {
    Document account = (Document) sqlMap.queryForObject("getAccountDom", newParameter("5"));
    assertNotNull(account);

    Probe dom = ProbeFactory.getProbe(account);

    assertEquals("5", dom.getObject(account, "ID"));
    assertEquals("&manda", dom.getObject(account, "FIRSTNAME"));
  }

  public void testExecuteQueryForDomExternalMaps() throws SQLException {
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

  public void testExecuteQueryForOrderDom() throws SQLException {

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
