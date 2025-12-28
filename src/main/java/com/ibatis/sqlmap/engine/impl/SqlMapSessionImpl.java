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
package com.ibatis.sqlmap.engine.impl;

import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.transaction.Transaction;
import com.ibatis.sqlmap.engine.transaction.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Implementation of SqlMapSession.
 */
public class SqlMapSessionImpl implements SqlMapSession {

  /** The delegate. */
  protected SqlMapExecutorDelegate delegate;

  /** The session scope. */
  protected SessionScope sessionScope;

  /** The closed. */
  protected boolean closed;

  /**
   * Constructor.
   *
   * @param client
   *          - the client that will use the session
   */
  public SqlMapSessionImpl(SqlMapClientImpl client) {
    this.delegate = client.getDelegate();
    this.sessionScope = this.delegate.beginSessionScope();
    this.sessionScope.setSqlMapClient(client);
    this.sessionScope.setSqlMapExecutor(client);
    this.sessionScope.setSqlMapTxMgr(client);
    this.closed = false;
  }

  /**
   * Start the session.
   */
  public void open() {
    sessionScope.setSqlMapTxMgr(this);
  }

  /**
   * Getter to tell if the session is still open.
   *
   * @return - the status of the session
   */
  public boolean isClosed() {
    return closed;
  }

  @Override
  public void close() {
    if (delegate != null && sessionScope != null) {
      delegate.endSessionScope(sessionScope);
    }
    if (sessionScope != null) {
      sessionScope = null;
    }
    if (delegate != null) {
      delegate = null;
    }
    if (!closed) {
      closed = true;
    }
  }

  @Override
  public Object insert(String id, Object param) throws SQLException {
    return delegate.insert(sessionScope, id, param);
  }

  @Override
  public Object insert(String id) throws SQLException {
    return insert(id, null);
  }

  @Override
  public int update(String id, Object param) throws SQLException {
    return delegate.update(sessionScope, id, param);
  }

  @Override
  public int update(String id) throws SQLException {
    return update(id, null);
  }

  @Override
  public int delete(String id, Object param) throws SQLException {
    return delegate.delete(sessionScope, id, param);
  }

  @Override
  public int delete(String id) throws SQLException {
    return delete(id, null);
  }

  @Override
  public Object queryForObject(String id, Object paramObject) throws SQLException {
    return delegate.queryForObject(sessionScope, id, paramObject);
  }

  @Override
  public Object queryForObject(String id) throws SQLException {
    return queryForObject(id, null);
  }

  @Override
  public Object queryForObject(String id, Object paramObject, Object resultObject) throws SQLException {
    return delegate.queryForObject(sessionScope, id, paramObject, resultObject);
  }

  @Override
  public List queryForList(String id, Object paramObject) throws SQLException {
    return delegate.queryForList(sessionScope, id, paramObject);
  }

  @Override
  public List queryForList(String id) throws SQLException {
    return queryForList(id, null);
  }

  @Override
  public List queryForList(String id, Object paramObject, int skip, int max) throws SQLException {
    return delegate.queryForList(sessionScope, id, paramObject, skip, max);
  }

  @Override
  public List queryForList(String id, int skip, int max) throws SQLException {
    return queryForList(id, null, skip, max);
  }

  /**
   * @deprecated All paginated list features have been deprecated
   */
  @Override
  @Deprecated
  public PaginatedList queryForPaginatedList(String id, Object paramObject, int pageSize) throws SQLException {
    return delegate.queryForPaginatedList(sessionScope, id, paramObject, pageSize);
  }

  /**
   * @deprecated All paginated list features have been deprecated
   */
  @Override
  @Deprecated
  public PaginatedList queryForPaginatedList(String id, int pageSize) throws SQLException {
    return queryForPaginatedList(id, null, pageSize);
  }

  @Override
  public Map queryForMap(String id, Object paramObject, String keyProp) throws SQLException {
    return delegate.queryForMap(sessionScope, id, paramObject, keyProp);
  }

  @Override
  public Map queryForMap(String id, Object paramObject, String keyProp, String valueProp) throws SQLException {
    return delegate.queryForMap(sessionScope, id, paramObject, keyProp, valueProp);
  }

  @Override
  public void queryWithRowHandler(String id, Object paramObject, RowHandler rowHandler) throws SQLException {
    delegate.queryWithRowHandler(sessionScope, id, paramObject, rowHandler);
  }

  @Override
  public void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException {
    queryWithRowHandler(id, null, rowHandler);
  }

  @Override
  public void startTransaction() throws SQLException {
    delegate.startTransaction(sessionScope);
  }

  @Override
  public void startTransaction(int transactionIsolation) throws SQLException {
    delegate.startTransaction(sessionScope, transactionIsolation);
  }

  @Override
  public void commitTransaction() throws SQLException {
    delegate.commitTransaction(sessionScope);
  }

  @Override
  public void endTransaction() throws SQLException {
    delegate.endTransaction(sessionScope);
  }

  @Override
  public void startBatch() throws SQLException {
    delegate.startBatch(sessionScope);
  }

  @Override
  public int executeBatch() throws SQLException {
    return delegate.executeBatch(sessionScope);
  }

  @Override
  public List executeBatchDetailed() throws SQLException, BatchException {
    return delegate.executeBatchDetailed(sessionScope);
  }

  @Override
  public void setUserConnection(Connection connection) throws SQLException {
    delegate.setUserProvidedTransaction(sessionScope, connection);
  }

  /**
   * Get User connection.
   *
   * @return Current connection
   *
   * @throws SQLException
   *           the SQL exception
   *
   * @deprecated Use {@link #getCurrentConnection()} instead.
   */
  @Override
  @Deprecated
  public Connection getUserConnection() throws SQLException {
    return getCurrentConnection();
  }

  @Override
  public Connection getCurrentConnection() throws SQLException {
    try {
      Connection conn = null;
      Transaction trans = delegate.getTransaction(sessionScope);
      if (trans != null) {
        conn = trans.getConnection();
      }
      return conn;
    } catch (TransactionException e) {
      throw new NestedSQLException("Error getting Connection from Transaction.  Cause: " + e, e);
    }
  }

  @Override
  public DataSource getDataSource() {
    return delegate.getDataSource();
  }

  /**
   * Gets a mapped statement by ID.
   *
   * @param id
   *          - the ID
   *
   * @return - the mapped statement
   */
  public MappedStatement getMappedStatement(String id) {
    return delegate.getMappedStatement(id);
  }

  /**
   * Get the status of lazy loading.
   *
   * @return - the status
   */
  public boolean isLazyLoadingEnabled() {
    return delegate.isLazyLoadingEnabled();
  }

  /**
   * Get the status of CGLib enhancements.
   *
   * @return - the status
   */
  public boolean isEnhancementEnabled() {
    return delegate.isEnhancementEnabled();
  }

  /**
   * Get the SQL executor.
   *
   * @return - the executor
   */
  public SqlExecutor getSqlExecutor() {
    return delegate.getSqlExecutor();
  }

  /**
   * Get the delegate.
   *
   * @return - the delegate
   */
  public SqlMapExecutorDelegate getDelegate() {
    return delegate;
  }

}
