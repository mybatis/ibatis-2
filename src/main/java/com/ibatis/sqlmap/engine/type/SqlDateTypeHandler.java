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
package com.ibatis.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL Date implementation of TypeHandler.
 */
public class SqlDateTypeHandler extends BaseTypeHandler implements TypeHandler {

  /** The Constant DATE_FORMAT. */
  private static final String DATE_FORMAT = "yyyy/MM/dd";

  @Override
  public void setParameter(PreparedStatement ps, int i, Object parameter, String jdbcType) throws SQLException {
    ps.setDate(i, (java.sql.Date) parameter);
  }

  @Override
  public Object getResult(ResultSet rs, String columnName) throws SQLException {
    Object sqlDate = rs.getDate(columnName);
    if (rs.wasNull()) {
      return null;
    }
    return sqlDate;
  }

  @Override
  public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
    Object sqlDate = rs.getDate(columnIndex);
    if (rs.wasNull()) {
      return null;
    }
    return sqlDate;
  }

  @Override
  public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
    Object sqlDate = cs.getDate(columnIndex);
    if (cs.wasNull()) {
      return null;
    }
    return sqlDate;
  }

  @Override
  public Object valueOf(String s) {
    return SimpleDateFormatter.format(DATE_FORMAT, s);
  }

}
