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
 * The Class IsolationLevel.
 */
public class IsolationLevel {

  /** The Constant UNSET_ISOLATION_LEVEL. */
  public static final int UNSET_ISOLATION_LEVEL = -9999;

  /** The isolation level. */
  private int isolationLevel = UNSET_ISOLATION_LEVEL;

  /** The original isolation level. */
  private int originalIsolationLevel = UNSET_ISOLATION_LEVEL;

  /**
   * Sets the isolation level.
   *
   * @param isolationLevel
   *          the new isolation level
   */
  public void setIsolationLevel(int isolationLevel) {
    this.isolationLevel = isolationLevel;
  }

  /**
   * Apply isolation level.
   *
   * @param conn
   *          the conn
   *
   * @throws SQLException
   *           the SQL exception
   */
  public void applyIsolationLevel(Connection conn) throws SQLException {
    if (isolationLevel != UNSET_ISOLATION_LEVEL) {
      originalIsolationLevel = conn.getTransactionIsolation();
      if (isolationLevel != originalIsolationLevel) {
        conn.setTransactionIsolation(isolationLevel);
      }
    }
  }

  /**
   * Restore isolation level.
   *
   * @param conn
   *          the conn
   *
   * @throws SQLException
   *           the SQL exception
   */
  public void restoreIsolationLevel(Connection conn) throws SQLException {
    if (isolationLevel != originalIsolationLevel) {
      conn.setTransactionIsolation(originalIsolationLevel);
    }
  }

}
