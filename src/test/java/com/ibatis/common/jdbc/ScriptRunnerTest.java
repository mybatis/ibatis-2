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
package com.ibatis.common.jdbc;

import java.io.StringReader;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScriptRunnerTest {

  @Test
  void shouldExecuteScriptWithResultsAndRestoreAutoCommit() throws Exception {
    MockConnectionState state = new MockConnectionState();
    state.autoCommit = true;
    Connection connection = createConnection(state);

    ScriptRunner runner = new ScriptRunner(connection, false, true);
    runner.runScript(new StringReader("-- comment\nSELECT 1;\n"));

    Assertions.assertEquals(1, state.executeCalls);
    Assertions.assertTrue(state.commitCalls > 0);
    Assertions.assertTrue(state.rollbackCalls > 0);
    Assertions.assertTrue(state.statementClosed);
    Assertions.assertTrue(state.autoCommit);
  }

  @Test
  void shouldContinueWhenStopOnErrorIsFalse() throws Exception {
    MockConnectionState state = new MockConnectionState();
    state.autoCommit = false;
    state.throwOnExecute = true;
    Connection connection = createConnection(state);

    ScriptRunner runner = new ScriptRunner(connection, false, false);
    runner.setDelimiter("GO", true);

    runner.runScript(new StringReader("SELECT 1\nGO\n"));

    Assertions.assertEquals(1, state.executeCalls);
    Assertions.assertTrue(state.rollbackCalls > 0);
  }

  @Test
  void shouldWrapDriverInstantiationErrors() {
    ScriptRunner runner = new ScriptRunner("no.such.Driver", "jdbc:invalid", "u", "p", true, true);

    RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
        () -> runner.runScript(new StringReader("SELECT 1;")));

    Assertions.assertTrue(exception.getMessage().startsWith("Error running script."));
  }

  private static Connection createConnection(MockConnectionState state) {
    Statement statement = createStatement(state);
    return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
        (proxy, method, args) -> {
          String name = method.getName();
          if ("getAutoCommit".equals(name)) {
            return state.autoCommit;
          }
          if ("setAutoCommit".equals(name)) {
            state.autoCommit = (Boolean) args[0];
            return null;
          }
          if ("createStatement".equals(name)) {
            return statement;
          }
          if ("commit".equals(name)) {
            state.commitCalls++;
            return null;
          }
          if ("rollback".equals(name)) {
            state.rollbackCalls++;
            return null;
          }
          return defaultValue(method.getReturnType());
        });
  }

  private static Statement createStatement(MockConnectionState state) {
    ResultSetMetaData metaData = (ResultSetMetaData) Proxy.newProxyInstance(ResultSetMetaData.class.getClassLoader(),
        new Class[] { ResultSetMetaData.class }, (proxy, method, args) -> {
          if ("getColumnCount".equals(method.getName())) {
            return 1;
          }
          if ("getColumnLabel".equals(method.getName())) {
            return "COL1";
          }
          return defaultValue(method.getReturnType());
        });

    ResultSet resultSet = (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
        new Class[] { ResultSet.class }, (proxy, method, args) -> {
          if ("getMetaData".equals(method.getName())) {
            return metaData;
          }
          if ("next".equals(method.getName())) {
            boolean next = !state.resultRowReturned;
            state.resultRowReturned = true;
            return next;
          }
          if ("getString".equals(method.getName())) {
            return "1";
          }
          return defaultValue(method.getReturnType());
        });

    return (Statement) Proxy.newProxyInstance(Statement.class.getClassLoader(), new Class[] { Statement.class },
        (proxy, method, args) -> {
          String name = method.getName();
          if ("execute".equals(name)) {
            state.executeCalls++;
            if (state.throwOnExecute) {
              throw new SQLException("boom");
            }
            return true;
          }
          if ("getResultSet".equals(name)) {
            return resultSet;
          }
          if ("close".equals(name)) {
            state.statementClosed = true;
            return null;
          }
          return defaultValue(method.getReturnType());
        });
  }

  private static Object defaultValue(Class<?> returnType) {
    if (!returnType.isPrimitive()) {
      return null;
    }
    if (returnType == boolean.class) {
      return false;
    }
    if (returnType == int.class) {
      return 0;
    }
    if (returnType == long.class) {
      return 0L;
    }
    if (returnType == double.class) {
      return 0D;
    }
    if (returnType == float.class) {
      return 0F;
    }
    if (returnType == short.class) {
      return (short) 0;
    }
    if (returnType == byte.class) {
      return (byte) 0;
    }
    if (returnType == char.class) {
      return (char) 0;
    }
    return null;
  }

  private static class MockConnectionState {
    private boolean autoCommit;
    private boolean throwOnExecute;
    private boolean statementClosed;
    private boolean resultRowReturned;
    private int executeCalls;
    private int commitCalls;
    private int rollbackCalls;
  }
}
