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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ibatis.common.resources.Resources;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MultiResultSetTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/DerbySqlMapConfig.xml",
        Resources.getResourceAsProperties("com/ibatis/sqlmap/maps/DerbySqlMapConfig.properties"));
    initScript("scripts/account-init.sql");
    initScript("scripts/derby-proc-init.sql");

  }

  @Test
  void testShouldRetrieveTwoSetsOfTwoAccountsFromMultipleResultMaps() throws Exception {
    final Map<String, Integer> persons = new HashMap<>();
    persons.put("1", Integer.valueOf(1));
    persons.put("2", Integer.valueOf(2));
    persons.put("3", Integer.valueOf(3));
    persons.put("4", Integer.valueOf(4));
    final List<?> results = sqlMap.queryForList("getMultiListsRm", persons);
    assertEquals(2, results.size());
    assertEquals(2, ((List<?>) results.get(0)).size());
    assertEquals(2, ((List<?>) results.get(1)).size());
  }

  @Test
  void testShouldRetrieveTwoSetsOfTwoAccountsFromMultipleResultClasses() throws Exception {
    final Map<String, Integer> persons = new HashMap<>();
    persons.put("1", Integer.valueOf(1));
    persons.put("2", Integer.valueOf(2));
    persons.put("3", Integer.valueOf(3));
    persons.put("4", Integer.valueOf(4));
    final List<?> results = sqlMap.queryForList("getMultiListsRc", persons);
    assertEquals(2, results.size());
    assertEquals(2, ((List<?>) results.get(0)).size());
    assertEquals(2, ((List<?>) results.get(1)).size());
  }

  @Test
  void testCallableStatementShouldReturnTwoResultSets() throws Exception {
    sqlMap.startTransaction();
    final Connection conn = sqlMap.getCurrentConnection();
    final CallableStatement cs = conn.prepareCall("{call MRESULTSET(?,?,?,?)}");
    cs.setInt(1, 1);
    cs.setInt(2, 2);
    cs.setInt(3, 3);
    cs.setInt(4, 4);
    cs.execute();
    final ResultSet rs = cs.getResultSet();
    assertNotNull(rs);
    int found = 1;
    while (cs.getMoreResults()) {
      assertNotNull(cs.getResultSet());
      found++;
    }
    rs.close();
    cs.close();
    assertEquals(2, found, "Didn't find second result set.");
  }

}
