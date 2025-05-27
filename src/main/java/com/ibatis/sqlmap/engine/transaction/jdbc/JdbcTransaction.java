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
package com.ibatis.sqlmap.engine.transaction.jdbc;

import com.ibatis.common.jdbc.logging.ConnectionLogProxy;
import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import com.ibatis.sqlmap.engine.transaction.IsolationLevel;
import com.ibatis.sqlmap.engine.transaction.Transaction;
import com.ibatis.sqlmap.engine.transaction.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * The Class JdbcTransaction.
 */
public class JdbcTransaction implements Transaction {

  /** The Constant connectionLog. */
  private static final Log connectionLog = LogFactory.getLog(Connection.class);

  /** The data source. */
  private DataSource dataSource;

  /** The connection. */
  private Connection connection;

  /** The isolation level. */
  private IsolationLevel isolationLevel = new IsolationLevel();

  /**
   * Instantiates a new jdbc transaction.
   *
   * @param ds
   *          the ds
   * @param isolationLevel
   *          the isolation level
   *
   * @throws TransactionException
   *           the transaction exception
   */
  public JdbcTransaction(DataSource ds, int isolationLevel) throws TransactionException {
    // Check Parameters
    dataSource = ds;
    if (dataSource == null) {
      throw new TransactionException("JdbcTransaction initialization failed.  DataSource was null.");
    }
    this.isolationLevel.setIsolationLevel(isolationLevel);
  }

  /**
   * Inits the.
   *
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  private void init() throws SQLException, TransactionException {
    // Open JDBC Transaction
    connection = dataSource.getConnection();
    if (connection == null) {
      throw new TransactionException(
          "JdbcTransaction could not start transaction.  Cause: The DataSource returned a null connection.");
    }
    // Isolation Level
    isolationLevel.applyIsolationLevel(connection);
    // AutoCommit
    if (connection.getAutoCommit()) {
      connection.setAutoCommit(false);
    }
    // Debug
    if (connectionLog.isDebugEnabled()) {
      connection = ConnectionLogProxy.newInstance(connection);
    }
  }

  @Override
  public void commit() throws SQLException, TransactionException {
    if (connection != null) {
      connection.commit();
    }
  }

  @Override
  public void rollback() throws SQLException, TransactionException {
    if (connection != null) {
      connection.rollback();
    }
  }

  @Override
  public void close() throws SQLException, TransactionException {
    if (connection != null) {
      try {
        isolationLevel.restoreIsolationLevel(connection);
      } finally {
        connection.close();
        connection = null;
      }
    }
  }

  @Override
  public Connection getConnection() throws SQLException, TransactionException {
    if (connection == null) {
      init();
    }
    return connection;
  }

}
