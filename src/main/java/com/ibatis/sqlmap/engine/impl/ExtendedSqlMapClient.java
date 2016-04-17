/**
 * Copyright 2004-2016 the original author or authors.
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

import com.ibatis.sqlmap.client.*;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.*;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.common.util.PaginatedList;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @deprecated - this class is uneccessary and should be removed as soon as possible. Currently spring integration
 *             depends on it.
 */
public interface ExtendedSqlMapClient extends SqlMapClient {

  Object insert(String id, Object param) throws SQLException;

  Object insert(String id) throws SQLException;

  int update(String id, Object param) throws SQLException;

  int update(String id) throws SQLException;

  int delete(String id, Object param) throws SQLException;

  int delete(String id) throws SQLException;

  Object queryForObject(String id, Object paramObject) throws SQLException;

  Object queryForObject(String id) throws SQLException;

  Object queryForObject(String id, Object paramObject, Object resultObject) throws SQLException;

  List queryForList(String id, Object paramObject) throws SQLException;

  List queryForList(String id) throws SQLException;

  List queryForList(String id, Object paramObject, int skip, int max) throws SQLException;

  List queryForList(String id, int skip, int max) throws SQLException;

  PaginatedList queryForPaginatedList(String id, Object paramObject, int pageSize) throws SQLException;

  PaginatedList queryForPaginatedList(String id, int pageSize) throws SQLException;

  Map queryForMap(String id, Object paramObject, String keyProp) throws SQLException;

  Map queryForMap(String id, Object paramObject, String keyProp, String valueProp) throws SQLException;

  void queryWithRowHandler(String id, Object paramObject, RowHandler rowHandler) throws SQLException;

  void queryWithRowHandler(String id, RowHandler rowHandler) throws SQLException;

  void startTransaction() throws SQLException;

  void startTransaction(int transactionIsolation) throws SQLException;

  void commitTransaction() throws SQLException;

  void endTransaction() throws SQLException;

  void startBatch() throws SQLException;

  int executeBatch() throws SQLException;

  List executeBatchDetailed() throws SQLException, BatchException;

  void setUserConnection(Connection connection) throws SQLException;

  Connection getUserConnection() throws SQLException;

  Connection getCurrentConnection() throws SQLException;

  DataSource getDataSource();

  MappedStatement getMappedStatement(String id);

  boolean isLazyLoadingEnabled();

  boolean isEnhancementEnabled();

  SqlExecutor getSqlExecutor();

  SqlMapExecutorDelegate getDelegate();

  SqlMapSession openSession();

  SqlMapSession openSession(Connection conn);

  SqlMapSession getSession();

  void flushDataCache();

  void flushDataCache(String cacheId);

  ResultObjectFactory getResultObjectFactory();

}
