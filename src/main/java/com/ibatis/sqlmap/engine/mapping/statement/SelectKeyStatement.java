/*
 * Copyright 2004-2022 the original author or authors.
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
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.ibatis.sqlmap.engine.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * The Class SelectKeyStatement.
 */
public class SelectKeyStatement extends SelectStatement {

  /** The key property. */
  private String keyProperty;

  /** The run after SQL. */
  private boolean runAfterSQL;

  /**
   * Gets the key property.
   *
   * @return the key property
   */
  public String getKeyProperty() {
    return keyProperty;
  }

  /**
   * Sets the key property.
   *
   * @param keyProperty
   *          the new key property
   */
  public void setKeyProperty(String keyProperty) {
    this.keyProperty = keyProperty;
  }

  /**
   * Checks if is run after SQL.
   *
   * @return true, if is run after SQL
   */
  public boolean isRunAfterSQL() {
    return runAfterSQL;
  }

  /**
   * Sets the run after SQL.
   *
   * @param runAfterSQL
   *          the new run after SQL
   */
  public void setRunAfterSQL(boolean runAfterSQL) {
    this.runAfterSQL = runAfterSQL;
  }

  @Override
  public List executeQueryForList(StatementScope statementScope, Transaction trans, Object parameterObject,
      int skipResults, int maxResults) throws SQLException {
    throw new SQLException("Select Key statements cannot be executed for a list.");
  }

  @Override
  public void executeQueryWithRowHandler(StatementScope statementScope, Transaction trans, Object parameterObject,
      RowHandler rowHandler) throws SQLException {
    throw new SQLException("Select Key statements cannot be executed with a row handler.");
  }

}
