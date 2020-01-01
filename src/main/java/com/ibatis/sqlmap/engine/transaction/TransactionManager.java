/**
 * Copyright 2004-2020 the original author or authors.
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
package com.ibatis.sqlmap.engine.transaction;

import com.ibatis.sqlmap.engine.scope.SessionScope;

import java.sql.SQLException;

/**
 * The Class TransactionManager.
 */
public class TransactionManager {

  /** The config. */
  private TransactionConfig config;

  /**
   * Instantiates a new transaction manager.
   *
   * @param transactionConfig
   *          the transaction config
   */
  public TransactionManager(TransactionConfig transactionConfig) {
    this.config = transactionConfig;
  }

  /**
   * Begin.
   *
   * @param sessionScope
   *          the session scope
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public void begin(SessionScope sessionScope) throws SQLException, TransactionException {
    begin(sessionScope, IsolationLevel.UNSET_ISOLATION_LEVEL);
  }

  /**
   * Begin.
   *
   * @param sessionScope
   *          the session scope
   * @param transactionIsolation
   *          the transaction isolation
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public void begin(SessionScope sessionScope, int transactionIsolation) throws SQLException, TransactionException {
    Transaction trans = sessionScope.getTransaction();
    TransactionState state = sessionScope.getTransactionState();
    if (state == TransactionState.STATE_STARTED) {
      throw new TransactionException(
          "TransactionManager could not start a new transaction.  " + "A transaction is already started.");
    } else if (state == TransactionState.STATE_USER_PROVIDED) {
      throw new TransactionException("TransactionManager could not start a new transaction.  "
          + "A user provided connection is currently being used by this session.  "
          + "The calling .setUserConnection (null) will clear the user provided transaction.");
    }

    trans = config.newTransaction(transactionIsolation);
    sessionScope.setCommitRequired(false);

    sessionScope.setTransaction(trans);
    sessionScope.setTransactionState(TransactionState.STATE_STARTED);
  }

  /**
   * Commit.
   *
   * @param sessionScope
   *          the session scope
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public void commit(SessionScope sessionScope) throws SQLException, TransactionException {
    Transaction trans = sessionScope.getTransaction();
    TransactionState state = sessionScope.getTransactionState();
    if (state == TransactionState.STATE_USER_PROVIDED) {
      throw new TransactionException("TransactionManager could not commit.  "
          + "A user provided connection is currently being used by this session.  "
          + "You must call the commit() method of the Connection directly.  "
          + "The calling .setUserConnection (null) will clear the user provided transaction.");
    } else if (state != TransactionState.STATE_STARTED && state != TransactionState.STATE_COMMITTED) {
      throw new TransactionException("TransactionManager could not commit.  No transaction is started.");
    }
    if (sessionScope.isCommitRequired() || config.isForceCommit()) {
      trans.commit();
      sessionScope.setCommitRequired(false);
    }
    sessionScope.setTransactionState(TransactionState.STATE_COMMITTED);
  }

  /**
   * End.
   *
   * @param sessionScope
   *          the session scope
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public void end(SessionScope sessionScope) throws SQLException, TransactionException {
    Transaction trans = sessionScope.getTransaction();
    TransactionState state = sessionScope.getTransactionState();

    if (state == TransactionState.STATE_USER_PROVIDED) {
      throw new TransactionException("TransactionManager could not end this transaction.  "
          + "A user provided connection is currently being used by this session.  "
          + "You must call the rollback() method of the Connection directly.  "
          + "The calling .setUserConnection (null) will clear the user provided transaction.");
    }

    try {
      if (trans != null) {
        try {
          if (state != TransactionState.STATE_COMMITTED) {
            if (sessionScope.isCommitRequired() || config.isForceCommit()) {
              trans.rollback();
              sessionScope.setCommitRequired(false);
            }
          }
        } finally {
          sessionScope.closePreparedStatements();
          trans.close();
        }
      }
    } finally {
      sessionScope.setTransaction(null);
      sessionScope.setTransactionState(TransactionState.STATE_ENDED);
    }
  }

  /**
   * Gets the config.
   *
   * @return the config
   */
  public TransactionConfig getConfig() {
    return config;
  }

}
