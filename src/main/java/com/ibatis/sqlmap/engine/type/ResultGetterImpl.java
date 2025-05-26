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

import com.ibatis.sqlmap.client.extensions.ResultGetter;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * A ResultGetter implementation.
 */
public class ResultGetterImpl implements ResultGetter {

  /** The rs. */
  private ResultSet rs;

  /** The name. */
  private String name;

  /** The index. */
  private int index;

  /**
   * Creates an instance for a PreparedStatement and column index.
   *
   * @param resultSet
   *          - the result set
   * @param columnIndex
   *          - the column index
   */
  public ResultGetterImpl(ResultSet resultSet, int columnIndex) {
    this.rs = resultSet;
    this.index = columnIndex;
  }

  /**
   * Creates an instance for a PreparedStatement and column name.
   *
   * @param resultSet
   *          - the result set
   * @param columnName
   *          - the column index
   */
  public ResultGetterImpl(ResultSet resultSet, String columnName) {
    this.rs = resultSet;
    this.name = columnName;
  }

  @Override
  public Array getArray() throws SQLException {
    if (name != null) {
      return rs.getArray(name);
    }
    return rs.getArray(index);
  }

  @Override
  public BigDecimal getBigDecimal() throws SQLException {
    if (name != null) {
      return rs.getBigDecimal(name);
    }
    return rs.getBigDecimal(index);
  }

  @Override
  public Blob getBlob() throws SQLException {
    if (name != null) {
      return rs.getBlob(name);
    }
    return rs.getBlob(index);
  }

  @Override
  public boolean getBoolean() throws SQLException {
    if (name != null) {
      return rs.getBoolean(name);
    }
    return rs.getBoolean(index);
  }

  @Override
  public byte getByte() throws SQLException {
    if (name != null) {
      return rs.getByte(name);
    }
    return rs.getByte(index);
  }

  @Override
  public byte[] getBytes() throws SQLException {
    if (name != null) {
      return rs.getBytes(name);
    }
    return rs.getBytes(index);
  }

  @Override
  public Clob getClob() throws SQLException {
    if (name != null) {
      return rs.getClob(name);
    }
    return rs.getClob(index);
  }

  @Override
  public Date getDate() throws SQLException {
    if (name != null) {
      return rs.getDate(name);
    }
    return rs.getDate(index);
  }

  @Override
  public Date getDate(Calendar cal) throws SQLException {
    if (name != null) {
      return rs.getDate(name, cal);
    }
    return rs.getDate(index, cal);
  }

  @Override
  public double getDouble() throws SQLException {
    if (name != null) {
      return rs.getDouble(name);
    }
    return rs.getDouble(index);
  }

  @Override
  public float getFloat() throws SQLException {
    if (name != null) {
      return rs.getFloat(name);
    }
    return rs.getFloat(index);
  }

  @Override
  public int getInt() throws SQLException {
    if (name != null) {
      return rs.getInt(name);
    }
    return rs.getInt(index);
  }

  @Override
  public long getLong() throws SQLException {
    if (name != null) {
      return rs.getLong(name);
    }
    return rs.getLong(index);
  }

  @Override
  public Object getObject() throws SQLException {
    if (name != null) {
      return rs.getObject(name);
    }
    return rs.getObject(index);
  }

  @Override
  public Object getObject(Map map) throws SQLException {
    if (name != null) {
      return rs.getObject(name, map);
    }
    return rs.getObject(index, map);
  }

  @Override
  public Ref getRef() throws SQLException {
    if (name != null) {
      return rs.getRef(name);
    }
    return rs.getRef(index);
  }

  @Override
  public short getShort() throws SQLException {
    if (name != null) {
      return rs.getShort(name);
    }
    return rs.getShort(index);
  }

  @Override
  public String getString() throws SQLException {
    if (name != null) {
      return rs.getString(name);
    }
    return rs.getString(index);
  }

  @Override
  public Time getTime() throws SQLException {
    if (name != null) {
      return rs.getTime(name);
    }
    return rs.getTime(index);
  }

  @Override
  public Time getTime(Calendar cal) throws SQLException {
    if (name != null) {
      return rs.getTime(name);
    }
    return rs.getTime(index);
  }

  @Override
  public Timestamp getTimestamp() throws SQLException {
    if (name != null) {
      return rs.getTimestamp(name);
    }
    return rs.getTimestamp(index);
  }

  @Override
  public Timestamp getTimestamp(Calendar cal) throws SQLException {
    if (name != null) {
      return rs.getTimestamp(name, cal);
    }
    return rs.getTimestamp(index, cal);
  }

  @Override
  public URL getURL() throws SQLException {
    if (name != null) {
      return rs.getURL(name);
    }
    return rs.getURL(index);
  }

  @Override
  public boolean wasNull() throws SQLException {
    return rs.wasNull();
  }

  @Override
  public ResultSet getResultSet() {
    return rs;
  }

  @Override
  public int getColumnIndex() {
    return index;
  }

  @Override
  public String getColumnName() {
    return name;
  }
}
