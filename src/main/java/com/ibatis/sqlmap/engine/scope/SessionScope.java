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

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.ibatis.sqlmap.client.SqlMapTransactionManager;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.transaction.Transaction;
import com.ibatis.sqlmap.engine.transaction.TransactionState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A Session based implementation of the Scope interface.
 */
public class SessionScope {

  /** The next id. */
  private static long nextId;

  /** The id. */
  private long id;

  /** The sql map client. */
  // Used by Any
  private SqlMapClient sqlMapClient;

  /** The sql map executor. */
  private SqlMapExecutor sqlMapExecutor;

  /** The sql map tx mgr. */
  private SqlMapTransactionManager sqlMapTxMgr;

  /** The request stack depth. */
  private int requestStackDepth;

  /** The transaction. */
  // Used by TransactionManager
  private Transaction transaction;

  /** The transaction state. */
  private TransactionState transactionState;

  /** The saved transaction state. */
  // Used by SqlMapExecutorDelegate.setUserProvidedTransaction()
  private TransactionState savedTransactionState;

  /** The in batch. */
  // Used by StandardSqlMapClient and GeneralStatement
  private boolean inBatch;

  /** The batch. */
  // Used by SqlExecutor
  private Object batch;

  /** The commit required. */
  private boolean commitRequired;

  /** The prepared statements. */
  private Map preparedStatements;

  /**
   * Default constructor.
   */
  public SessionScope() {
    this.preparedStatements = new HashMap<>();
    this.inBatch = false;
    this.requestStackDepth = 0;
    this.id = getNextId();
  }

  /**
   * Get the SqlMapClient for the session.
   *
   * @return - the SqlMapClient
   */
  public SqlMapClient getSqlMapClient() {
    return sqlMapClient;
  }

  /**
   * Set the SqlMapClient for the session.
   *
   * @param sqlMapClient
   *          - the SqlMapClient
   */
  public void setSqlMapClient(SqlMapClient sqlMapClient) {
    this.sqlMapClient = sqlMapClient;
  }

  /**
   * Get the SQL executor for the session.
   *
   * @return - the SQL executor
   */
  public SqlMapExecutor getSqlMapExecutor() {
    return sqlMapExecutor;
  }

  /**
   * Get the SQL executor for the session.
   *
   * @param sqlMapExecutor
   *          - the SQL executor
   */
  public void setSqlMapExecutor(SqlMapExecutor sqlMapExecutor) {
    this.sqlMapExecutor = sqlMapExecutor;
  }

  /**
   * Get the transaction manager.
   *
   * @return - the transaction manager
   */
  public SqlMapTransactionManager getSqlMapTxMgr() {
    return sqlMapTxMgr;
  }

  /**
   * Set the transaction manager.
   *
   * @param sqlMapTxMgr
   *          - the transaction manager
   */
  public void setSqlMapTxMgr(SqlMapTransactionManager sqlMapTxMgr) {
    this.sqlMapTxMgr = sqlMapTxMgr;
  }

  /**
   * Tells us if we are in batch mode or not.
   *
   * @return - true if we are working with a batch
   */
  public boolean isInBatch() {
    return inBatch;
  }

  /**
   * Turn batch mode on or off.
   *
   * @param inBatch
   *          - the switch
   */
  public void setInBatch(boolean inBatch) {
    this.inBatch = inBatch;
  }

  /**
   * Getter for the session transaction.
   *
   * @return - the transaction
   */
  public Transaction getTransaction() {
    return transaction;
  }

  /**
   * Setter for the session transaction.
   *
   * @param transaction
   *          - the transaction
   */
  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  /**
   * Getter for the transaction state of the session.
   *
   * @return - the state
   */
  public TransactionState getTransactionState() {
    return transactionState;
  }

  /**
   * Setter for the transaction state of the session.
   *
   * @param transactionState
   *          - the new transaction state
   */
  public void setTransactionState(TransactionState transactionState) {
    this.transactionState = transactionState;
  }

  /**
   * Getter for the batch of the session.
   *
   * @return - the batch
   */
  public Object getBatch() {
    return batch;
  }

  /**
   * Stter for the batch of the session.
   *
   * @param batch
   *          the new batch
   */
  public void setBatch(Object batch) {
    this.batch = batch;
  }

  /**
   * Get the request stack depth.
   *
   * @return - the stack depth
   */
  public int getRequestStackDepth() {
    return requestStackDepth;
  }

  /**
   * Increment the stack depth by one.
   */
  public void incrementRequestStackDepth() {
    requestStackDepth++;
  }

  /**
   * Decrement the stack depth by one.
   */
  public void decrementRequestStackDepth() {
    requestStackDepth--;
  }

  /**
   * Getter to tell if a commit is required for the session.
   *
   * @return - true if a commit is required
   */
  public boolean isCommitRequired() {
    return commitRequired;
  }

  /**
   * Setter to tell the session that a commit is required for the session.
   *
   * @param commitRequired
   *          - the flag
   */
  public void setCommitRequired(boolean commitRequired) {
    this.commitRequired = commitRequired;
  }

  /**
   * Checks for prepared statement for.
   *
   * @param sql
   *          the sql
   *
   * @return true, if successful
   */
  public boolean hasPreparedStatementFor(String sql) {
    return preparedStatements.containsKey(sql);
  }

  /**
   * Checks for prepared statement.
   *
   * @param ps
   *          the ps
   *
   * @return true, if successful
   */
  public boolean hasPreparedStatement(PreparedStatement ps) {
    return preparedStatements.containsValue(ps);
  }

  /**
   * Gets the prepared statement.
   *
   * @param sql
   *          the sql
   *
   * @return the prepared statement
   *
   * @throws SQLException
   *           the SQL exception
   */
  public PreparedStatement getPreparedStatement(String sql) throws SQLException {
    if (!hasPreparedStatementFor(sql)) {
      throw new SqlMapException("Could not get prepared statement.  This is likely a bug.");
    }
    return (PreparedStatement) preparedStatements.get(sql);
  }

  /**
   * Put prepared statement.
   *
   * @param delegate
   *          the delegate
   * @param sql
   *          the sql
   * @param ps
   *          the ps
   */
  public void putPreparedStatement(SqlMapExecutorDelegate delegate, String sql, PreparedStatement ps) {
    if (delegate.isStatementCacheEnabled()) {
      if (!isInBatch()) {
        if (hasPreparedStatementFor(sql))
          throw new SqlMapException("Duplicate prepared statement found.  This is likely a bug.");
        preparedStatements.put(sql, ps);
      }
    }
  }

  /**
   * Close prepared statements.
   */
  public void closePreparedStatements() {
    Iterator keys = preparedStatements.keySet().iterator();
    while (keys.hasNext()) {
      PreparedStatement ps = (PreparedStatement) preparedStatements.get(keys.next());
      try {
        ps.close();
      } catch (Exception e) {
        // ignore -- we don't care if this fails at this point.
      }
    }
    preparedStatements.clear();
  }

  /**
   * Cleanup.
   */
  public void cleanup() {
    closePreparedStatements();
    preparedStatements.clear();
  }

  @Override
  public boolean equals(Object parameterObject) {
    if (this == parameterObject)
      return true;
    if (!(parameterObject instanceof SessionScope))
      return false;
    final SessionScope sessionScope = (SessionScope) parameterObject;
    if (id != sessionScope.id)
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  /**
   * Method to get a unique ID.
   *
   * @return - the new ID
   */
  public synchronized static long getNextId() {
    return nextId++;
  }

  /**
   * Saves the current transaction state.
   */
  public void saveTransactionState() {
    savedTransactionState = transactionState;
  }

  /**
   * Restores the previously saved transaction state.
   */
  public void recallTransactionState() {
    transactionState = savedTransactionState;
  }

}
