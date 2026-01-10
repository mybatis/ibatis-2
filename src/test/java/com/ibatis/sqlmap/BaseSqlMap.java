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

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.Reader;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;

import testdomain.Account;
import testdomain.FieldAccount;
import testdomain.Order;
import testdomain.PrivateAccount;

public class BaseSqlMap {

  protected static SqlMapClient sqlMap;

  protected static void initSqlMap(final String configFile, final Properties props) throws Exception {
    final Reader reader = Resources.getResourceAsReader(configFile);
    BaseSqlMap.sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader, props);
    reader.close();
  }

  protected static void initScript(final String script) throws Exception {
    final DataSource ds = BaseSqlMap.sqlMap.getDataSource();

    final Connection conn = ds.getConnection();

    final Reader reader = Resources.getResourceAsReader(script);

    final ScriptRunner runner = new ScriptRunner(conn, false, false);
    runner.setLogWriter(null);
    runner.setErrorLogWriter(null);

    runner.runScript(reader);
    conn.commit();
    conn.close();
    reader.close();
  }

  protected Account newAccount6() {
    final Account account = new Account();
    account.setId(6);
    account.setFirstName("Jennifer");
    account.setLastName("Begin");
    account.setEmailAddress("no_email@provided.com");
    account.setBannerOption(true);
    account.setCartOption(true);
    return account;
  }

  protected FieldAccount newFieldAccount6() {
    final FieldAccount account = new FieldAccount();
    account.id(6);
    account.firstName("Jennifer");
    account.lastName("Begin");
    account.emailAddress("no_email@provided.com");
    return account;
  }

  protected void assertAccount1(final Account account) {
    Assertions.assertEquals(1, account.getId());
    Assertions.assertEquals("Clinton", account.getFirstName());
    Assertions.assertEquals("Begin", account.getLastName());
    Assertions.assertEquals("clinton.begin@ibatis.com", account.getEmailAddress());
  }

  protected void assertAccount2(final Account account) {
    Assertions.assertEquals(2, account.getId());
    Assertions.assertEquals("Jim", account.getFirstName());
    Assertions.assertEquals("Smith", account.getLastName());
    Assertions.assertEquals(account.getEmailAddress(), "jim.smith@somewhere.com");
  }

  protected void assertList(final List<?> list) {
    Assertions.assertEquals(2, list.size());
  }

  protected void assertAccount6(final Account account) {
    Assertions.assertEquals(6, account.getId());
    Assertions.assertEquals("Jennifer", account.getFirstName());
    Assertions.assertEquals("Begin", account.getLastName());
    Assertions.assertNull(account.getEmailAddress());
  }

  protected void assertPrivateAccount6(final PrivateAccount account) {
    Assertions.assertEquals(6, account.getId());
    Assertions.assertEquals("Jennifer", account.getFirstName());
    Assertions.assertEquals("Begin", account.getLastName());
    Assertions.assertNull(account.getEmailAddress());
  }

  protected void assertFieldAccount6(final FieldAccount account) {
    Assertions.assertEquals(6, account.id());
    Assertions.assertEquals("Jennifer", account.firstName());
    Assertions.assertEquals("Begin", account.lastName());
    Assertions.assertNull(account.emailAddress());
  }

  protected void assertAccount1(final Map<?, ?> account) {
    Integer id = (Integer) account.get("id");
    String firstName = (String) account.get("firstName");
    String lastName = (String) account.get("lastName");
    String emailAddress = (String) account.get("emailAddress");

    if (id == null) {
      id = (Integer) account.get("ID");
      firstName = (String) account.get("FIRSTNAME");
      lastName = (String) account.get("LASTNAME");
      emailAddress = (String) account.get("EMAILADDRESS");
    }

    Assertions.assertEquals(Integer.valueOf(1), id);
    Assertions.assertEquals("Clinton", firstName);
    Assertions.assertEquals("Begin", lastName);
    Assertions.assertEquals("clinton.begin@ibatis.com", emailAddress);
  }

  protected void assertOrder1(final Order order) {
    final Calendar cal = new GregorianCalendar(2003, 1, 15, 8, 15, 00);

    Assertions.assertEquals(1, order.getId());
    Assertions.assertEquals(cal.getTime().getTime(), order.getDate().getTime());
    Assertions.assertEquals("VISA", order.getCardType());
    Assertions.assertEquals("999999999999", order.getCardNumber());
    Assertions.assertEquals("05/03", order.getCardExpiry());
    Assertions.assertEquals("11 This Street", order.getStreet());
    Assertions.assertEquals("Victoria", order.getCity());
    Assertions.assertEquals("BC", order.getProvince());
    Assertions.assertEquals("C4B 4F4", order.getPostalCode());
  }

  protected void assertOrder1(final Map<?, ?> order) {
    final Calendar cal = new GregorianCalendar(2003, 1, 15, 8, 15, 00);

    Assertions.assertEquals(Integer.valueOf(1), order.get("id"));
    Assertions.assertEquals(cal.getTime().getTime(), ((Date) order.get("date")).getTime());
    Assertions.assertEquals("VISA", order.get("cardType"));
    Assertions.assertEquals("999999999999", order.get("cardNumber"));
    Assertions.assertEquals("05/03", order.get("cardExpiry"));
    Assertions.assertEquals("11 This Street", order.get("street"));
    Assertions.assertEquals("Victoria", order.get("city"));
    Assertions.assertEquals("BC", order.get("province"));
    Assertions.assertEquals("C4B 4F4", order.get("postalCode"));
  }

}
