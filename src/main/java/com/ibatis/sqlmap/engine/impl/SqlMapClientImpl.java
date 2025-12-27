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

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Implementation of ExtendedSqlMapClient.
 */
public class SqlMapClientImpl implements SqlMapClient, ExtendedSqlMapClient {

  /** The Constant log. */
  private static final Log log = LogFactory.getLog(SqlMapClientImpl.class);

  /** Delegate for SQL execution. */
  public SqlMapExecutorDelegate delegate;

  /** The local sql map session. */
  protected ThreadLocal<SqlMapSessionImpl> localSqlMapSession = new ThreadLocal<>();

  /**
   * Constructor to supply a delegate.
   *
   * @param delegate
   *          - the delegate
   */
  public SqlMapClientImpl(SqlMapExecutorDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object insert(String id, Object param) throws SQLException {
    return getLocalSqlMapSession().insert(id, param);
  }

  @Override
  public Object insert(String id) throws SQLException {
    return getLocalSqlMapSession().insert(id);
  }

  @Override
  public int update(String id, Object param) throws SQLException {
    return getLocalSqlMapSession().update(id, param);
  }

  @Override
  public int update(String id) throws SQLException {
    return getLocalSqlMapSession().update(id);
  }

  @Override
  public int delete(String id, Object param) throws SQLException {
    return getLocalSqlMapSession().delete(id, param);
  }

  @Override
  public int delete(String id) throws SQLException {
    return getLocalSqlMapSession().delete(id);
  }

  @Override
  public Object queryForObject(String id, Object paramObject) throws SQLException {
    return getLocalSqlMapSession().queryForObject(id, paramObject);
  }

  @Override
  public Object queryForObject(String id) throws SQLException {
    return getLocalSqlMapSession().queryForObject(id);
  }

  @Override
  public Object queryForObject(String id, Object paramObject, Object resultObject) throws SQLException {
    return getLocalSqlMapSession().queryForObject(id, paramObject, resultObject);
  }

  @Override
  public List queryForList(String id, Object paramObject) throws SQLException {
    return getLocalSqlMapSession().queryForList(id, paramObject);
  }

  @Override
  public List queryForList(String id) throws SQLException {
    return getLocalSqlMapSession().queryForList(id);
  }

  @Override
  public List queryForList(String id, Object paramObject, int skip, int max) throws SQLException {
    return getLocalSqlMapSession().queryForList(id, paramObject, skip, max);
  }

  @Override
  public List queryForList(String id, int skip, int max) throws SQLException {
    return getLocalSqlMapSession().queryForList(id, skip, max);
  }

  /**
   * @deprecated All paginated list features have been deprecated
   */
  @Override
  @Deprecated
  public PaginatedList queryForPaginatedList(String id, Object paramObject, int pageSize) throws SQLException {
    return getLocalSqlMapSession().queryForPaginatedList(id, paramObject, pageSize);
  }

  /**
   * @deprecated All paginated list features have been deprecated
   */
  @Override
  @Deprecated
  public PaginatedList queryForPaginatedList(String id, int pageSize) throws SQLException {
    return getLocalSqlMapSession().queryForPaginatedList(id, pageSize);
  }

  @Override
  public Map queryForMap(String id, Object paramObject, String keyProp) throws SQLException {
    return getLocalSqlMapSession().queryForMap(id, paramObject, keyProp);
  }

  @Override
  public Map queryForMap(String id, Object paramObject, String keyProp, String valueProp) throws SQLException {
    return getLocalSqlMapSession().queryForMap(id, paramObject, keyProp, valueProp);
  }

  @Override
  public void queryWithRowHandler(String id, Object paramObject, RowHandler rowHandler) throws SQLException {
    getLocalSqlMapSession().queryWithRowHandler(id, paramObject, rowHandler);
  }

  @Override
  public void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException {
    getLocalSqlMapSession().queryWithRowHandler(id, rowHandler);
  }

  @Override
  public void startTransaction() throws SQLException {
    getLocalSqlMapSession().startTransaction();
  }

  @Override
  public void startTransaction(int transactionIsolation) throws SQLException {
    getLocalSqlMapSession().startTransaction(transactionIsolation);
  }

  @Override
  public void commitTransaction() throws SQLException {
    getLocalSqlMapSession().commitTransaction();
  }

  @Override
  public void endTransaction() throws SQLException {
    try {
      getLocalSqlMapSession().endTransaction();
    } finally {
      getLocalSqlMapSession().close();
      localSqlMapSession.remove();
    }
  }

  @Override
  public void startBatch() throws SQLException {
    getLocalSqlMapSession().startBatch();
  }

  @Override
  public int executeBatch() throws SQLException {
    return getLocalSqlMapSession().executeBatch();
  }

  @Override
  public List executeBatchDetailed() throws SQLException, BatchException {
    return getLocalSqlMapSession().executeBatchDetailed();
  }

  @Override
  public void setUserConnection(Connection connection) throws SQLException {
    try {
      getLocalSqlMapSession().setUserConnection(connection);
    } finally {
      if (connection == null) {
        getLocalSqlMapSession().close();
      }
    }
  }

  /**
   * Get user connection.
   *
   * @return Current connection
   *
   * @throws SQLException
   *           the SQL exception
   *
   * @deprecated Use getCurrentconnection() instead.
   */
  @Override
  @Deprecated
  public Connection getUserConnection() throws SQLException {
    return getCurrentConnection();
  }

  @Override
  public Connection getCurrentConnection() throws SQLException {
    return getLocalSqlMapSession().getCurrentConnection();
  }

  @Override
  public DataSource getDataSource() {
    return delegate.getDataSource();
  }

  @Override
  public MappedStatement getMappedStatement(String id) {
    return delegate.getMappedStatement(id);
  }

  @Override
  public boolean isLazyLoadingEnabled() {
    return delegate.isLazyLoadingEnabled();
  }

  @Override
  public boolean isEnhancementEnabled() {
    return delegate.isEnhancementEnabled();
  }

  @Override
  public SqlExecutor getSqlExecutor() {
    return delegate.getSqlExecutor();
  }

  @Override
  public SqlMapExecutorDelegate getDelegate() {
    return delegate;
  }

  @Override
  public SqlMapSession openSession() {
    SqlMapSessionImpl sqlMapSession = new SqlMapSessionImpl(this);
    sqlMapSession.open();
    return sqlMapSession;
  }

  @Override
  public SqlMapSession openSession(Connection conn) {
    try {
      SqlMapSessionImpl sqlMapSession = new SqlMapSessionImpl(this);
      sqlMapSession.open();
      sqlMapSession.setUserConnection(conn);
      return sqlMapSession;
    } catch (SQLException e) {
      throw new SqlMapException("Error setting user provided connection.  Cause: " + e, e);
    }
  }

  /**
   * GetSession.
   *
   * @deprecated Use openSession()
   */
  @Override
  @Deprecated
  public SqlMapSession getSession() {
    log.warn(
        "Use of a deprecated API detected.  SqlMapClient.getSession() is deprecated.  Use SqlMapClient.openSession() instead.");
    return openSession();
  }

  @Override
  public void flushDataCache() {
    delegate.flushDataCache();
  }

  @Override
  public void flushDataCache(String cacheId) {
    delegate.flushDataCache(cacheId);
  }

  /**
   * Gets the local sql map session.
   *
   * @return the local sql map session
   */
  protected SqlMapSessionImpl getLocalSqlMapSession() {
    SqlMapSessionImpl sqlMapSession = localSqlMapSession.get();
    if (sqlMapSession == null || sqlMapSession.isClosed()) {
      sqlMapSession = new SqlMapSessionImpl(this);
      localSqlMapSession.set(sqlMapSession);
    }
    return sqlMapSession;
  }

  @Override
  public ResultObjectFactory getResultObjectFactory() {
    return delegate.getResultObjectFactory();
  }
}
