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
package com.ibatis.sqlmap.engine.mapping.statement;

import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.cache.CacheKey;
import com.ibatis.sqlmap.engine.cache.CacheModel;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.ibatis.sqlmap.engine.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * The Class CachingStatement.
 */
public class CachingStatement extends MappedStatement {

  /** The statement. */
  private MappedStatement statement;

  /** The cache model. */
  private CacheModel cacheModel;

  /**
   * Instantiates a new caching statement.
   *
   * @param statement
   *          the statement
   * @param cacheModel
   *          the cache model
   */
  public CachingStatement(MappedStatement statement, CacheModel cacheModel) {
    this.statement = statement;
    this.cacheModel = cacheModel;
  }

  @Override
  public String getId() {
    return statement.getId();
  }

  @Override
  public StatementType getStatementType() {
    return statement.getStatementType();
  }

  @Override
  public Integer getResultSetType() {
    return statement.getResultSetType();
  }

  @Override
  public Integer getFetchSize() {
    return statement.getFetchSize();
  }

  @Override
  public ParameterMap getParameterMap() {
    return statement.getParameterMap();
  }

  @Override
  public ResultMap getResultMap() {
    return statement.getResultMap();
  }

  @Override
  public int executeUpdate(StatementScope statementScope, Transaction trans, Object parameterObject)
      throws SQLException {
    return statement.executeUpdate(statementScope, trans, parameterObject);
  }

  @Override
  public Object executeQueryForObject(StatementScope statementScope, Transaction trans, Object parameterObject,
      Object resultObject) throws SQLException {
    CacheKey cacheKey = getCacheKey(statementScope, parameterObject);
    cacheKey.update("executeQueryForObject");
    Object object = cacheModel.getObject(cacheKey);
    if (object == CacheModel.NULL_OBJECT) {
      // This was cached, but null
      object = null;
    } else if (object == null) {
      object = statement.executeQueryForObject(statementScope, trans, parameterObject, resultObject);
      cacheModel.putObject(cacheKey, object);
    }
    return object;
  }

  @Override
  public List executeQueryForList(StatementScope statementScope, Transaction trans, Object parameterObject,
      int skipResults, int maxResults) throws SQLException {
    CacheKey cacheKey = getCacheKey(statementScope, parameterObject);
    cacheKey.update("executeQueryForList");
    cacheKey.update(skipResults);
    cacheKey.update(maxResults);
    Object listAsObject = cacheModel.getObject(cacheKey);
    List list;
    if (listAsObject == CacheModel.NULL_OBJECT) {
      // The cached object was null
      list = null;
    } else if (listAsObject == null) {
      list = statement.executeQueryForList(statementScope, trans, parameterObject, skipResults, maxResults);
      cacheModel.putObject(cacheKey, list);
    } else {
      list = (List) listAsObject;
    }
    return list;
  }

  @Override
  public void executeQueryWithRowHandler(StatementScope statementScope, Transaction trans, Object parameterObject,
      RowHandler rowHandler) throws SQLException {
    statement.executeQueryWithRowHandler(statementScope, trans, parameterObject, rowHandler);
  }

  @Override
  public CacheKey getCacheKey(StatementScope statementScope, Object parameterObject) {
    CacheKey key = statement.getCacheKey(statementScope, parameterObject);
    if (!cacheModel.isReadOnly() && !cacheModel.isSerialize()) {
      key.update(statementScope.getSession());
    }
    return key;
  }

  @Override
  public void setBaseCacheKey(int base) {
    statement.setBaseCacheKey(base);
  }

  @Override
  public void addExecuteListener(ExecuteListener listener) {
    statement.addExecuteListener(listener);
  }

  @Override
  public void notifyListeners() {
    statement.notifyListeners();
  }

  @Override
  public void initRequest(StatementScope statementScope) {
    statement.initRequest(statementScope);
  }

  @Override
  public Sql getSql() {
    return statement.getSql();
  }

  @Override
  public Class getParameterClass() {
    return statement.getParameterClass();
  }

  @Override
  public Integer getTimeout() {
    return statement.getTimeout();
  }

  @Override
  public boolean hasMultipleResultMaps() {
    return statement.hasMultipleResultMaps();
  }

  @Override
  public ResultMap[] getAdditionalResultMaps() {
    return statement.getAdditionalResultMaps();
  }

}
