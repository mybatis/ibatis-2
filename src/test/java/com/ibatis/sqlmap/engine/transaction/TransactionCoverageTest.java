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
package com.ibatis.sqlmap.engine.transaction;

import com.ibatis.sqlmap.engine.transaction.external.ExternalTransaction;
import com.ibatis.sqlmap.engine.transaction.external.ExternalTransactionConfig;
import com.ibatis.sqlmap.engine.transaction.jta.JakartaTransaction;
import com.ibatis.sqlmap.engine.transaction.jta.JakartaTransactionConfig;
import com.ibatis.sqlmap.engine.transaction.jta.JavaxTransaction;
import com.ibatis.sqlmap.engine.transaction.jta.JavaxTransactionConfig;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.Status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransactionCoverageTest {

  @Test
  void shouldExerciseExternalTransactionAndConfig() throws Exception {
    ConnectionState connectionState = new ConnectionState();
    connectionState.autoCommit = true;
    connectionState.transactionIsolation = Connection.TRANSACTION_SERIALIZABLE;
    Connection connection = createConnection(connectionState);

    DataSource dataSource = createDataSource(connection);

    ExternalTransaction transaction = new ExternalTransaction(dataSource, false, true,
        Connection.TRANSACTION_READ_COMMITTED);
    Connection txConnection = transaction.getConnection();
    Assertions.assertTrue(Proxy.isProxyClass(txConnection.getClass()));
    Assertions.assertFalse(connectionState.autoCommit);

    transaction.commit();
    transaction.rollback();
    transaction.close();
    Assertions.assertTrue(connectionState.closed);

    ExternalTransactionConfig config = new ExternalTransactionConfig();
    config.setDataSource(dataSource);
    Properties props = new Properties();
    props.setProperty("DefaultAutoCommit", "true");
    props.setProperty("SetAutoCommitAllowed", "false");
    config.setProperties(props);

    Transaction configured = config.newTransaction(Connection.TRANSACTION_NONE);
    Assertions.assertNotNull(configured.getConnection());
    configured.close();

    Assertions.assertThrows(TransactionException.class,
        () -> new ExternalTransaction(null, false, true, Connection.TRANSACTION_NONE));
    Assertions.assertThrows(TransactionException.class,
        () -> new ExternalTransaction(createDataSource(null), false, true, Connection.TRANSACTION_NONE)
            .getConnection());
  }

  @Test
  void shouldExerciseJavaxTransactionAndConfig() throws Exception {
    ConnectionState connectionState = new ConnectionState();
    connectionState.autoCommit = true;
    Connection connection = createConnection(connectionState);
    DataSource dataSource = createDataSource(connection);

    SimpleJavaxUserTransaction userTransaction = new SimpleJavaxUserTransaction();
    userTransaction.status = Status.STATUS_NO_TRANSACTION;

    JavaxTransaction transaction = new JavaxTransaction(userTransaction, dataSource,
        Connection.TRANSACTION_REPEATABLE_READ);
    Assertions.assertNotNull(transaction.getConnection());
    Assertions.assertEquals(1, userTransaction.beginCalls);

    transaction.commit();
    Assertions.assertEquals(1, userTransaction.commitCalls);
    Assertions.assertThrows(TransactionException.class, transaction::commit);

    transaction.close();

    SimpleJavaxUserTransaction participatingTransaction = new SimpleJavaxUserTransaction();
    participatingTransaction.status = Status.STATUS_ACTIVE;
    JavaxTransaction participating = new JavaxTransaction(participatingTransaction, dataSource,
        Connection.TRANSACTION_NONE);
    participating.getConnection();
    participating.rollback();
    Assertions.assertEquals(1, participatingTransaction.rollbackOnlyCalls);
    participating.close();

    JavaxTransactionConfig config = new JavaxTransactionConfig();
    config.setDataSource(dataSource);
    config.setUserTransaction(new SimpleJavaxUserTransaction());
    Assertions.assertNotNull(config.newTransaction(Connection.TRANSACTION_NONE));

    Assertions.assertThrows(TransactionException.class,
        () -> new JavaxTransaction(null, dataSource, Connection.TRANSACTION_NONE));
    Assertions.assertThrows(TransactionException.class,
        () -> new JavaxTransaction(new SimpleJavaxUserTransaction(), null, Connection.TRANSACTION_NONE));
  }

  @Test
  void shouldExerciseJakartaTransactionAndConfig() throws Exception {
    ConnectionState connectionState = new ConnectionState();
    connectionState.autoCommit = true;
    Connection connection = createConnection(connectionState);
    DataSource dataSource = createDataSource(connection);

    SimpleJakartaUserTransaction userTransaction = new SimpleJakartaUserTransaction();
    userTransaction.status = jakarta.transaction.Status.STATUS_NO_TRANSACTION;

    JakartaTransaction transaction = new JakartaTransaction(userTransaction, dataSource,
        Connection.TRANSACTION_SERIALIZABLE);
    Assertions.assertNotNull(transaction.getConnection());
    Assertions.assertEquals(1, userTransaction.beginCalls);

    transaction.rollback();
    Assertions.assertEquals(1, userTransaction.rollbackCalls);
    transaction.commit();
    Assertions.assertEquals(1, userTransaction.commitCalls);
    transaction.close();

    SimpleJakartaUserTransaction participatingTransaction = new SimpleJakartaUserTransaction();
    participatingTransaction.status = jakarta.transaction.Status.STATUS_ACTIVE;
    JakartaTransaction participating = new JakartaTransaction(participatingTransaction, dataSource,
        Connection.TRANSACTION_NONE);
    participating.getConnection();
    participating.rollback();
    Assertions.assertEquals(1, participatingTransaction.rollbackOnlyCalls);
    participating.close();

    JakartaTransactionConfig config = new JakartaTransactionConfig();
    config.setDataSource(dataSource);
    config.setUserTransaction(new SimpleJakartaUserTransaction());
    Assertions.assertNotNull(config.newTransaction(Connection.TRANSACTION_NONE));

    Assertions.assertThrows(TransactionException.class,
        () -> new JakartaTransaction(null, dataSource, Connection.TRANSACTION_NONE));
    Assertions.assertThrows(TransactionException.class,
        () -> new JakartaTransaction(new SimpleJakartaUserTransaction(), null, Connection.TRANSACTION_NONE));
  }

  private static DataSource createDataSource(Connection connection) {
    return (DataSource) Proxy.newProxyInstance(DataSource.class.getClassLoader(), new Class[] { DataSource.class },
        (proxy, method, args) -> {
          if ("getConnection".equals(method.getName())) {
            return connection;
          }
          return defaultValue(method.getReturnType());
        });
  }

  private static Connection createConnection(ConnectionState state) {
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
          if ("rollback".equals(name)) {
            state.rollbackCalls++;
            return null;
          }
          if ("close".equals(name)) {
            state.closed = true;
            return null;
          }
          if ("getTransactionIsolation".equals(name)) {
            return state.transactionIsolation;
          }
          if ("setTransactionIsolation".equals(name)) {
            state.transactionIsolation = (Integer) args[0];
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

  private static class ConnectionState {
    private boolean autoCommit;
    private int transactionIsolation = Connection.TRANSACTION_NONE;
    private boolean closed;
    private int rollbackCalls;
  }

  private static class SimpleJavaxUserTransaction implements javax.transaction.UserTransaction {

    private int status = Status.STATUS_NO_TRANSACTION;
    private int beginCalls;
    private int commitCalls;
    private int rollbackCalls;
    private int rollbackOnlyCalls;

    @Override
    public void begin() {
      beginCalls++;
      status = Status.STATUS_ACTIVE;
    }

    @Override
    public void commit() {
      commitCalls++;
      status = Status.STATUS_NO_TRANSACTION;
    }

    @Override
    public void rollback() {
      rollbackCalls++;
      status = Status.STATUS_NO_TRANSACTION;
    }

    @Override
    public void setRollbackOnly() {
      rollbackOnlyCalls++;
      status = Status.STATUS_MARKED_ROLLBACK;
    }

    @Override
    public int getStatus() {
      return status;
    }

    @Override
    public void setTransactionTimeout(int seconds) {
    }
  }

  private static class SimpleJakartaUserTransaction implements jakarta.transaction.UserTransaction {

    private int status = jakarta.transaction.Status.STATUS_NO_TRANSACTION;
    private int beginCalls;
    private int commitCalls;
    private int rollbackCalls;
    private int rollbackOnlyCalls;

    @Override
    public void begin() {
      beginCalls++;
      status = jakarta.transaction.Status.STATUS_ACTIVE;
    }

    @Override
    public void commit() {
      commitCalls++;
      status = jakarta.transaction.Status.STATUS_NO_TRANSACTION;
    }

    @Override
    public void rollback() {
      rollbackCalls++;
      status = jakarta.transaction.Status.STATUS_NO_TRANSACTION;
    }

    @Override
    public void setRollbackOnly() {
      rollbackOnlyCalls++;
      status = jakarta.transaction.Status.STATUS_MARKED_ROLLBACK;
    }

    @Override
    public int getStatus() {
      return status;
    }

    @Override
    public void setTransactionTimeout(int seconds) {
    }
  }
}
