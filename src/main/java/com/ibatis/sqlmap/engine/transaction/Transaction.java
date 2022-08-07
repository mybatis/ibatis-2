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
package com.ibatis.sqlmap.engine.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Interface Transaction.
 */
public interface Transaction {

  /**
   * Commit.
   *
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public void commit() throws SQLException, TransactionException;

  /**
   * Rollback.
   *
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public void rollback() throws SQLException, TransactionException;

  /**
   * Close.
   *
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public void close() throws SQLException, TransactionException;

  /**
   * Gets the connection.
   *
   * @return the connection
   *
   * @throws SQLException
   *           the SQL exception
   * @throws TransactionException
   *           the transaction exception
   */
  public Connection getConnection() throws SQLException, TransactionException;

}
