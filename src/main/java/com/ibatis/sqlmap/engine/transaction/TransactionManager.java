/*
 *  Copyright 2004 Clinton Begin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ibatis.sqlmap.engine.transaction;

import com.ibatis.sqlmap.engine.scope.SessionScope;

import java.sql.SQLException;

public class TransactionManager {

  private TransactionConfig config;

  public TransactionManager(TransactionConfig transactionConfig) {
    this.config = transactionConfig;
  }

  public void begin(SessionScope sessionScope) throws SQLException, TransactionException {
    begin(sessionScope, IsolationLevel.UNSET_ISOLATION_LEVEL);
  }

  public void begin(SessionScope sessionScope, int transactionIsolation) throws SQLException, TransactionException {
    Transaction trans = sessionScope.getTransaction();
    TransactionState state = sessionScope.getTransactionState();
    if (state == TransactionState.STATE_STARTED) {
      throw new TransactionException("TransactionManager could not start a new transaction.  " +
          "A transaction is already started.");
    } else if (state == TransactionState.STATE_USER_PROVIDED) {
      throw new TransactionException("TransactionManager could not start a new transaction.  " +
          "A user provided connection is currently being used by this session.  " +
          "The calling .setUserConnection (null) will clear the user provided transaction.");
    }

    trans = config.newTransaction(transactionIsolation);
    sessionScope.setCommitRequired(false);

    sessionScope.setTransaction(trans);
    sessionScope.setTransactionState(TransactionState.STATE_STARTED);
  }

  public void commit(SessionScope sessionScope) throws SQLException, TransactionException {
    Transaction trans = sessionScope.getTransaction();
    TransactionState state = sessionScope.getTransactionState();
    if (state == TransactionState.STATE_USER_PROVIDED) {
      throw new TransactionException("TransactionManager could not commit.  " +
          "A user provided connection is currently being used by this session.  " +
          "You must call the commit() method of the Connection directly.  " +
          "The calling .setUserConnection (null) will clear the user provided transaction.");
    } else if (state != TransactionState.STATE_STARTED && state != TransactionState.STATE_COMMITTED ) {
      throw new TransactionException("TransactionManager could not commit.  No transaction is started.");
    }
    if (sessionScope.isCommitRequired() || config.isForceCommit()) {
      trans.commit();
      sessionScope.setCommitRequired(false);
    }
    sessionScope.setTransactionState(TransactionState.STATE_COMMITTED);
  }

  public void end(SessionScope sessionScope) throws SQLException, TransactionException {
    Transaction trans = sessionScope.getTransaction();
    TransactionState state = sessionScope.getTransactionState();

    if (state == TransactionState.STATE_USER_PROVIDED) {
      throw new TransactionException("TransactionManager could not end this transaction.  " +
          "A user provided connection is currently being used by this session.  " +
          "You must call the rollback() method of the Connection directly.  " +
          "The calling .setUserConnection (null) will clear the user provided transaction.");
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

  public TransactionConfig getConfig() {
    return config;
  }

}