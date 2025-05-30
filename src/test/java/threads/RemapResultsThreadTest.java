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
package threads;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

class RemapResultsThreadTest {

  @Test
  void testWithRemap() throws Exception {
    runTest("WithRemap");
  }

  @Test
  void testWithoutRemap() throws Exception {
    runTest("WithoutRemap");
  }

  private void runTest(String statementToRun) throws IOException, SQLException {
    String resource = "threads/sql-map-config.xml";
    Reader reader = Resources.getResourceAsReader(resource);
    SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);

    sqlMap.update("drop");
    sqlMap.update("create");

    for (int i = 0; i < 100; i++) {
      sqlMap.update("inserta");
      sqlMap.update("insertb");
      sqlMap.update("insertc");
    }

    int count = 2;

    List<MyThread> threads = new LinkedList<>();

    for (int i = 0; i < count; i++) {
      MyThread thread = new MyThread(sqlMap, statementToRun);
      thread.start();
      threads.add(thread);
    }

    Date d1 = new Date(new Date().getTime() + 10000);

    // let's do the test for 10 seconds - for me it failed quite early
    while (new Date().before(d1)) {
      for (MyThread myThread : threads) {
        assertTrue(myThread.isAlive());
      }
    }
  }

}
