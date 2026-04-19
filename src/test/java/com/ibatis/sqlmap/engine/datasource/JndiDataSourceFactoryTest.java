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
package com.ibatis.sqlmap.engine.datasource;

import com.ibatis.sqlmap.client.SqlMapException;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JndiDataSourceFactoryTest {

  @AfterEach
  void cleanup() {
    System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
    MockInitialContextFactory.reset();
  }

  @Test
  void shouldResolveDataSourceWithContextProperties() {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

    DataSource expected = createDataSource();
    MockInitialContextFactory.bind("jdbc/main", expected);

    Map<String, String> properties = new HashMap<>();
    properties.put("context.java.naming.factory.url.pkgs", "example.package");
    properties.put("DataSource", "jdbc/main");

    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    factory.initialize(properties);

    Assertions.assertSame(expected, factory.getDataSource());
    Assertions.assertEquals("example.package",
        MockInitialContextFactory.lastEnvironment.getProperty("java.naming.factory.url.pkgs"));
  }

  @Test
  void shouldResolveLegacyJndiKeys() {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

    DataSource first = createDataSource();
    DataSource second = createDataSource();
    DataSource third = createDataSource();
    MockInitialContextFactory.bind("legacy/context", first);
    MockInitialContextFactory.bind("legacy/full", second);

    Context nested = MockInitialContextFactory.contextWith(Map.of("legacy/lookup", third));
    MockInitialContextFactory.bind("legacy/init", nested);

    JndiDataSourceFactory factory = new JndiDataSourceFactory();

    factory.initialize(Map.of("DBJndiContext", "legacy/context"));
    Assertions.assertSame(first, factory.getDataSource());

    factory.initialize(Map.of("DBFullJndiContext", "legacy/full"));
    Assertions.assertSame(second, factory.getDataSource());

    factory.initialize(Map.of("DBInitialContext", "legacy/init", "DBLookup", "legacy/lookup"));
    Assertions.assertSame(third, factory.getDataSource());
  }

  @Test
  void shouldWrapNamingException() {
    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());

    JndiDataSourceFactory factory = new JndiDataSourceFactory();
    SqlMapException exception = Assertions.assertThrows(SqlMapException.class,
        () -> factory.initialize(Map.of("DataSource", "missing")));

    Assertions.assertTrue(exception.getMessage().contains("JndiDataSourceTransactionPool"));
  }

  private static DataSource createDataSource() {
    return (DataSource) Proxy.newProxyInstance(DataSource.class.getClassLoader(), new Class[] { DataSource.class },
        (proxy, method, args) -> null);
  }

  public static class MockInitialContextFactory implements InitialContextFactory {

    private static final Map<String, Object> BINDINGS = new HashMap<>();
    private static Properties lastEnvironment;

    static void bind(String key, Object value) {
      BINDINGS.put(key, value);
    }

    static Context contextWith(Map<String, Object> bindings) {
      return (Context) Proxy.newProxyInstance(Context.class.getClassLoader(), new Class[] { Context.class },
          (proxy, method, args) -> {
            if ("lookup".equals(method.getName())) {
              String name = (String) args[0];
              if (!bindings.containsKey(name)) {
                throw new NamingException("Name not found: " + name);
              }
              return bindings.get(name);
            }
            if ("close".equals(method.getName())) {
              return null;
            }
            return null;
          });
    }

    static void reset() {
      BINDINGS.clear();
      lastEnvironment = null;
    }

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) {
      if (environment != null) {
        lastEnvironment = new Properties();
        for (Map.Entry<?, ?> entry : environment.entrySet()) {
          lastEnvironment.put(entry.getKey(), entry.getValue());
        }
      } else {
        lastEnvironment = new Properties();
      }
      return contextWith(BINDINGS);
    }
  }
}
