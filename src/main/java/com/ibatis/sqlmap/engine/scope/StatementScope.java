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
package com.ibatis.sqlmap.engine.scope;

import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Request based implementation of Scope interface.
 */
public class StatementScope {

  /** The session scope. */
  // Used by Any
  private SessionScope sessionScope;

  /** The error context. */
  private ErrorContext errorContext;

  /** The statement. */
  private MappedStatement statement;

  /** The parameter map. */
  private ParameterMap parameterMap;

  /** The result map. */
  private ResultMap resultMap;

  /** The sql. */
  private Sql sql;

  /** The dynamic parameter map. */
  // Used by DynamicSql
  private ParameterMap dynamicParameterMap;

  /** The dynamic sql. */
  private String dynamicSql;

  /** The result set. */
  // Used by N+1 Select solution
  private ResultSet resultSet;

  /** The unique keys. */
  private Map uniqueKeys;

  /** The row data found. */
  private boolean rowDataFound;

  /** The current nested key. */
  private String currentNestedKey;

  /**
   * Instantiates a new statement scope.
   *
   * @param sessionScope
   *          the session scope
   */
  public StatementScope(SessionScope sessionScope) {
    this.errorContext = new ErrorContext();
    this.rowDataFound = true;
    this.sessionScope = sessionScope;
  }

  /**
   * Gets the current nested key.
   *
   * @return Returns the currentNestedKey.
   */
  public String getCurrentNestedKey() {
    return currentNestedKey;
  }

  /**
   * Sets the current nested key.
   *
   * @param currentNestedKey
   *          The currentNestedKey to set.
   */
  public void setCurrentNestedKey(String currentNestedKey) {
    this.currentNestedKey = currentNestedKey;
  }

  /**
   * Get the request's error context.
   *
   * @return - the request's error context
   */
  public ErrorContext getErrorContext() {
    return errorContext;
  }

  /**
   * Get the session of the request.
   *
   * @return - the session
   */
  public SessionScope getSession() {
    return sessionScope;
  }

  /**
   * Get the statement for the request.
   *
   * @return - the statement
   */
  public MappedStatement getStatement() {
    return statement;
  }

  /**
   * Set the statement for the request.
   *
   * @param statement
   *          - the statement
   */
  public void setStatement(MappedStatement statement) {
    this.statement = statement;
  }

  /**
   * Get the parameter map for the request.
   *
   * @return - the parameter map
   */
  public ParameterMap getParameterMap() {
    return parameterMap;
  }

  /**
   * Set the parameter map for the request.
   *
   * @param parameterMap
   *          - the new parameter map
   */
  public void setParameterMap(ParameterMap parameterMap) {
    this.parameterMap = parameterMap;
  }

  /**
   * Get the result map for the request.
   *
   * @return - the result map
   */
  public ResultMap getResultMap() {
    return resultMap;
  }

  /**
   * Set the result map for the request.
   *
   * @param resultMap
   *          - the result map
   */
  public void setResultMap(ResultMap resultMap) {
    this.resultMap = resultMap;
  }

  /**
   * Get the SQL for the request.
   *
   * @return - the sql
   */
  public Sql getSql() {
    return sql;
  }

  /**
   * Set the SQL for the request.
   *
   * @param sql
   *          - the sql
   */
  public void setSql(Sql sql) {
    this.sql = sql;
  }

  /**
   * Get the dynamic parameter for the request.
   *
   * @return - the dynamic parameter
   */
  public ParameterMap getDynamicParameterMap() {
    return dynamicParameterMap;
  }

  /**
   * Set the dynamic parameter for the request.
   *
   * @param dynamicParameterMap
   *          - the dynamic parameter
   */
  public void setDynamicParameterMap(ParameterMap dynamicParameterMap) {
    this.dynamicParameterMap = dynamicParameterMap;
  }

  /**
   * Get the dynamic SQL for the request.
   *
   * @return - the dynamic SQL
   */
  public String getDynamicSql() {
    return dynamicSql;
  }

  /**
   * Set the dynamic SQL for the request.
   *
   * @param dynamicSql
   *          - the dynamic SQL
   */
  public void setDynamicSql(String dynamicSql) {
    this.dynamicSql = dynamicSql;
  }

  /**
   * Gets the result set.
   *
   * @return the result set
   */
  public ResultSet getResultSet() {
    return resultSet;
  }

  /**
   * Sets the result set.
   *
   * @param resultSet
   *          the new result set
   */
  public void setResultSet(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  /**
   * Gets the unique keys.
   *
   * @param map
   *          the map
   *
   * @return the unique keys
   */
  public Map getUniqueKeys(ResultMap map) {
    if (uniqueKeys == null) {
      return null;
    }
    return (Map) uniqueKeys.get(map);
  }

  /**
   * Sets the unique keys.
   *
   * @param map
   *          the map
   * @param keys
   *          the keys
   */
  public void setUniqueKeys(ResultMap map, Map keys) {
    if (uniqueKeys == null) {
      uniqueKeys = new HashMap<>();
    }
    this.uniqueKeys.put(map, keys);
  }

  /**
   * Checks if is row data found.
   *
   * @return true, if is row data found
   */
  public boolean isRowDataFound() {
    return rowDataFound;
  }

  /**
   * Sets the row data found.
   *
   * @param rowDataFound
   *          the new row data found
   */
  public void setRowDataFound(boolean rowDataFound) {
    this.rowDataFound = rowDataFound;
  }

}
