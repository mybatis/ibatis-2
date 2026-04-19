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
package com.ibatis.sqlmap.engine.transaction.external;

import com.ibatis.sqlmap.engine.transaction.BaseTransactionConfig;
import com.ibatis.sqlmap.engine.transaction.Transaction;
import com.ibatis.sqlmap.engine.transaction.TransactionException;

import java.sql.SQLException;
import java.util.Properties;

/**
 * The Class ExternalTransactionConfig.
 */
public class ExternalTransactionConfig extends BaseTransactionConfig {

  /** The default auto commit. */
  private boolean defaultAutoCommit = false;

  /** The set auto commit allowed. */
  private boolean setAutoCommitAllowed = true;

  @Override
  public Transaction newTransaction(int transactionIsolation) throws SQLException, TransactionException {
    return new ExternalTransaction(dataSource, defaultAutoCommit, setAutoCommitAllowed, transactionIsolation);
  }

  /**
   * Checks if is default auto commit.
   *
   * @return true, if is default auto commit
   */
  public boolean isDefaultAutoCommit() {
    return defaultAutoCommit;
  }

  /**
   * Sets the default auto commit.
   *
   * @param defaultAutoCommit
   *          the new default auto commit
   */
  public void setDefaultAutoCommit(boolean defaultAutoCommit) {
    this.defaultAutoCommit = defaultAutoCommit;
  }

  /**
   * Checks if is sets the auto commit allowed.
   *
   * @return true, if is sets the auto commit allowed
   */
  public boolean isSetAutoCommitAllowed() {
    return setAutoCommitAllowed;
  }

  /**
   * Sets the sets the auto commit allowed.
   *
   * @param setAutoCommitAllowed
   *          the new sets the auto commit allowed
   */
  public void setSetAutoCommitAllowed(boolean setAutoCommitAllowed) {
    this.setAutoCommitAllowed = setAutoCommitAllowed;
  }

  @Override
  public void setProperties(Properties props) throws SQLException, TransactionException {
    String dacProp = props.getProperty("DefaultAutoCommit");
    String sacaProp = props.getProperty("SetAutoCommitAllowed");
    defaultAutoCommit = "true".equals(dacProp);
    setAutoCommitAllowed = "true".equals(sacaProp) || sacaProp == null;
  }

}
