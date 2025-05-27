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
package com.ibatis.sqlmap.engine.mapping.sql;

import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * The Interface Sql.
 */
public interface Sql {

  /**
   * Gets the sql.
   *
   * @param statementScope
   *          the statement scope
   * @param parameterObject
   *          the parameter object
   *
   * @return the sql
   */
  String getSql(StatementScope statementScope, Object parameterObject);

  /**
   * Gets the parameter map.
   *
   * @param statementScope
   *          the statement scope
   * @param parameterObject
   *          the parameter object
   *
   * @return the parameter map
   */
  ParameterMap getParameterMap(StatementScope statementScope, Object parameterObject);

  /**
   * Gets the result map.
   *
   * @param statementScope
   *          the statement scope
   * @param parameterObject
   *          the parameter object
   *
   * @return the result map
   */
  ResultMap getResultMap(StatementScope statementScope, Object parameterObject);

  /**
   * Cleanup.
   *
   * @param statementScope
   *          the statement scope
   */
  void cleanup(StatementScope statementScope);

}
