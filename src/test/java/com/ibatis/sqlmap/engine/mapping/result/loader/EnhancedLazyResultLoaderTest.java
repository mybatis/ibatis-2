/**
 * Copyright 2004-2020 the original author or authors.
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
package com.ibatis.sqlmap.engine.mapping.result.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.Test;

import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.result.loader.test.Bean1;
import com.ibatis.sqlmap.engine.mapping.result.loader.test.Bean2;

public class EnhancedLazyResultLoaderTest {

  /**
   * Test if a method in a proxied object can access a default method of another class in the same package with default
   * access modifier.
   * <p>
   * Depending of the implementation of the Cglib-proxy the access will throw an IllegalAccessException.
   */
  @Test
  public void testProxyMethodAccess() throws SQLException {
    SqlMapClientImpl client = setupMockSqlMapClientImpl();

    EnhancedLazyResultLoader loader = new EnhancedLazyResultLoader(client, "bean2", null, Bean2.class);
    Bean2 bean2 = (Bean2) loader.loadResult();

    // cglib might throw an IllegalAccessException
    bean2.testDefaultAccess();
  }

  /**
   * Test if a proxy for a null nevertheless dispatch to a default object.
   */
  @Test
  public void testNullProxy() throws SQLException {
    SqlMapClientImpl client = setupMockSqlMapClientImpl();

    EnhancedLazyResultLoader loader = new EnhancedLazyResultLoader(client, "bean3", null, TestBean3.class);
    TestBean3 bean = (TestBean3) loader.loadResult();

    assertEquals("foobar", bean.getText());
  }

  /**
   * Mock a {@link SqlMapClientImpl} which returns a {@link Bean1} for the id <code>bean1</code> and {@link Bean2} for
   * id <code>bean2</code>.
   */
  private SqlMapClientImpl setupMockSqlMapClientImpl() {
    SqlMapExecutorDelegate delegate = new SqlMapExecutorDelegate();
    SqlMapClientImpl client = new SqlMapClientImpl(delegate) {
      @Override
      public Object queryForObject(String id, Object paramObject) throws SQLException {
        if ("bean1".equals(id)) {
          return new Bean1();
        } else if ("bean2".equals(id)) {
          Bean2 bean = new Bean2();
          EnhancedLazyResultLoader loader = new EnhancedLazyResultLoader(this, "bean1", null, Bean1.class);
          bean.setBean1((Bean1) loader.loadResult());
          return bean;
        } else if ("bean3".equals(id)) {
          return null;
        }
        fail();
        return null;
      }
    };
    return client;
  }

  public static class TestBean3 {

    public String getText() {
      return "foobar";
    }

  }
}
