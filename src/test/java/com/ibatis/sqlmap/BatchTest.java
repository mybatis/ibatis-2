/*
 * Copyright 2004-2023 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.execution.BatchResult;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;

/**
 * @author Jeff Butler
 */
class BatchTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  @Test
  void testExecutebatchDetailed() {
    List<Account> accountList1 = new ArrayList<Account>();
    Account account = new Account();
    account.setId(10);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(11);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(12);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(13);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(14);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    List<Account> accountList2 = new ArrayList<Account>();
    account = new Account();
    account.setId(15);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(16);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(17);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(18);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    try {
      sqlMap.startTransaction();
      sqlMap.startBatch();

      // insert 5 accounts
      for (int i = 0; i < accountList1.size(); i++) {
        sqlMap.insert("insertAccountViaInlineParameters", accountList1.get(i));
      }

      // update 1 account
      account = new Account();
      account.setId(10);
      account.setFirstName("barney");
      account.setLastName("rubble");
      account.setEmailAddress("barney.rubble@gmail.com");

      sqlMap.update("updateAccountViaInlineParameters", account);

      // insert 4 accounts
      for (int i = 0; i < accountList2.size(); i++) {
        sqlMap.insert("insertAccountViaInlineParameters", accountList2.get(i));
      }

      List<?> results = sqlMap.executeBatchDetailed();
      sqlMap.commitTransaction();

      assertEquals(3, results.size());

      BatchResult br = (BatchResult) results.get(0);
      assertEquals(5, br.getUpdateCounts().length);

      br = (BatchResult) results.get(1);
      assertEquals(1, br.getUpdateCounts().length);

      br = (BatchResult) results.get(2);
      assertEquals(4, br.getUpdateCounts().length);

    } catch (BatchException e) {
      fail(e.getMessage());
    } catch (SQLException e) {
      fail(e.getMessage());
    } finally {
      try {
        sqlMap.endTransaction();
      } catch (SQLException e) {
        fail(e.getMessage());
      }
    }
  }

  @Test
  void testExecutebatchDetailedWithError() {
    List<Account> accountList1 = new ArrayList<Account>();
    Account account = new Account();
    account.setId(10);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(11);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(12);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(13);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(14);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    List<Account> accountList2 = new ArrayList<Account>();
    account = new Account();
    account.setId(15);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(16);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(17);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(18);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    try {
      sqlMap.startTransaction();
      sqlMap.startBatch();

      // insert 5 accounts
      for (int i = 0; i < accountList1.size(); i++) {
        sqlMap.insert("insertAccountViaInlineParameters", accountList1.get(i));
      }

      // update 1 account
      account = new Account();
      account.setId(10);
      account.setFirstName("barney");
      account.setLastName("rubble");
      account.setEmailAddress("barney.rubble@gmail.com");

      sqlMap.update("updateAccountViaInlineParameters", account);

      // insert another account
      account = new Account();
      account.setId(19);
      account.setFirstName("fred");
      account.setLastName("flintstone");
      account.setEmailAddress("fred.flintstone@gmail.com");
      sqlMap.insert("insertAccountViaInlineParameters", account);

      // insert 1 account with all null values (this should cause an error when the batch is
      // executed)
      account = new Account();
      sqlMap.insert("insertAccountViaInlineParameters", account);

      // update 1 account
      account = new Account();
      account.setId(11);
      account.setFirstName("barney");
      account.setLastName("rubble");
      account.setEmailAddress("barney.rubble@gmail.com");

      sqlMap.update("updateAccountViaInlineParameters", account);

      // insert 4 accounts
      for (int i = 0; i < accountList2.size(); i++) {
        sqlMap.insert("insertAccountViaInlineParameters", accountList2.get(i));
      }

      sqlMap.executeBatchDetailed();
      fail("This statement should not get executed - we expect an SQLException");
    } catch (BatchException e) {
      // the first statement of the failing batch should have executed OK
      BatchUpdateException bue = e.getBatchUpdateException();
      assertEquals(1, bue.getUpdateCounts().length);

      List<?> results = e.getSuccessfulBatchResults();
      assertEquals(2, results.size());
      BatchResult br = (BatchResult) results.get(0);
      assertEquals(5, br.getUpdateCounts().length);
      br = (BatchResult) results.get(1);
      assertEquals(1, br.getUpdateCounts().length);
    } catch (SQLException e) {
      fail(e.getMessage());
    } finally {
      try {
        sqlMap.endTransaction();
      } catch (SQLException e) {
        fail(e.getMessage());
      }
    }
  }

  @Test
  void testExecutebatch() {
    List<Account> accountList1 = new ArrayList<Account>();
    Account account = new Account();
    account.setId(10);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(11);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(12);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(13);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    account = new Account();
    account.setId(14);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList1.add(account);

    List<Account> accountList2 = new ArrayList<Account>();
    account = new Account();
    account.setId(15);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(16);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(17);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    account = new Account();
    account.setId(18);
    account.setFirstName("fred");
    account.setLastName("flintstone");
    account.setEmailAddress("fred.flintstone@gmail.com");
    accountList2.add(account);

    try {
      sqlMap.startTransaction();
      sqlMap.startBatch();

      // insert 5 accounts
      for (int i = 0; i < accountList1.size(); i++) {
        sqlMap.insert("insertAccountViaInlineParameters", accountList1.get(i));
      }

      // update 1 account
      account = new Account();
      account.setId(10);
      account.setFirstName("barney");
      account.setLastName("rubble");
      account.setEmailAddress("barney.rubble@gmail.com");

      sqlMap.update("updateAccountViaInlineParameters", account);

      // insert 4 accounts
      for (int i = 0; i < accountList2.size(); i++) {
        sqlMap.insert("insertAccountViaInlineParameters", accountList2.get(i));
      }

      int results = sqlMap.executeBatch();
      sqlMap.commitTransaction();

      assertEquals(10, results);
    } catch (SQLException e) {
      fail(e.getMessage());
    } finally {
      try {
        sqlMap.endTransaction();
      } catch (SQLException e) {
        fail(e.getMessage());
      }
    }
  }
}
