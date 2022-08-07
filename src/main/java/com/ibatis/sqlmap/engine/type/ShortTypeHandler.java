/*
 * Copyright 2004-2021 the original author or authors.
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
 * Short implementation of TypeHandler.
 */
public class ShortTypeHandler extends BaseTypeHandler implements TypeHandler {

  public void setParameter(PreparedStatement ps, int i, Object parameter, String jdbcType) throws SQLException {
    ps.setShort(i, ((Short) parameter).shortValue());
  }

  public Object getResult(ResultSet rs, String columnName) throws SQLException {
    short s = rs.getShort(columnName);
    if (rs.wasNull()) {
      return null;
    } else {
      return Short.valueOf(s);
    }
  }

  public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
    short s = rs.getShort(columnIndex);
    if (rs.wasNull()) {
      return null;
    } else {
      return Short.valueOf(s);
    }
  }

  public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
    short s = cs.getShort(columnIndex);
    if (cs.wasNull()) {
      return null;
    } else {
      return Short.valueOf(s);
    }
  }

  public Object valueOf(String s) {
    return Short.valueOf(s);
  }

}
