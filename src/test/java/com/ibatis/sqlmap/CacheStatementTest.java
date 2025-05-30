/*
 * Copyright 2004-2025 the original author or authors.
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.engine.cache.CacheKey;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.Account;

class CacheStatementTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
  }

  @Test
  void testMappedStatementQueryWithCache() throws SQLException {
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);

    int firstId = System.identityHashCode(list);

    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);

    int secondId = System.identityHashCode(list);

    assertEquals(firstId, secondId);

    Account account = (Account) list.get(1);
    account.setEmailAddress("new.clinton@ibatis.com");
    sqlMap.update("updateAccountViaInlineParameters", account);

    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);

    int thirdId = System.identityHashCode(list);

    assertTrue(firstId != thirdId);

  }

  @Test
  void testMappedStatementQueryWithCache2() throws SQLException {
    // tests the new methods that don't require a parameter object
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap");

    int firstId = System.identityHashCode(list);

    list = sqlMap.queryForList("getCachedAccountsViaResultMap");

    int secondId = System.identityHashCode(list);

    assertEquals(firstId, secondId);

    Account account = (Account) list.get(1);
    account.setEmailAddress("new.clinton@ibatis.com");
    sqlMap.update("updateAccountViaInlineParameters", account);

    list = sqlMap.queryForList("getCachedAccountsViaResultMap");

    int thirdId = System.identityHashCode(list);

    assertTrue(firstId != thirdId);

  }

  @Test
  void testFlushDataCache() throws SQLException {
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int firstId = System.identityHashCode(list);
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int secondId = System.identityHashCode(list);
    assertEquals(firstId, secondId);
    sqlMap.flushDataCache();
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int thirdId = System.identityHashCode(list);
    assertTrue(firstId != thirdId);
  }

  @Test
  void testFlushDataCache2() throws SQLException {
    // tests the new methods that don't require a parameter object
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int firstId = System.identityHashCode(list);
    list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int secondId = System.identityHashCode(list);
    assertEquals(firstId, secondId);
    sqlMap.flushDataCache();
    list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int thirdId = System.identityHashCode(list);
    assertTrue(firstId != thirdId);
  }

  @Test
  void testFlushDataCacheOnExecute() throws SQLException {
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int firstId = System.identityHashCode(list);
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int secondId = System.identityHashCode(list);
    assertEquals(firstId, secondId);
    sqlMap.update("updateAccountViaInlineParameters", list.get(0));
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int thirdId = System.identityHashCode(list);
    assertTrue(firstId != thirdId);
  }

  @Test
  void testFlushDataCacheOnExecute2() throws SQLException {
    // tests the new methods that don't require a parameter object
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int firstId = System.identityHashCode(list);
    list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int secondId = System.identityHashCode(list);
    assertEquals(firstId, secondId);
    sqlMap.update("updateAccountViaInlineParameters", list.get(0));
    list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int thirdId = System.identityHashCode(list);
    assertTrue(firstId != thirdId);
  }

  @Test
  void testFlushDataCacheOnExecuteInBatch() throws SQLException {
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int firstId = System.identityHashCode(list);
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int secondId = System.identityHashCode(list);
    assertEquals(firstId, secondId);
    sqlMap.startBatch();
    sqlMap.update("updateAccountViaInlineParameters", list.get(0));
    sqlMap.executeBatch();
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int thirdId = System.identityHashCode(list);
    assertTrue(firstId != thirdId);
  }

  @Test
  void testFlushDataCacheOnExecuteInBatch2() throws SQLException {
    // tests the new methods that don't require a parameter object
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int firstId = System.identityHashCode(list);
    list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int secondId = System.identityHashCode(list);
    assertEquals(firstId, secondId);
    sqlMap.startBatch();
    sqlMap.update("updateAccountViaInlineParameters", list.get(0));
    sqlMap.executeBatch();
    list = sqlMap.queryForList("getCachedAccountsViaResultMap");
    int thirdId = System.identityHashCode(list);
    assertTrue(firstId != thirdId);
  }

  @Test
  void testFlushDataCacheOnExecuteInBatchWithTx() throws SQLException {
    List<?> list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int firstId = System.identityHashCode(list);
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int secondId = System.identityHashCode(list);
    assertEquals(firstId, secondId);
    try {
      sqlMap.startTransaction();
      sqlMap.startBatch();
      sqlMap.update("updateAccountViaInlineParameters", list.get(0));
      sqlMap.executeBatch();
      sqlMap.commitTransaction();
    } finally {
      sqlMap.endTransaction();
    }
    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);
    int thirdId = System.identityHashCode(list);
    assertTrue(firstId != thirdId);
  }

  @Test
  void testMappedStatementQueryWithThreadedCache() throws SQLException {

    Map<String, Object> results = new HashMap<>();

    TestCacheThread.startThread(sqlMap, results, "getCachedAccountsViaResultMap");
    Integer firstId = (Integer) results.get("id");

    TestCacheThread.startThread(sqlMap, results, "getCachedAccountsViaResultMap");
    Integer secondId = (Integer) results.get("id");

    assertTrue(firstId.equals(secondId));

    List<?> list = (List<?>) results.get("list");

    Account account = (Account) list.get(1);
    account.setEmailAddress("new.clinton@ibatis.com");
    sqlMap.update("updateAccountViaInlineParameters", account);

    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);

    int thirdId = System.identityHashCode(list);

    assertTrue(firstId.intValue() != thirdId);

  }

  @Test
  void testMappedStatementQueryWithThreadedReadWriteCache() throws SQLException {

    Map<String, Object> results = new HashMap<>();

    TestCacheThread.startThread(sqlMap, results, "getRWCachedAccountsViaResultMap");
    Integer firstId = (Integer) results.get("id");

    TestCacheThread.startThread(sqlMap, results, "getRWCachedAccountsViaResultMap");
    Integer secondId = (Integer) results.get("id");

    assertFalse(firstId.equals(secondId));

    List<?> list = (List<?>) results.get("list");

    Account account = (Account) list.get(1);
    account.setEmailAddress("new.clinton@ibatis.com");
    sqlMap.update("updateAccountViaInlineParameters", account);

    list = sqlMap.queryForList("getCachedAccountsViaResultMap", null);

    int thirdId = System.identityHashCode(list);

    assertTrue(firstId.intValue() != thirdId);

  }

  @Test
  void testCacheKeyWithSameHashcode() {
    CacheKey key1 = new CacheKey();
    CacheKey key2 = new CacheKey();

    key1.update("HS1CS001");
    key2.update("HS1D4001");

    assertEquals(key1.hashCode(), key2.hashCode(), "Expect same hashcode.");
    assertFalse(key1.equals(key2), "Expect not equal");
  }

  @Test
  void testCacheKeyWithTwoParamsSameHashcode() {
    CacheKey key1 = new CacheKey();
    CacheKey key2 = new CacheKey();

    key1.update("HS1CS001");
    key1.update("HS1D4001");

    key2.update("HS1D4001");
    key2.update("HS1CS001");

    assertEquals(key1.hashCode(), key2.hashCode(), "Expect same hashcode.");
    assertFalse(key1.equals(key2), "Expect not equal");
  }

  private static class TestCacheThread extends Thread {
    private SqlMapClient sqlMap;
    private Map<String, Object> results;
    private String statementName;

    public TestCacheThread(SqlMapClient sqlMap, Map<String, Object> results, String statementName) {
      this.sqlMap = sqlMap;
      this.results = results;
      this.statementName = statementName;
    }

    @Override
    public void run() {
      try {
        SqlMapSession session = sqlMap.openSession();
        List<?> list = session.queryForList(statementName, null);
        System.identityHashCode(list);
        list = session.queryForList(statementName, null);
        System.identityHashCode(list);
        // assertEquals(firstId, secondId);
        results.put("id", Integer.valueOf(System.identityHashCode(list)));
        results.put("list", list);
        session.close();
      } catch (SQLException e) {
        throw new RuntimeException("Error.  Cause: " + e);
      }
    }

    public static void startThread(SqlMapClient sqlMap, Map<String, Object> results, String statementName) {
      Thread t = new TestCacheThread(sqlMap, results, statementName);
      t.start();
      try {
        t.join();
      } catch (InterruptedException e) {
        throw new RuntimeException("Error.  Cause: " + e);
      }
    }
  }

}
