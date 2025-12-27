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
package com.ibatis.sqlmap.engine.mapping.sql.simple;

import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.scope.StatementScope;

import java.util.StringTokenizer;

/**
 * The Class SimpleDynamicSql.
 */
public class SimpleDynamicSql implements Sql {

  /** The Constant PROBE. */
  private static final Probe PROBE = ProbeFactory.getProbe();

  /** The Constant ELEMENT_TOKEN. */
  private static final String ELEMENT_TOKEN = "$";

  /** The sql statement. */
  private String sqlStatement;

  /** The delegate. */
  private SqlMapExecutorDelegate delegate;

  /**
   * Instantiates a new simple dynamic sql.
   *
   * @param delegate
   *          the delegate
   * @param sqlStatement
   *          the sql statement
   */
  public SimpleDynamicSql(SqlMapExecutorDelegate delegate, String sqlStatement) {
    this.delegate = delegate;
    this.sqlStatement = sqlStatement;
  }

  @Override
  public String getSql(StatementScope statementScope, Object parameterObject) {
    return processDynamicElements(sqlStatement, parameterObject);
  }

  @Override
  public ParameterMap getParameterMap(StatementScope statementScope, Object parameterObject) {
    return statementScope.getParameterMap();
  }

  @Override
  public ResultMap getResultMap(StatementScope statementScope, Object parameterObject) {
    return statementScope.getResultMap();
  }

  @Override
  public void cleanup(StatementScope statementScope) {
  }

  /**
   * Checks if is simple dynamic sql.
   *
   * @param sql
   *          the sql
   *
   * @return true, if is simple dynamic sql
   */
  public static boolean isSimpleDynamicSql(String sql) {
    return sql != null && sql.indexOf(ELEMENT_TOKEN) > -1;
  }

  /**
   * Process dynamic elements.
   *
   * @param sql
   *          the sql
   * @param parameterObject
   *          the parameter object
   *
   * @return the string
   */
  private String processDynamicElements(String sql, Object parameterObject) {
    StringTokenizer parser = new StringTokenizer(sql, ELEMENT_TOKEN, true);
    StringBuilder newSql = new StringBuilder();

    String token = null;
    String lastToken = null;
    while (parser.hasMoreTokens()) {
      token = parser.nextToken();

      if (ELEMENT_TOKEN.equals(lastToken)) {
        if (ELEMENT_TOKEN.equals(token)) {
          newSql.append(ELEMENT_TOKEN);
          token = null;
        } else {

          Object value = null;
          if (parameterObject != null) {
            if (delegate.getTypeHandlerFactory().hasTypeHandler(parameterObject.getClass())) {
              value = parameterObject;
            } else {
              value = PROBE.getObject(parameterObject, token);
            }
          }
          if (value != null) {
            newSql.append(String.valueOf(value));
          }

          token = parser.nextToken();
          if (!ELEMENT_TOKEN.equals(token)) {
            throw new SqlMapException("Unterminated dynamic element in sql (" + sql + ").");
          }
          token = null;
        }
      } else {
        if (!ELEMENT_TOKEN.equals(token)) {
          newSql.append(token);
        }
      }

      lastToken = token;
    }

    return newSql.toString();
  }

}
