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
package com.ibatis.sqlmap.engine.execution;

import com.ibatis.sqlmap.engine.config.SqlMapConfiguration;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Classes responsible for executing the SQL implement this interface Support for custom SQL Executors.
 */
public interface SqlExecutor {
  //
  // Constants
  //
  /** Constant to let us know not to skip anything. */
  int NO_SKIPPED_RESULTS = 0;

  /** Constant to let us know to include all records. */
  int NO_MAXIMUM_RESULTS = -999999;

  /**
   * Execute an update.
   *
   * @param statementScope
   *          - the request scope
   * @param conn
   *          - the database connection
   * @param sql
   *          - the sql statement to execute
   * @param parameters
   *          - the parameters for the sql statement
   *
   * @return - the number of records changed
   *
   * @throws SQLException
   *           - if the update fails
   */
  int executeUpdate(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
      throws SQLException;

  /**
   * Adds a statement to a batch.
   *
   * @param statementScope
   *          - the request scope
   * @param conn
   *          - the database connection
   * @param sql
   *          - the sql statement
   * @param parameters
   *          - the parameters for the statement
   *
   * @throws SQLException
   *           - if the statement fails
   */
  void addBatch(StatementScope statementScope, Connection conn, String sql, Object[] parameters) throws SQLException;

  /**
   * Execute a batch of statements.
   *
   * @param sessionScope
   *          - the session scope
   *
   * @return - the number of rows impacted by the batch
   *
   * @throws SQLException
   *           - if a statement fails
   */
  int executeBatch(SessionScope sessionScope) throws SQLException;

  /**
   * Execute a batch of statements.
   *
   * @param sessionScope
   *          - the session scope
   *
   * @return - a List of BatchResult objects (may be null if no batch has been initiated). There will be one BatchResult
   *         object in the list for each sub-batch executed
   *
   * @throws SQLException
   *           if a database access error occurs, or the drive does not support batch statements
   * @throws BatchException
   *           if the driver throws BatchUpdateException
   */
  List executeBatchDetailed(SessionScope sessionScope) throws SQLException, BatchException;

  /**
   * Long form of the method to execute a query.
   *
   * @param statementScope
   *          - the request scope
   * @param conn
   *          - the database connection
   * @param sql
   *          - the SQL statement to execute
   * @param parameters
   *          - the parameters for the statement
   * @param skipResults
   *          - the number of results to skip
   * @param maxResults
   *          - the maximum number of results to return
   * @param callback
   *          - the row handler for the query
   *
   * @throws SQLException
   *           - if the query fails
   */
  void executeQuery(StatementScope statementScope, Connection conn, String sql, Object[] parameters, int skipResults,
      int maxResults, RowHandlerCallback callback) throws SQLException;

  /**
   * Execute a stored procedure that updates data.
   *
   * @param statementScope
   *          - the request scope
   * @param conn
   *          - the database connection
   * @param sql
   *          - the SQL to call the procedure
   * @param parameters
   *          - the parameters for the procedure
   *
   * @return - the rows impacted by the procedure
   *
   * @throws SQLException
   *           - if the procedure fails
   */
  int executeUpdateProcedure(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
      throws SQLException;

  /**
   * Execute a stored procedure.
   *
   * @param statementScope
   *          - the request scope
   * @param conn
   *          - the database connection
   * @param sql
   *          - the sql to call the procedure
   * @param parameters
   *          - the parameters for the procedure
   * @param skipResults
   *          - the number of results to skip
   * @param maxResults
   *          - the maximum number of results to return
   * @param callback
   *          - a row handler for processing the results
   *
   * @throws SQLException
   *           - if the procedure fails
   */
  void executeQueryProcedure(StatementScope statementScope, Connection conn, String sql, Object[] parameters,
      int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException;

  /**
   * Clean up any batches on the session.
   *
   * @param sessionScope
   *          - the session to clean up
   */
  void cleanup(SessionScope sessionScope);

  /**
   * Initializing SQL Executor by passing configuration and global properties.
   *
   * @param config
   *          - the configuration class, which contains complete configuration info
   * @param globalProps
   *          the global props
   */
  void init(SqlMapConfiguration config, Properties globalProps);
}
