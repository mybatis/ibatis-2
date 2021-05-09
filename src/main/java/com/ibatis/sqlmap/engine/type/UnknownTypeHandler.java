/*
 * Copyright 2004-2021 the original author or authors.
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
package com.ibatis.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of TypeHandler for dealing with unknown types.
 */
public class UnknownTypeHandler extends BaseTypeHandler implements TypeHandler {

  /** The factory. */
  private TypeHandlerFactory factory;

  /**
   * Constructor to create via a factory.
   *
   * @param factory
   *          - the factory to associate this with
   */
  public UnknownTypeHandler(TypeHandlerFactory factory) {
    this.factory = factory;
  }

  public void setParameter(PreparedStatement ps, int i, Object parameter, String jdbcType) throws SQLException {
    Class searchClass = parameter.getClass();
    if (searchClass == null) {
      searchClass = parameter.getClass();
    }
    TypeHandler handler = factory.getTypeHandler(searchClass, jdbcType);
    handler.setParameter(ps, i, parameter, jdbcType);
  }

  public Object getResult(ResultSet rs, String columnName) throws SQLException {
    Object object = rs.getObject(columnName);
    if (rs.wasNull()) {
      return null;
    } else {
      return object;
    }
  }

  public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
    Object object = rs.getObject(columnIndex);
    if (rs.wasNull()) {
      return null;
    } else {
      return object;
    }
  }

  public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
    Object object = cs.getObject(columnIndex);
    if (cs.wasNull()) {
      return null;
    } else {
      return object;
    }
  }

  public Object valueOf(String s) {
    return s;
  }

  @Override
  public boolean equals(Object object, String string) {
    if (object == null || string == null) {
      return object == string;
    } else {
      TypeHandler handler = factory.getTypeHandler(object.getClass());
      Object castedObject = handler.valueOf(string);
      return object.equals(castedObject);
    }
  }

}
