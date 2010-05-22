package threads;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.*;
import junit.framework.TestCase;

import java.io.*;
import java.util.*;
import java.sql.SQLException;

public class RemapResultsThreadTest extends TestCase {

  public void testWithRemap() throws Exception {
    runTest("WithRemap");
  }

  public void testWithoutRemap() throws Exception {
    runTest("WithoutRemap");
  }

  private void runTest(String statementToRun) throws IOException, SQLException {
    String resource = "threads/sql-map-config.xml";
    Reader reader = Resources.getResourceAsReader(resource);
    SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);

    sqlMap.update("create");

    int count = 2;

    List<MyThread> threads = new LinkedList<MyThread>();

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
