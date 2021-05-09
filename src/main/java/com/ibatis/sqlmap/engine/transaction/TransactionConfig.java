/*
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

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * The Interface TransactionConfig.
 */
public interface TransactionConfig {

  /**
   * New transaction.
   *
   * @param transactionIsolation
   *          the transaction isolation
   * @return the transaction
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  Transaction newTransaction(int transactionIsolation) throws SQLException, TransactionException;

  /**
   * Gets the data source.
   *
   * @return the data source
   */
  DataSource getDataSource();

  /**
   * Sets the data source.
   *
   * @param ds
   *          the new data source
   */
  void setDataSource(DataSource ds);

  /**
   * This should not be used and is here purely to avoid spring integration from breaking.
   *
   * @return -1
   * @deprecated
   */
  int getMaximumConcurrentTransactions();

  /**
   * This should not be used. It does nothing and is here purely to prevent Spring integration from breaking
   *
   * @param maximumConcurrentTransactions
   *          the new maximum concurrent transactions
   * @deprecated
   */
  void setMaximumConcurrentTransactions(int maximumConcurrentTransactions);

  /**
   * Checks if is force commit.
   *
   * @return true, if is force commit
   */
  boolean isForceCommit();

  /**
   * Sets the force commit.
   *
   * @param forceCommit
   *          the new force commit
   */
  void setForceCommit(boolean forceCommit);

  /**
   * This method should call setProperties. It is here simply to ease transition
   *
   * @param props
   *          - Properties
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   * @deprecated
   */
  void initialize(Properties props) throws SQLException, TransactionException;

  /**
   * Sets the properties.
   *
   * @param props
   *          the new properties
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  void setProperties(Properties props) throws SQLException, TransactionException;

}
