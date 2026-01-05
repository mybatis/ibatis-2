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
package com.ibatis.sqlmap.engine.impl;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
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
 * The Interface ExtendedSqlMapClient.
 *
 * @deprecated - this class is unnecessary and should be removed as soon as possible. Currently spring integration
 *             depends on it.
 */
@Deprecated
public interface ExtendedSqlMapClient extends SqlMapClient {

  /**
   * Insert.
   *
   * @param id
   *          the id
   * @param param
   *          the param
   *
   * @return the object
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Object insert(String id, Object param) throws SQLException;

  /**
   * Insert.
   *
   * @param id
   *          the id
   *
   * @return the object
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Object insert(String id) throws SQLException;

  /**
   * Update.
   *
   * @param id
   *          the id
   * @param param
   *          the param
   *
   * @return the int
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  int update(String id, Object param) throws SQLException;

  /**
   * Update.
   *
   * @param id
   *          the id
   *
   * @return the int
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  int update(String id) throws SQLException;

  /**
   * Delete.
   *
   * @param id
   *          the id
   * @param param
   *          the param
   *
   * @return the int
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  int delete(String id, Object param) throws SQLException;

  /**
   * Delete.
   *
   * @param id
   *          the id
   *
   * @return the int
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  int delete(String id) throws SQLException;

  /**
   * Query for object.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   *
   * @return the object
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Object queryForObject(String id, Object paramObject) throws SQLException;

  /**
   * Query for object.
   *
   * @param id
   *          the id
   *
   * @return the object
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Object queryForObject(String id) throws SQLException;

  /**
   * Query for object.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @param resultObject
   *          the result object
   *
   * @return the object
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Object queryForObject(String id, Object paramObject, Object resultObject) throws SQLException;

  /**
   * Query for list.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   *
   * @return the list
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  List queryForList(String id, Object paramObject) throws SQLException;

  /**
   * Query for list.
   *
   * @param id
   *          the id
   *
   * @return the list
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  List queryForList(String id) throws SQLException;

  /**
   * Query for list.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @param skip
   *          the skip
   * @param max
   *          the max
   *
   * @return the list
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  List queryForList(String id, Object paramObject, int skip, int max) throws SQLException;

  /**
   * Query for list.
   *
   * @param id
   *          the id
   * @param skip
   *          the skip
   * @param max
   *          the max
   *
   * @return the list
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  List queryForList(String id, int skip, int max) throws SQLException;

  /**
   * Query for paginated list.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @param pageSize
   *          the page size
   *
   * @return the paginated list
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  PaginatedList queryForPaginatedList(String id, Object paramObject, int pageSize) throws SQLException;

  /**
   * Query for paginated list.
   *
   * @param id
   *          the id
   * @param pageSize
   *          the page size
   *
   * @return the paginated list
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  PaginatedList queryForPaginatedList(String id, int pageSize) throws SQLException;

  /**
   * Query for map.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @param keyProp
   *          the key prop
   *
   * @return the map
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Map queryForMap(String id, Object paramObject, String keyProp) throws SQLException;

  /**
   * Query for map.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @param keyProp
   *          the key prop
   * @param valueProp
   *          the value prop
   *
   * @return the map
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Map queryForMap(String id, Object paramObject, String keyProp, String valueProp) throws SQLException;

  /**
   * Query with row handler.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @param rowHandler
   *          the row handler
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void queryWithRowHandler(String id, Object paramObject, RowHandler rowHandler) throws SQLException;

  /**
   * Query with row handler.
   *
   * @param id
   *          the id
   * @param rowHandler
   *          the row handler
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException;

  /**
   * Start transaction.
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void startTransaction() throws SQLException;

  /**
   * Start transaction.
   *
   * @param transactionIsolation
   *          the transaction isolation
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void startTransaction(int transactionIsolation) throws SQLException;

  /**
   * Commit transaction.
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void commitTransaction() throws SQLException;

  /**
   * End transaction.
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void endTransaction() throws SQLException;

  /**
   * Start batch.
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void startBatch() throws SQLException;

  /**
   * Execute batch.
   *
   * @return the int
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  int executeBatch() throws SQLException;

  /**
   * Execute batch detailed.
   *
   * @return the list
   *
   * @throws SQLException
   *           the SQL exception
   * @throws BatchException
   *           the batch exception
   */
  @Deprecated
  @Override
  List executeBatchDetailed() throws SQLException, BatchException;

  /**
   * Sets the user connection.
   *
   * @param connection
   *          the new user connection
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  void setUserConnection(Connection connection) throws SQLException;

  /**
   * Gets the user connection.
   *
   * @return the user connection
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Connection getUserConnection() throws SQLException;

  /**
   * Gets the current connection.
   *
   * @return the current connection
   *
   * @throws SQLException
   *           the SQL exception
   */
  @Deprecated
  @Override
  Connection getCurrentConnection() throws SQLException;

  /**
   * Gets the data source.
   *
   * @return the data source
   */
  @Deprecated
  @Override
  DataSource getDataSource();

  /**
   * Gets the mapped statement.
   *
   * @param id
   *          the id
   *
   * @return the mapped statement
   */
  @Deprecated
  MappedStatement getMappedStatement(String id);

  /**
   * Checks if is lazy loading enabled.
   *
   * @return true, if is lazy loading enabled
   */
  @Deprecated
  boolean isLazyLoadingEnabled();

  /**
   * Checks if is enhancement enabled.
   *
   * @return true, if is enhancement enabled
   */
  @Deprecated
  boolean isEnhancementEnabled();

  /**
   * Gets the sql executor.
   *
   * @return the sql executor
   */
  @Deprecated
  SqlExecutor getSqlExecutor();

  /**
   * Gets the delegate.
   *
   * @return the delegate
   */
  @Deprecated
  SqlMapExecutorDelegate getDelegate();

  /**
   * Open session.
   *
   * @return the sql map session
   */
  @Deprecated
  @Override
  SqlMapSession openSession();

  /**
   * Open session.
   *
   * @param conn
   *          the conn
   *
   * @return the sql map session
   */
  @Deprecated
  @Override
  SqlMapSession openSession(Connection conn);

  /**
   * Gets the session.
   *
   * @return the session
   */
  @Deprecated
  @Override
  SqlMapSession getSession();

  /**
   * Flush data cache.
   */
  @Deprecated
  @Override
  void flushDataCache();

  /**
   * Flush data cache.
   *
   * @param cacheId
   *          the cache id
   */
  @Deprecated
  @Override
  void flushDataCache(String cacheId);

  /**
   * Gets the result object factory.
   *
   * @return the result object factory
   */
  @Deprecated
  ResultObjectFactory getResultObjectFactory();

}
