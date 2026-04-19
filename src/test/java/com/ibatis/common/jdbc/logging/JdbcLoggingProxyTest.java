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
package com.ibatis.common.jdbc.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JdbcLoggingProxyTest {

  @Test
  void shouldExerciseBaseLogProxyHelpers() {
    TestableBaseLogProxy proxy = new TestableBaseLogProxy();

    proxy.set(1, "hello");
    proxy.set(2, null);

    Assertions.assertEquals("hello", proxy.get(1));
    Assertions.assertEquals("[1, 2]", proxy.columns());
    Assertions.assertEquals("[hello, null]", proxy.values());
    Assertions.assertEquals("[java.lang.String, null]", proxy.types());
    Assertions.assertEquals("a b c", proxy.removeWhitespace("a\nb\tc"));

    proxy.clear();
    Assertions.assertEquals("[]", proxy.columns());
  }

  @Test
  void shouldProxyPreparedStatementAndResultSet() throws Exception {
    ResultSetState rsState = new ResultSetState();
    rsState.values.put("name", "ibatis");
    rsState.values.put("nullable", null);
    ResultSet rawResultSet = createResultSet(rsState);

    PreparedStatementState preparedState = new PreparedStatementState();
    preparedState.executeQueryResult = rawResultSet;
    preparedState.getResultSetResult = rawResultSet;
    PreparedStatement rawPreparedStatement = createPreparedStatement(preparedState);

    PreparedStatement statement = PreparedStatementLogProxy.newInstance(rawPreparedStatement, "select *\nfrom test");

    statement.setString(1, "abc");
    statement.setNull(2, java.sql.Types.INTEGER);
    ResultSet resultSet = statement.executeQuery();
    Assertions.assertNotNull(resultSet);
    Assertions.assertTrue(Proxy.isProxyClass(resultSet.getClass()));

    Assertions.assertEquals("ibatis", resultSet.getString("name"));
    Assertions.assertNull(resultSet.getString("nullable"));
    resultSet.next();
    resultSet.close();

    ResultSet fromGetResultSet = statement.getResultSet();
    Assertions.assertNotNull(fromGetResultSet);

    Assertions.assertTrue(statement.equals(statement));
    Assertions.assertFalse(statement.equals(rawPreparedStatement));

    preparedState.throwOnExecute = true;
    Assertions.assertThrows(SQLException.class, statement::execute);
  }

  @Test
  void shouldProxyStatementAndConnection() throws Exception {
    ResultSet rawResultSet = createResultSet(new ResultSetState());

    StatementState statementState = new StatementState();
    statementState.executeQueryResult = rawResultSet;
    statementState.getResultSetResult = rawResultSet;
    Statement rawStatement = createStatement(statementState);

    PreparedStatement rawPrepared = createPreparedStatement(new PreparedStatementState());

    ConnectionState connectionState = new ConnectionState();
    connectionState.statement = rawStatement;
    connectionState.preparedStatement = rawPrepared;
    Connection rawConnection = createConnection(connectionState);

    Connection connection = ConnectionLogProxy.newInstance(rawConnection);

    Statement statement = connection.createStatement();
    Assertions.assertTrue(Proxy.isProxyClass(statement.getClass()));
    Assertions.assertNotNull(connection.prepareStatement("select 1"));
    Assertions.assertNotNull(connection.prepareCall("call abc"));

    ResultSet resultSet = statement.executeQuery("select 1");
    Assertions.assertTrue(Proxy.isProxyClass(resultSet.getClass()));
    Assertions.assertNotNull(statement.getResultSet());

    statementState.throwOnExecuteQuery = true;
    Assertions.assertThrows(SQLException.class, () -> statement.executeQuery("bad"));

    connectionState.throwOnCreateStatement = true;
    Assertions.assertThrows(SQLException.class, connection::createStatement);
  }

  private static ResultSet createResultSet(ResultSetState state) {
    InvocationHandler handler = (proxy, method, args) -> {
      String name = method.getName();
      if ("getString".equals(name) || "getObject".equals(name)) {
        Object value = state.values.get(args[0]);
        state.wasNull = value == null;
        return value;
      }
      if ("wasNull".equals(name)) {
        return state.wasNull;
      }
      if ("next".equals(name)) {
        state.nextCalls++;
        return false;
      }
      if ("close".equals(name)) {
        state.closed = true;
        return null;
      }
      if ("toString".equals(name)) {
        return "ResultSet";
      }
      return defaultValue(method.getReturnType());
    };
    return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(), new Class[] { ResultSet.class },
        handler);
  }

  private static PreparedStatement createPreparedStatement(PreparedStatementState state) {
    InvocationHandler handler = (proxy, method, args) -> {
      String name = method.getName();
      if (name.startsWith("set")) {
        state.setCalls++;
        return null;
      }
      if ("executeQuery".equals(name)) {
        if (state.throwOnExecute) {
          throw new SQLException("executeQuery failed");
        }
        return state.executeQueryResult;
      }
      if ("execute".equals(name) || "executeUpdate".equals(name)) {
        if (state.throwOnExecute) {
          throw new SQLException("execute failed");
        }
        return "executeUpdate".equals(name) ? 1 : true;
      }
      if ("getResultSet".equals(name)) {
        return state.getResultSetResult;
      }
      if ("toString".equals(name)) {
        return "PreparedStatement";
      }
      return defaultValue(method.getReturnType());
    };
    return (PreparedStatement) Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
        new Class[] { PreparedStatement.class, CallableStatement.class }, handler);
  }

  private static Statement createStatement(StatementState state) {
    InvocationHandler handler = (proxy, method, args) -> {
      String name = method.getName();
      if ("executeQuery".equals(name)) {
        if (state.throwOnExecuteQuery) {
          throw new SQLException("statement executeQuery failed");
        }
        return state.executeQueryResult;
      }
      if ("execute".equals(name)) {
        return true;
      }
      if ("executeUpdate".equals(name)) {
        return 1;
      }
      if ("getResultSet".equals(name)) {
        return state.getResultSetResult;
      }
      if ("toString".equals(name)) {
        return "Statement";
      }
      return defaultValue(method.getReturnType());
    };
    return (Statement) Proxy.newProxyInstance(Statement.class.getClassLoader(), new Class[] { Statement.class },
        handler);
  }

  private static Connection createConnection(ConnectionState state) {
    InvocationHandler handler = (proxy, method, args) -> {
      String name = method.getName();
      if ("prepareStatement".equals(name) || "prepareCall".equals(name)) {
        return state.preparedStatement;
      }
      if ("createStatement".equals(name)) {
        if (state.throwOnCreateStatement) {
          throw new SQLException("createStatement failed");
        }
        return state.statement;
      }
      if ("toString".equals(name)) {
        return "Connection";
      }
      return defaultValue(method.getReturnType());
    };
    return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
        handler);
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

  private static class ResultSetState {
    private final Map<Object, Object> values = new HashMap<>();
    private boolean wasNull;
    private boolean closed;
    private int nextCalls;
  }

  private static class PreparedStatementState {
    private ResultSet executeQueryResult;
    private ResultSet getResultSetResult;
    private boolean throwOnExecute;
    private int setCalls;
  }

  private static class StatementState {
    private ResultSet executeQueryResult;
    private ResultSet getResultSetResult;
    private boolean throwOnExecuteQuery;
  }

  private static class ConnectionState {
    private PreparedStatement preparedStatement;
    private Statement statement;
    private boolean throwOnCreateStatement;
  }

  private static class TestableBaseLogProxy extends BaseLogProxy {

    private void set(Object key, Object value) {
      setColumn(key, value);
    }

    private Object get(Object key) {
      return getColumn(key);
    }

    private String values() {
      return getValueString();
    }

    private String types() {
      return getTypeString();
    }

    private String columns() {
      return getColumnString();
    }

    private void clear() {
      clearColumnInfo();
    }

    private String removeWhitespace(String original) {
      return removeBreakingWhitespace(original);
    }
  }
}
