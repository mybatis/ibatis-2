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
package com.ibatis.sqlmap.engine.mapping.sql.raw;

import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * A non-executable SQL container simply for communicating raw SQL around the framework.
 */
public class RawSql implements Sql {

  /** The sql. */
  private String sql;

  /**
   * Instantiates a new raw sql.
   *
   * @param sql
   *          the sql
   */
  public RawSql(String sql) {
    this.sql = sql;
  }

  public String getSql(StatementScope statementScope, Object parameterObject) {
    return sql;
  }

  public ParameterMap getParameterMap(StatementScope statementScope, Object parameterObject) {
    throw new RuntimeException("Method not implemented on RawSql.");
  }

  public ResultMap getResultMap(StatementScope statementScope, Object parameterObject) {
    throw new RuntimeException("Method not implemented on RawSql.");
  }

  public void cleanup(StatementScope statementScope) {
    throw new RuntimeException("Method not implemented on RawSql.");
  }
}
