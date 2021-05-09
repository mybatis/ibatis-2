/*
 * Copyright 2004-2021 the original author or authors.
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
package com.ibatis.sqlmap.engine.impl;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.*;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.*;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

/**
 * The Interface ExtendedSqlMapClient.
 *
 * @deprecated - this class is uneccessary and should be removed as soon as possible. Currently spring integration
 *             depends on it.
 */
public interface ExtendedSqlMapClient extends SqlMapClient {

  /**
   * Insert.
   *
   * @param id
   *          the id
   * @param param
   *          the param
   * @return the object
   * @throws SQLException
   *           the SQL exception
   */
  Object insert(String id, Object param) throws SQLException;

  /**
   * Insert.
   *
   * @param id
   *          the id
   * @return the object
   * @throws SQLException
   *           the SQL exception
   */
  Object insert(String id) throws SQLException;

  /**
   * Update.
   *
   * @param id
   *          the id
   * @param param
   *          the param
   * @return the int
   * @throws SQLException
   *           the SQL exception
   */
  int update(String id, Object param) throws SQLException;

  /**
   * Update.
   *
   * @param id
   *          the id
   * @return the int
   * @throws SQLException
   *           the SQL exception
   */
  int update(String id) throws SQLException;

  /**
   * Delete.
   *
   * @param id
   *          the id
   * @param param
   *          the param
   * @return the int
   * @throws SQLException
   *           the SQL exception
   */
  int delete(String id, Object param) throws SQLException;

  /**
   * Delete.
   *
   * @param id
   *          the id
   * @return the int
   * @throws SQLException
   *           the SQL exception
   */
  int delete(String id) throws SQLException;

  /**
   * Query for object.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @return the object
   * @throws SQLException
   *           the SQL exception
   */
  Object queryForObject(String id, Object paramObject) throws SQLException;

  /**
   * Query for object.
   *
   * @param id
   *          the id
   * @return the object
   * @throws SQLException
   *           the SQL exception
   */
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
   * @return the object
   * @throws SQLException
   *           the SQL exception
   */
  Object queryForObject(String id, Object paramObject, Object resultObject) throws SQLException;

  /**
   * Query for list.
   *
   * @param id
   *          the id
   * @param paramObject
   *          the param object
   * @return the list
   * @throws SQLException
   *           the SQL exception
   */
  List queryForList(String id, Object paramObject) throws SQLException;

  /**
   * Query for list.
   *
   * @param id
   *          the id
   * @return the list
   * @throws SQLException
   *           the SQL exception
   */
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
   * @return the list
   * @throws SQLException
   *           the SQL exception
   */
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
   * @return the list
   * @throws SQLException
   *           the SQL exception
   */
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
   * @return the paginated list
   * @throws SQLException
   *           the SQL exception
   */
  PaginatedList queryForPaginatedList(String id, Object paramObject, int pageSize) throws SQLException;

  /**
   * Query for paginated list.
   *
   * @param id
   *          the id
   * @param pageSize
   *          the page size
   * @return the paginated list
   * @throws SQLException
   *           the SQL exception
   */
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
   * @return the map
   * @throws SQLException
   *           the SQL exception
   */
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
   * @return the map
   * @throws SQLException
   *           the SQL exception
   */
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
   * @throws SQLException
   *           the SQL exception
   */
  void queryWithRowHandler(String id, Object paramObject, RowHandler rowHandler) throws SQLException;

  /**
   * Query with row handler.
   *
   * @param id
   *          the id
   * @param rowHandler
   *          the row handler
   * @throws SQLException
   *           the SQL exception
   */
  void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException;

  /**
   * Start transaction.
   *
   * @throws SQLException
   *           the SQL exception
   */
  void startTransaction() throws SQLException;

  /**
   * Start transaction.
   *
   * @param transactionIsolation
   *          the transaction isolation
   * @throws SQLException
   *           the SQL exception
   */
  void startTransaction(int transactionIsolation) throws SQLException;

  /**
   * Commit transaction.
   *
   * @throws SQLException
   *           the SQL exception
   */
  void commitTransaction() throws SQLException;

  /**
   * End transaction.
   *
   * @throws SQLException
   *           the SQL exception
   */
  void endTransaction() throws SQLException;

  /**
   * Start batch.
   *
   * @throws SQLException
   *           the SQL exception
   */
  void startBatch() throws SQLException;

  /**
   * Execute batch.
   *
   * @return the int
   * @throws SQLException
   *           the SQL exception
   */
  int executeBatch() throws SQLException;

  /**
   * Execute batch detailed.
   *
   * @return the list
   * @throws SQLException
   *           the SQL exception
   * @throws BatchException
   *           the batch exception
   */
  List executeBatchDetailed() throws SQLException, BatchException;

  /**
   * Sets the user connection.
   *
   * @param connection
   *          the new user connection
   * @throws SQLException
   *           the SQL exception
   */
  void setUserConnection(Connection connection) throws SQLException;

  /**
   * Gets the user connection.
   *
   * @return the user connection
   * @throws SQLException
   *           the SQL exception
   */
  Connection getUserConnection() throws SQLException;

  /**
   * Gets the current connection.
   *
   * @return the current connection
   * @throws SQLException
   *           the SQL exception
   */
  Connection getCurrentConnection() throws SQLException;

  /**
   * Gets the data source.
   *
   * @return the data source
   */
  DataSource getDataSource();

  /**
   * Gets the mapped statement.
   *
   * @param id
   *          the id
   * @return the mapped statement
   */
  MappedStatement getMappedStatement(String id);

  /**
   * Checks if is lazy loading enabled.
   *
   * @return true, if is lazy loading enabled
   */
  boolean isLazyLoadingEnabled();

  /**
   * Checks if is enhancement enabled.
   *
   * @return true, if is enhancement enabled
   */
  boolean isEnhancementEnabled();

  /**
   * Gets the sql executor.
   *
   * @return the sql executor
   */
  SqlExecutor getSqlExecutor();

  /**
   * Gets the delegate.
   *
   * @return the delegate
   */
  SqlMapExecutorDelegate getDelegate();

  /**
   * Open session.
   *
   * @return the sql map session
   */
  SqlMapSession openSession();

  /**
   * Open session.
   *
   * @param conn
   *          the conn
   * @return the sql map session
   */
  SqlMapSession openSession(Connection conn);

  /**
   * Gets the session.
   *
   * @return the session
   */
  SqlMapSession getSession();

  /**
   * Flush data cache.
   */
  void flushDataCache();

  /**
   * Flush data cache.
   *
   * @param cacheId
   *          the cache id
   */
  void flushDataCache(String cacheId);

  /**
   * Gets the result object factory.
   *
   * @return the result object factory
   */
  ResultObjectFactory getResultObjectFactory();

}
