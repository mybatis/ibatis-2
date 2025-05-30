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
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactoryUtil;
import com.ibatis.sqlmap.engine.mapping.statement.DefaultRowHandler;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.ErrorContext;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;

import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class responsible for executing the SQL.
 */
public class DefaultSqlExecutor implements SqlExecutor {

  /**
   * Execute an update
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
  public int executeUpdate(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
      throws SQLException {
    ErrorContext errorContext = statementScope.getErrorContext();
    errorContext.setActivity("executing update");
    errorContext.setObjectId(sql);
    PreparedStatement ps = null;
    setupResultObjectFactory(statementScope);
    int rows = 0;
    try {
      errorContext.setMoreInfo("Check the SQL Statement (preparation failed).");
      ps = prepareStatement(statementScope.getSession(), conn, sql);
      setStatementTimeout(statementScope.getStatement(), ps);
      errorContext.setMoreInfo("Check the parameters (set parameters failed).");
      statementScope.getParameterMap().setParameters(statementScope, ps, parameters);
      errorContext.setMoreInfo("Check the statement (update failed).");
      ps.execute();
      rows = ps.getUpdateCount();
    } finally {
      closeStatement(statementScope.getSession(), ps);
      cleanupResultObjectFactory();
    }
    return rows;
  }

  /**
   * Adds a statement to a batch
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
  public void addBatch(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
      throws SQLException {
    Batch batch = (Batch) statementScope.getSession().getBatch();
    if (batch == null) {
      batch = new Batch();
      statementScope.getSession().setBatch(batch);
    }
    batch.addBatch(statementScope, conn, sql, parameters);
  }

  /**
   * Execute a batch of statements
   *
   * @param sessionScope
   *          - the session scope
   *
   * @return - the number of rows impacted by the batch
   *
   * @throws SQLException
   *           - if a statement fails
   */
  public int executeBatch(SessionScope sessionScope) throws SQLException {
    int rows = 0;
    Batch batch = (Batch) sessionScope.getBatch();
    if (batch != null) {
      try {
        rows = batch.executeBatch();
      } finally {
        batch.cleanupBatch(sessionScope);
      }
    }
    return rows;
  }

  /**
   * Execute a batch of statements
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
  public List executeBatchDetailed(SessionScope sessionScope) throws SQLException, BatchException {
    List answer = null;
    Batch batch = (Batch) sessionScope.getBatch();
    if (batch != null) {
      try {
        answer = batch.executeBatchDetailed();
      } finally {
        batch.cleanupBatch(sessionScope);
      }
    }
    return answer;
  }

  /**
   * Long form of the method to execute a query
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
  public void executeQuery(StatementScope statementScope, Connection conn, String sql, Object[] parameters,
      int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {
    ErrorContext errorContext = statementScope.getErrorContext();
    errorContext.setActivity("executing query");
    errorContext.setObjectId(sql);
    PreparedStatement ps = null;
    ResultSet rs = null;
    setupResultObjectFactory(statementScope);
    try {
      errorContext.setMoreInfo("Check the SQL Statement (preparation failed).");
      Integer rsType = statementScope.getStatement().getResultSetType();
      if (rsType != null) {
        ps = prepareStatement(statementScope.getSession(), conn, sql, rsType);
      } else {
        ps = prepareStatement(statementScope.getSession(), conn, sql);
      }
      setStatementTimeout(statementScope.getStatement(), ps);
      Integer fetchSize = statementScope.getStatement().getFetchSize();
      if (fetchSize != null) {
        ps.setFetchSize(fetchSize.intValue());
      }
      errorContext.setMoreInfo("Check the parameters (set parameters failed).");
      statementScope.getParameterMap().setParameters(statementScope, ps, parameters);
      errorContext.setMoreInfo("Check the statement (query failed).");
      ps.execute();
      errorContext.setMoreInfo("Check the results (failed to retrieve results).");

      // Begin ResultSet Handling
      rs = handleMultipleResults(ps, statementScope, skipResults, maxResults, callback);
      // End ResultSet Handling
    } finally {
      try {
        closeResultSet(rs);
      } finally {
        closeStatement(statementScope.getSession(), ps);
        cleanupResultObjectFactory();
      }
    }

  }

  /**
   * Execute a stored procedure that updates data
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
  public int executeUpdateProcedure(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
      throws SQLException {
    ErrorContext errorContext = statementScope.getErrorContext();
    errorContext.setActivity("executing update procedure");
    errorContext.setObjectId(sql);
    CallableStatement cs = null;
    setupResultObjectFactory(statementScope);
    int rows = 0;
    try {
      errorContext.setMoreInfo("Check the SQL Statement (preparation failed).");
      cs = prepareCall(statementScope.getSession(), conn, sql);
      setStatementTimeout(statementScope.getStatement(), cs);
      ParameterMap parameterMap = statementScope.getParameterMap();
      ParameterMapping[] mappings = parameterMap.getParameterMappings();
      errorContext.setMoreInfo("Check the output parameters (register output parameters failed).");
      registerOutputParameters(cs, mappings);
      errorContext.setMoreInfo("Check the parameters (set parameters failed).");
      parameterMap.setParameters(statementScope, cs, parameters);
      errorContext.setMoreInfo("Check the statement (update procedure failed).");
      cs.execute();
      rows = cs.getUpdateCount();
      errorContext.setMoreInfo("Check the output parameters (retrieval of output parameters failed).");
      retrieveOutputParameters(statementScope, cs, mappings, parameters, null);
    } finally {
      closeStatement(statementScope.getSession(), cs);
      cleanupResultObjectFactory();
    }
    return rows;
  }

  /**
   * Execute a stored procedure
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
  public void executeQueryProcedure(StatementScope statementScope, Connection conn, String sql, Object[] parameters,
      int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {
    ErrorContext errorContext = statementScope.getErrorContext();
    errorContext.setActivity("executing query procedure");
    errorContext.setObjectId(sql);
    CallableStatement cs = null;
    ResultSet rs = null;
    setupResultObjectFactory(statementScope);
    try {
      errorContext.setMoreInfo("Check the SQL Statement (preparation failed).");
      Integer rsType = statementScope.getStatement().getResultSetType();
      if (rsType != null) {
        cs = prepareCall(statementScope.getSession(), conn, sql, rsType);
      } else {
        cs = prepareCall(statementScope.getSession(), conn, sql);
      }
      setStatementTimeout(statementScope.getStatement(), cs);
      Integer fetchSize = statementScope.getStatement().getFetchSize();
      if (fetchSize != null) {
        cs.setFetchSize(fetchSize.intValue());
      }
      ParameterMap parameterMap = statementScope.getParameterMap();
      ParameterMapping[] mappings = parameterMap.getParameterMappings();
      errorContext.setMoreInfo("Check the output parameters (register output parameters failed).");
      registerOutputParameters(cs, mappings);
      errorContext.setMoreInfo("Check the parameters (set parameters failed).");
      parameterMap.setParameters(statementScope, cs, parameters);
      errorContext.setMoreInfo("Check the statement (update procedure failed).");
      cs.execute();
      errorContext.setMoreInfo("Check the results (failed to retrieve results).");

      // Begin ResultSet Handling
      rs = handleMultipleResults(cs, statementScope, skipResults, maxResults, callback);
      // End ResultSet Handling
      errorContext.setMoreInfo("Check the output parameters (retrieval of output parameters failed).");
      retrieveOutputParameters(statementScope, cs, mappings, parameters, callback);

    } finally {
      try {
        closeResultSet(rs);
      } finally {
        closeStatement(statementScope.getSession(), cs);
        cleanupResultObjectFactory();
      }
    }
  }

  public void init(SqlMapConfiguration config, Properties globalProps) {
    // No implementation is required in DefaultSqlExecutor.
  }

  /**
   * Handle multiple results.
   *
   * @param ps
   *          the ps
   * @param statementScope
   *          the statement scope
   * @param skipResults
   *          the skip results
   * @param maxResults
   *          the max results
   * @param callback
   *          the callback
   *
   * @return the result set
   *
   * @throws SQLException
   *           the SQL exception
   */
  private ResultSet handleMultipleResults(PreparedStatement ps, StatementScope statementScope, int skipResults,
      int maxResults, RowHandlerCallback callback) throws SQLException {
    ResultSet rs;
    rs = getFirstResultSet(statementScope, ps);
    if (rs != null) {
      handleResults(statementScope, rs, skipResults, maxResults, callback);
    }

    // Multiple ResultSet handling
    if (callback.getRowHandler() instanceof DefaultRowHandler) {
      MappedStatement statement = statementScope.getStatement();
      DefaultRowHandler defaultRowHandler = ((DefaultRowHandler) callback.getRowHandler());
      if (statement.hasMultipleResultMaps()) {
        List multipleResults = new ArrayList<>();
        multipleResults.add(defaultRowHandler.getList());
        ResultMap[] resultMaps = statement.getAdditionalResultMaps();
        int i = 0;
        while (moveToNextResultsSafely(statementScope, ps)) {
          if (i >= resultMaps.length)
            break;
          ResultMap rm = resultMaps[i];
          statementScope.setResultMap(rm);
          rs = ps.getResultSet();
          DefaultRowHandler rh = new DefaultRowHandler();
          handleResults(statementScope, rs, skipResults, maxResults, new RowHandlerCallback(rm, null, rh));
          multipleResults.add(rh.getList());
          i++;
        }
        defaultRowHandler.setList(multipleResults);
        statementScope.setResultMap(statement.getResultMap());
      } else {
        while (moveToNextResultsSafely(statementScope, ps))
          ;
      }
    }
    // End additional ResultSet handling
    return rs;
  }

  /**
   * Gets the first result set.
   *
   * @param scope
   *          the scope
   * @param stmt
   *          the stmt
   *
   * @return the first result set
   *
   * @throws SQLException
   *           the SQL exception
   */
  private ResultSet getFirstResultSet(StatementScope scope, Statement stmt) throws SQLException {
    ResultSet rs = null;
    boolean hasMoreResults = true;
    while (hasMoreResults) {
      rs = stmt.getResultSet();
      if (rs != null) {
        break;
      }
      hasMoreResults = moveToNextResultsIfPresent(scope, stmt);
    }
    return rs;
  }

  /**
   * Move to next results if present.
   *
   * @param scope
   *          the scope
   * @param stmt
   *          the stmt
   *
   * @return true, if successful
   *
   * @throws SQLException
   *           the SQL exception
   */
  private boolean moveToNextResultsIfPresent(StatementScope scope, Statement stmt) throws SQLException {
    boolean moreResults;
    // This is the messed up JDBC approach for determining if there are more results
    boolean movedToNextResultsSafely = moveToNextResultsSafely(scope, stmt);
    int updateCount = stmt.getUpdateCount();

    moreResults = !(!movedToNextResultsSafely && (updateCount == -1));

    // ibatis-384: workaround for mysql not returning -1 for stmt.getUpdateCount()
    if (moreResults == true) {
      moreResults = !(!movedToNextResultsSafely && !isMultipleResultSetSupportPresent(scope, stmt));
    }

    return moreResults;
  }

  /**
   * Move to next results safely.
   *
   * @param scope
   *          the scope
   * @param stmt
   *          the stmt
   *
   * @return true, if successful
   *
   * @throws SQLException
   *           the SQL exception
   */
  private boolean moveToNextResultsSafely(StatementScope scope, Statement stmt) throws SQLException {
    if (isMultipleResultSetSupportPresent(scope, stmt)) {
      return stmt.getMoreResults();
    }
    return false;
  }

  /**
   * checks whether multiple result set support is present - either by direct support of the database driver or by
   * forcing it.
   *
   * @param scope
   *          the scope
   * @param stmt
   *          the stmt
   *
   * @return true, if is multiple result set support present
   *
   * @throws SQLException
   *           the SQL exception
   */
  private boolean isMultipleResultSetSupportPresent(StatementScope scope, Statement stmt) throws SQLException {
    return forceMultipleResultSetSupport(scope) || stmt.getConnection().getMetaData().supportsMultipleResultSets();
  }

  /**
   * Force multiple result set support.
   *
   * @param scope
   *          the scope
   *
   * @return true, if successful
   */
  private boolean forceMultipleResultSetSupport(StatementScope scope) {
    return ((SqlMapClientImpl) scope.getSession().getSqlMapClient()).getDelegate().isForceMultipleResultSetSupport();
  }

  /**
   * Handle results.
   *
   * @param statementScope
   *          the statement scope
   * @param rs
   *          the rs
   * @param skipResults
   *          the skip results
   * @param maxResults
   *          the max results
   * @param callback
   *          the callback
   *
   * @throws SQLException
   *           the SQL exception
   */
  private void handleResults(StatementScope statementScope, ResultSet rs, int skipResults, int maxResults,
      RowHandlerCallback callback) throws SQLException {
    try {
      statementScope.setResultSet(rs);
      ResultMap resultMap = statementScope.getResultMap();
      if (resultMap != null) {
        // Skip Results
        if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
          if (skipResults > 0) {
            rs.absolute(skipResults);
          }
        } else {
          for (int i = 0; i < skipResults; i++) {
            if (!rs.next()) {
              return;
            }
          }
        }

        // Get Results
        int resultsFetched = 0;
        while ((maxResults == NO_MAXIMUM_RESULTS || resultsFetched < maxResults) && rs.next()) {
          Object[] columnValues = resultMap.resolveSubMap(statementScope, rs).getResults(statementScope, rs);
          callback.handleResultObject(statementScope, columnValues, rs);
          resultsFetched++;
        }
      }
    } finally {
      statementScope.setResultSet(null);
    }
  }

  /**
   * Retrieve output parameters.
   *
   * @param statementScope
   *          the statement scope
   * @param cs
   *          the cs
   * @param mappings
   *          the mappings
   * @param parameters
   *          the parameters
   * @param callback
   *          the callback
   *
   * @throws SQLException
   *           the SQL exception
   */
  private void retrieveOutputParameters(StatementScope statementScope, CallableStatement cs,
      ParameterMapping[] mappings, Object[] parameters, RowHandlerCallback callback) throws SQLException {
    for (int i = 0; i < mappings.length; i++) {
      ParameterMapping mapping = ((ParameterMapping) mappings[i]);
      if (mapping.isOutputAllowed()) {
        if ("java.sql.ResultSet".equalsIgnoreCase(mapping.getJavaTypeName())) {
          ResultSet rs = (ResultSet) cs.getObject(i + 1);
          ResultMap resultMap;
          if (mapping.getResultMapName() == null) {
            resultMap = statementScope.getResultMap();
            handleOutputParameterResults(statementScope, resultMap, rs, callback);
          } else {
            SqlMapClientImpl client = (SqlMapClientImpl) statementScope.getSession().getSqlMapClient();
            resultMap = client.getDelegate().getResultMap(mapping.getResultMapName());
            DefaultRowHandler rowHandler = new DefaultRowHandler();
            RowHandlerCallback handlerCallback = new RowHandlerCallback(resultMap, null, rowHandler);
            handleOutputParameterResults(statementScope, resultMap, rs, handlerCallback);
            parameters[i] = rowHandler.getList();
          }
          rs.close();
        } else {
          parameters[i] = mapping.getTypeHandler().getResult(cs, i + 1);
        }
      }
    }
  }

  /**
   * Register output parameters.
   *
   * @param cs
   *          the cs
   * @param mappings
   *          the mappings
   *
   * @throws SQLException
   *           the SQL exception
   */
  private void registerOutputParameters(CallableStatement cs, ParameterMapping[] mappings) throws SQLException {
    for (int i = 0; i < mappings.length; i++) {
      ParameterMapping mapping = ((ParameterMapping) mappings[i]);
      if (mapping.isOutputAllowed()) {
        if (null != mapping.getTypeName() && !mapping.getTypeName().equals("")) { // @added
          cs.registerOutParameter(i + 1, mapping.getJdbcType(), mapping.getTypeName());
        } else {
          if (mapping.getNumericScale() != null
              && (mapping.getJdbcType() == Types.NUMERIC || mapping.getJdbcType() == Types.DECIMAL)) {
            cs.registerOutParameter(i + 1, mapping.getJdbcType(), mapping.getNumericScale().intValue());
          } else {
            cs.registerOutParameter(i + 1, mapping.getJdbcType());
          }
        }
      }
    }
  }

  /**
   * Handle output parameter results.
   *
   * @param statementScope
   *          the statement scope
   * @param resultMap
   *          the result map
   * @param rs
   *          the rs
   * @param callback
   *          the callback
   *
   * @throws SQLException
   *           the SQL exception
   */
  private void handleOutputParameterResults(StatementScope statementScope, ResultMap resultMap, ResultSet rs,
      RowHandlerCallback callback) throws SQLException {
    ResultMap orig = statementScope.getResultMap();
    try {
      statementScope.setResultSet(rs);
      if (resultMap != null) {
        statementScope.setResultMap(resultMap);

        // Get Results
        while (rs.next()) {
          Object[] columnValues = resultMap.resolveSubMap(statementScope, rs).getResults(statementScope, rs);
          callback.handleResultObject(statementScope, columnValues, rs);
        }
      }
    } finally {
      statementScope.setResultSet(null);
      statementScope.setResultMap(orig);
    }
  }

  /**
   * Clean up any batches on the session
   *
   * @param sessionScope
   *          - the session to clean up
   */
  public void cleanup(SessionScope sessionScope) {
    Batch batch = (Batch) sessionScope.getBatch();
    if (batch != null) {
      batch.cleanupBatch(sessionScope);
      sessionScope.setBatch(null);
    }
  }

  /**
   * Prepare statement.
   *
   * @param sessionScope
   *          the session scope
   * @param conn
   *          the conn
   * @param sql
   *          the sql
   * @param rsType
   *          the rs type
   *
   * @return the prepared statement
   *
   * @throws SQLException
   *           the SQL exception
   */
  private PreparedStatement prepareStatement(SessionScope sessionScope, Connection conn, String sql, Integer rsType)
      throws SQLException {
    SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sessionScope.getSqlMapExecutor()).getDelegate();
    if (sessionScope.hasPreparedStatementFor(sql)) {
      return sessionScope.getPreparedStatement((sql));
    } else {
      PreparedStatement ps = conn.prepareStatement(sql, rsType.intValue(), ResultSet.CONCUR_READ_ONLY);
      sessionScope.putPreparedStatement(delegate, sql, ps);
      return ps;
    }
  }

  /**
   * Prepare call.
   *
   * @param sessionScope
   *          the session scope
   * @param conn
   *          the conn
   * @param sql
   *          the sql
   * @param rsType
   *          the rs type
   *
   * @return the callable statement
   *
   * @throws SQLException
   *           the SQL exception
   */
  private CallableStatement prepareCall(SessionScope sessionScope, Connection conn, String sql, Integer rsType)
      throws SQLException {
    SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sessionScope.getSqlMapExecutor()).getDelegate();
    if (sessionScope.hasPreparedStatementFor(sql)) {
      return (CallableStatement) sessionScope.getPreparedStatement((sql));
    } else {
      CallableStatement cs = conn.prepareCall(sql, rsType.intValue(), ResultSet.CONCUR_READ_ONLY);
      sessionScope.putPreparedStatement(delegate, sql, cs);
      return cs;
    }
  }

  /**
   * Prepare statement.
   *
   * @param sessionScope
   *          the session scope
   * @param conn
   *          the conn
   * @param sql
   *          the sql
   *
   * @return the prepared statement
   *
   * @throws SQLException
   *           the SQL exception
   */
  private static PreparedStatement prepareStatement(SessionScope sessionScope, Connection conn, String sql)
      throws SQLException {
    SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sessionScope.getSqlMapExecutor()).getDelegate();
    if (sessionScope.hasPreparedStatementFor(sql)) {
      return sessionScope.getPreparedStatement((sql));
    } else {
      PreparedStatement ps = conn.prepareStatement(sql);
      sessionScope.putPreparedStatement(delegate, sql, ps);
      return ps;
    }
  }

  /**
   * Prepare call.
   *
   * @param sessionScope
   *          the session scope
   * @param conn
   *          the conn
   * @param sql
   *          the sql
   *
   * @return the callable statement
   *
   * @throws SQLException
   *           the SQL exception
   */
  private CallableStatement prepareCall(SessionScope sessionScope, Connection conn, String sql) throws SQLException {
    SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sessionScope.getSqlMapExecutor()).getDelegate();
    if (sessionScope.hasPreparedStatementFor(sql)) {
      return (CallableStatement) sessionScope.getPreparedStatement((sql));
    } else {
      CallableStatement cs = conn.prepareCall(sql);
      sessionScope.putPreparedStatement(delegate, sql, cs);
      return cs;
    }
  }

  /**
   * Close statement.
   *
   * @param sessionScope
   *          the session scope
   * @param ps
   *          the ps
   */
  private static void closeStatement(SessionScope sessionScope, PreparedStatement ps) {
    if (ps != null) {
      if (!sessionScope.hasPreparedStatement(ps)) {
        try {
          ps.close();
        } catch (SQLException e) {
          // ignore
        }
      }
    }
  }

  /**
   * Close result set.
   *
   * @param rs
   *          the rs
   */
  private static void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        // ignore
      }
    }
  }

  /**
   * Sets the statement timeout.
   *
   * @param mappedStatement
   *          the mapped statement
   * @param statement
   *          the statement
   *
   * @throws SQLException
   *           the SQL exception
   */
  private static void setStatementTimeout(MappedStatement mappedStatement, Statement statement) throws SQLException {
    if (mappedStatement.getTimeout() != null) {
      statement.setQueryTimeout(mappedStatement.getTimeout().intValue());
    }
  }

  /**
   * The Class Batch.
   */
  private static class Batch {

    /** The current sql. */
    private String currentSql;

    /** The statement list. */
    private List statementList = new ArrayList<>();

    /** The batch result list. */
    private List batchResultList = new ArrayList<>();

    /** The size. */
    private int size;

    /**
     * Create a new batch.
     */
    public Batch() {
      this.size = 0;
    }

    /**
     * Getter for the batch size.
     *
     * @return - the batch size
     */
    public int getSize() {
      return size;
    }

    /**
     * Add a prepared statement to the batch.
     *
     * @param statementScope
     *          - the request scope
     * @param conn
     *          - the database connection
     * @param sql
     *          - the SQL to add
     * @param parameters
     *          - the parameters for the SQL
     *
     * @throws SQLException
     *           - if the prepare for the SQL fails
     */
    public void addBatch(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
        throws SQLException {
      PreparedStatement ps = null;
      if (currentSql != null && currentSql.equals(sql)) {
        int last = statementList.size() - 1;
        ps = (PreparedStatement) statementList.get(last);
      } else {
        ps = prepareStatement(statementScope.getSession(), conn, sql);
        setStatementTimeout(statementScope.getStatement(), ps);
        currentSql = sql;
        statementList.add(ps);
        batchResultList.add(new BatchResult(statementScope.getStatement().getId(), sql));
      }
      statementScope.getParameterMap().setParameters(statementScope, ps, parameters);
      ps.addBatch();
      size++;
    }

    /**
     * TODO (Jeff Butler) - maybe this method should be deprecated in some release, and then removed in some even later
     * release. executeBatchDetailed gives much more complete information.
     * <p>
     * Execute the current session's batch
     *
     * @return - the number of rows updated
     *
     * @throws SQLException
     *           - if the batch fails
     */
    public int executeBatch() throws SQLException {
      int totalRowCount = 0;
      for (int i = 0, n = statementList.size(); i < n; i++) {
        PreparedStatement ps = (PreparedStatement) statementList.get(i);
        int[] rowCounts = ps.executeBatch();
        for (int j = 0; j < rowCounts.length; j++) {
          if (rowCounts[j] == Statement.SUCCESS_NO_INFO) {
            // do nothing
          } else if (rowCounts[j] == Statement.EXECUTE_FAILED) {
            throw new SQLException("The batched statement at index " + j + " failed to execute.");
          } else {
            totalRowCount += rowCounts[j];
          }
        }
      }
      return totalRowCount;
    }

    /**
     * Batch execution method that returns all the information the driver has to offer.
     *
     * @return a List of BatchResult objects
     *
     * @throws SQLException
     *           if a database access error occurs, or the drive does not support batch statements
     * @throws BatchException
     *           if the driver throws BatchUpdateException
     */
    public List executeBatchDetailed() throws SQLException, BatchException {
      List answer = new ArrayList<>();
      for (int i = 0, n = statementList.size(); i < n; i++) {
        BatchResult br = (BatchResult) batchResultList.get(i);
        PreparedStatement ps = (PreparedStatement) statementList.get(i);
        try {
          br.setUpdateCounts(ps.executeBatch());
        } catch (BatchUpdateException e) {
          StringBuilder message = new StringBuilder();
          message.append("Sub batch number ");
          message.append(i + 1);
          message.append(" failed.");
          if (i > 0) {
            message.append(" ");
            message.append(i);
            message.append(" prior sub batch(s) completed successfully, but will be rolled back.");
          }
          throw new BatchException(message.toString(), e, answer, br.getStatementId(), br.getSql());
        }
        answer.add(br);
      }
      return answer;
    }

    /**
     * Close all the statements in the batch and clear all the statements.
     *
     * @param sessionScope
     *          the session scope
     */
    public void cleanupBatch(SessionScope sessionScope) {
      for (int i = 0, n = statementList.size(); i < n; i++) {
        PreparedStatement ps = (PreparedStatement) statementList.get(i);
        closeStatement(sessionScope, ps);
      }
      currentSql = null;
      statementList.clear();
      batchResultList.clear();
      size = 0;
    }
  }

  /**
   * Sets the up result object factory.
   *
   * @param statementScope
   *          the new up result object factory
   */
  private void setupResultObjectFactory(StatementScope statementScope) {
    SqlMapClientImpl client = (SqlMapClientImpl) statementScope.getSession().getSqlMapClient();
    ResultObjectFactoryUtil.setupResultObjectFactory(client.getResultObjectFactory(),
        statementScope.getStatement().getId());
  }

  /**
   * Cleanup result object factory.
   */
  private void cleanupResultObjectFactory() {
    ResultObjectFactoryUtil.cleanupResultObjectFactory();
  }
}
