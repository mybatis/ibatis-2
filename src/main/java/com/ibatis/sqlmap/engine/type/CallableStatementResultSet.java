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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * A way to make a CallableStatement look like a ResultSet.
 */
public class CallableStatementResultSet implements ResultSet {

  /** The cs. */
  private CallableStatement cs;

  /**
   * Constructor to stretch a ResultSet interface over a CallableStatement.
   *
   * @param cs
   *          - the CallableStatement
   */
  public CallableStatementResultSet(CallableStatement cs) {
    this.cs = cs;
  }

  @Override
  public boolean absolute(int row) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void afterLast() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void beforeFirst() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void cancelRowUpdates() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void clearWarnings() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void close() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void deleteRow() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public int findColumn(String columnName) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean first() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public Array getArray(String colName) throws SQLException {
    return cs.getArray(colName);
  }

  @Override
  public Array getArray(int i) throws SQLException {
    return cs.getArray(i);
  }

  @Override
  public InputStream getAsciiStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public InputStream getAsciiStream(String columnName) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
    return cs.getBigDecimal(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public BigDecimal getBigDecimal(String columnName) throws SQLException {
    return cs.getBigDecimal(columnName);
  }

  @Override
  public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public InputStream getBinaryStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public InputStream getBinaryStream(String columnName) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public Blob getBlob(String colName) throws SQLException {
    return cs.getBlob(colName);
  }

  @Override
  public Blob getBlob(int i) throws SQLException {
    return cs.getBlob(i);
  }

  @Override
  public boolean getBoolean(int columnIndex) throws SQLException {
    return cs.getBoolean(columnIndex);
  }

  @Override
  public boolean getBoolean(String columnName) throws SQLException {
    return cs.getBoolean(columnName);
  }

  @Override
  public byte getByte(int columnIndex) throws SQLException {
    return cs.getByte(columnIndex);
  }

  @Override
  public byte getByte(String columnName) throws SQLException {
    return cs.getByte(columnName);
  }

  @Override
  public byte[] getBytes(int columnIndex) throws SQLException {
    return cs.getBytes(columnIndex);
  }

  @Override
  public byte[] getBytes(String columnName) throws SQLException {
    return cs.getBytes(columnName);
  }

  @Override
  public Reader getCharacterStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public Reader getCharacterStream(String columnName) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public Clob getClob(String colName) throws SQLException {
    return cs.getClob(colName);
  }

  @Override
  public Clob getClob(int i) throws SQLException {
    return cs.getClob(i);
  }

  @Override
  public int getConcurrency() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public String getCursorName() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public Date getDate(int columnIndex) throws SQLException {
    return cs.getDate(columnIndex);
  }

  @Override
  public Date getDate(int columnIndex, Calendar cal) throws SQLException {
    return cs.getDate(columnIndex, cal);
  }

  @Override
  public Date getDate(String columnName) throws SQLException {
    return cs.getDate(columnName);
  }

  @Override
  public Date getDate(String columnName, Calendar cal) throws SQLException {
    return cs.getDate(columnName, cal);
  }

  @Override
  public double getDouble(int columnIndex) throws SQLException {
    return cs.getDouble(columnIndex);
  }

  @Override
  public double getDouble(String columnName) throws SQLException {
    return cs.getDouble(columnName);
  }

  @Override
  public int getFetchDirection() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public int getFetchSize() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public float getFloat(int columnIndex) throws SQLException {
    return cs.getFloat(columnIndex);
  }

  @Override
  public float getFloat(String columnName) throws SQLException {
    return cs.getFloat(columnName);
  }

  @Override
  public int getInt(int columnIndex) throws SQLException {
    return cs.getInt(columnIndex);
  }

  @Override
  public int getInt(String columnName) throws SQLException {
    return cs.getInt(columnName);
  }

  @Override
  public long getLong(int columnIndex) throws SQLException {
    return cs.getLong(columnIndex);
  }

  @Override
  public long getLong(String columnName) throws SQLException {
    return cs.getLong(columnName);
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public Object getObject(String colName, Map map) throws SQLException {
    return cs.getObject(colName, map);
  }

  @Override
  public Object getObject(int columnIndex) throws SQLException {
    return cs.getObject(columnIndex);
  }

  @Override
  public Object getObject(String columnName) throws SQLException {
    return cs.getObject(columnName);
  }

  @Override
  public Object getObject(int i, Map map) throws SQLException {
    return cs.getObject(i, map);
  }

  @Override
  public Ref getRef(String colName) throws SQLException {
    return cs.getRef(colName);
  }

  @Override
  public Ref getRef(int i) throws SQLException {
    return cs.getRef(i);
  }

  @Override
  public int getRow() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public short getShort(int columnIndex) throws SQLException {
    return cs.getShort(columnIndex);
  }

  @Override
  public short getShort(String columnName) throws SQLException {
    return cs.getShort(columnName);
  }

  @Override
  public Statement getStatement() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public String getString(int columnIndex) throws SQLException {
    return cs.getString(columnIndex);
  }

  @Override
  public String getString(String columnName) throws SQLException {
    return cs.getString(columnName);
  }

  @Override
  public Time getTime(int columnIndex) throws SQLException {
    return cs.getTime(columnIndex);
  }

  @Override
  public Time getTime(int columnIndex, Calendar cal) throws SQLException {
    return cs.getTime(columnIndex, cal);
  }

  @Override
  public Time getTime(String columnName) throws SQLException {
    return cs.getTime(columnName);
  }

  @Override
  public Time getTime(String columnName, Calendar cal) throws SQLException {
    return cs.getTime(columnName, cal);
  }

  @Override
  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    return cs.getTimestamp(columnIndex);
  }

  @Override
  public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
    return cs.getTimestamp(columnIndex, cal);
  }

  @Override
  public Timestamp getTimestamp(String columnName) throws SQLException {
    return cs.getTimestamp(columnName);
  }

  @Override
  public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
    return cs.getTimestamp(columnName, cal);
  }

  @Override
  public int getType() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public InputStream getUnicodeStream(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public InputStream getUnicodeStream(String columnName) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public URL getURL(int columnIndex) throws SQLException {
    return cs.getURL(columnIndex);
  }

  @Override
  public URL getURL(String columnName) throws SQLException {
    return cs.getURL(columnName);
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void insertRow() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean isAfterLast() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean isBeforeFirst() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean isFirst() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean isLast() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean last() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void moveToCurrentRow() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void moveToInsertRow() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean next() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean previous() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void refreshRow() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean relative(int rows) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean rowDeleted() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean rowInserted() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean rowUpdated() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void setFetchDirection(int direction) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void setFetchSize(int rows) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateArray(int columnIndex, Array x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateArray(String columnName, Array x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBlob(int columnIndex, Blob x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBlob(String columnName, Blob x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBoolean(int columnIndex, boolean x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBoolean(String columnName, boolean x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateByte(int columnIndex, byte x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateByte(String columnName, byte x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBytes(int columnIndex, byte[] x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateBytes(String columnName, byte[] x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateClob(int columnIndex, Clob x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateClob(String columnName, Clob x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateDate(int columnIndex, Date x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateDate(String columnName, Date x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateDouble(int columnIndex, double x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateDouble(String columnName, double x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateFloat(int columnIndex, float x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateFloat(String columnName, float x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateInt(int columnIndex, int x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateInt(String columnName, int x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateLong(int columnIndex, long x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateLong(String columnName, long x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateNull(int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateNull(String columnName) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateObject(int columnIndex, Object x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateObject(String columnName, Object x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateObject(String columnName, Object x, int scale) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateRef(int columnIndex, Ref x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateRef(String columnName, Ref x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateRow() throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateShort(int columnIndex, short x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateShort(String columnName, short x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateString(int columnIndex, String x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateString(String columnName, String x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateTime(int columnIndex, Time x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateTime(String columnName, Time x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
    throw new UnsupportedOperationException("CallableStatement does not support this method.");
  }

  @Override
  public boolean wasNull() throws SQLException {
    return cs.wasNull();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public RowId getRowId(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RowId getRowId(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateRowId(int columnIndex, RowId x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateRowId(String columnLabel, RowId x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public int getHoldability() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isClosed() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void updateNString(int columnIndex, String nString) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNString(String columnLabel, String nString) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public NClob getNClob(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NClob getNClob(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SQLXML getSQLXML(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SQLXML getSQLXML(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public String getNString(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getNString(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Reader getNCharacterStream(int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Reader getNCharacterStream(String columnLabel) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateClob(int columnIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateClob(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(String columnLabel, Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

}
