/**
 * Copyright 2004-2015 the original author or authors.
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
package threads;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import com.ibatis.sqlmap.client.SqlMapClient;
import threads.Foo;

class MyThread extends Thread {

  private SqlMapClient sqlMap;
  private String remap;

  public MyThread(SqlMapClient sqlMap, String remap) {
    this.remap = remap;
    this.sqlMap = sqlMap;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void run() {
    while (true) {
      try {
        List<Foo> list = sqlMap.queryForList("selectFoo" + remap, null);
        TestCase.assertEquals(300, list.size());
        check(list);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static void check(List<Foo> list) {
    for (Foo foo : list) {
      if (foo == null) {
        TestCase.fail("list contained a null element");
      }
    }
  }
}
