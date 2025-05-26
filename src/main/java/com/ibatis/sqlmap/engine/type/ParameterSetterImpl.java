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

import com.ibatis.sqlmap.client.extensions.ParameterSetter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * A ParameterSetter implementation.
 */
public class ParameterSetterImpl implements ParameterSetter {

  /** The ps. */
  private PreparedStatement ps;

  /** The index. */
  private int index;

  /**
   * Creates an instance for a PreparedStatement and column index.
   *
   * @param statement
   *          - the PreparedStatement
   * @param columnIndex
   *          - the column index
   */
  public ParameterSetterImpl(PreparedStatement statement, int columnIndex) {
    this.ps = statement;
    this.index = columnIndex;
  }

  @Override
  public void setArray(Array x) throws SQLException {
    ps.setArray(index, x);
  }

  @Override
  public void setAsciiStream(InputStream x, int length) throws SQLException {
    ps.setAsciiStream(index, x, length);
  }

  @Override
  public void setBigDecimal(BigDecimal x) throws SQLException {
    ps.setBigDecimal(index, x);
  }

  @Override
  public void setBinaryStream(InputStream x, int length) throws SQLException {
    ps.setBinaryStream(index, x, length);
  }

  @Override
  public void setBlob(Blob x) throws SQLException {
    ps.setBlob(index, x);
  }

  @Override
  public void setBoolean(boolean x) throws SQLException {
    ps.setBoolean(index, x);
  }

  @Override
  public void setByte(byte x) throws SQLException {
    ps.setByte(index, x);
  }

  @Override
  public void setBytes(byte x[]) throws SQLException {
    ps.setBytes(index, x);
  }

  @Override
  public void setCharacterStream(Reader reader, int length) throws SQLException {
    ps.setCharacterStream(index, reader, length);
  }

  @Override
  public void setClob(Clob x) throws SQLException {
    ps.setClob(index, x);
  }

  @Override
  public void setDate(Date x) throws SQLException {
    ps.setDate(index, x);
  }

  @Override
  public void setDate(Date x, Calendar cal) throws SQLException {
    ps.setDate(index, x, cal);
  }

  @Override
  public void setDouble(double x) throws SQLException {
    ps.setDouble(index, x);
  }

  @Override
  public void setFloat(float x) throws SQLException {
    ps.setFloat(index, x);
  }

  @Override
  public void setInt(int x) throws SQLException {
    ps.setInt(index, x);
  }

  @Override
  public void setLong(long x) throws SQLException {
    ps.setLong(index, x);
  }

  @Override
  public void setNull(int sqlType) throws SQLException {
    ps.setNull(index, sqlType);
  }

  @Override
  public void setNull(int sqlType, String typeName) throws SQLException {
    ps.setNull(index, sqlType, typeName);
  }

  @Override
  public void setObject(Object x) throws SQLException {
    ps.setObject(index, x);
  }

  @Override
  public void setObject(Object x, int targetSqlType) throws SQLException {
    ps.setObject(index, x, targetSqlType);
  }

  @Override
  public void setObject(Object x, int targetSqlType, int scale) throws SQLException {
    ps.setObject(index, x, scale);
  }

  @Override
  public void setRef(Ref x) throws SQLException {
    ps.setRef(index, x);
  }

  @Override
  public void setShort(short x) throws SQLException {
    ps.setShort(index, x);
  }

  @Override
  public void setString(String x) throws SQLException {
    ps.setString(index, x);
  }

  @Override
  public void setTime(Time x) throws SQLException {
    ps.setTime(index, x);
  }

  @Override
  public void setTime(Time x, Calendar cal) throws SQLException {
    ps.setTime(index, x, cal);
  }

  @Override
  public void setTimestamp(Timestamp x) throws SQLException {
    ps.setTimestamp(index, x);
  }

  @Override
  public void setTimestamp(Timestamp x, Calendar cal) throws SQLException {
    ps.setTimestamp(index, x, cal);
  }

  @Override
  public void setURL(URL x) throws SQLException {
    ps.setURL(index, x);
  }

  @Override
  public PreparedStatement getPreparedStatement() {
    return ps;
  }

  @Override
  public int getParameterIndex() {
    return index;
  }
}
