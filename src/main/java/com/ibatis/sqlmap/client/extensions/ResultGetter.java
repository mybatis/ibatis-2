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
package com.ibatis.sqlmap.client.extensions;

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
 * Allows values to be retrieved from the underlying result set. TypeHandlerCallback implementations use this interface
 * to get values that they can subsequently manipulate before having them returned. Each of these methods has a
 * corresponding method on the ResultSet (or CallableStatement) class, the only difference being that there is no need
 * to specify the column name or index with these methods.
 * <p>
 * <b>NOTE:</b> There is no need to implement this. The implementation will be passed into the TypeHandlerCallback
 * automatically.
 */
public interface ResultGetter {

  /**
   * Gets an array from the underlying result set.
   *
   * @return - the array
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Array getArray() throws SQLException;

  /**
   * Gets a BigDecimal from the underlying result set.
   *
   * @return - the BigDecimal
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  BigDecimal getBigDecimal() throws SQLException;

  /**
   * Gets a Blob from the underlying result set.
   *
   * @return - the Blob
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Blob getBlob() throws SQLException;

  /**
   * Gets a boolean from the underlying result set.
   *
   * @return - the boolean
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  boolean getBoolean() throws SQLException;

  /**
   * Gets a byte from the underlying result set.
   *
   * @return - the byte
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  byte getByte() throws SQLException;

  /**
   * Gets a byte[] from the underlying result set.
   *
   * @return - the byte[]
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  byte[] getBytes() throws SQLException;

  /**
   * Gets a Clob from the underlying result set.
   *
   * @return - the Clob
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Clob getClob() throws SQLException;

  /**
   * Gets a Date from the underlying result set.
   *
   * @return - the Date
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Date getDate() throws SQLException;

  /**
   * Gets a Date from the underlying result set using a calendar.
   *
   * @param cal
   *          - the Calendar
   *
   * @return - the Date
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Date getDate(Calendar cal) throws SQLException;

  /**
   * Gets a double from the underlying result set.
   *
   * @return - the double
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  double getDouble() throws SQLException;

  /**
   * Gets a float from the underlying result set.
   *
   * @return - the float
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  float getFloat() throws SQLException;

  /**
   * Gets an int from the underlying result set.
   *
   * @return - the int
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  int getInt() throws SQLException;

  /**
   * Gets a long from the underlying result set.
   *
   * @return - the long
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  long getLong() throws SQLException;

  /**
   * Gets an Object from the underlying result set.
   *
   * @return - the Object
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Object getObject() throws SQLException;

  /**
   * Gets an Object from the underlying result set using a Map.
   *
   * @param map
   *          - the Map
   *
   * @return - the Object
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Object getObject(Map map) throws SQLException;

  /**
   * Gets a Ref from the underlying result set.
   *
   * @return - the Ref
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Ref getRef() throws SQLException;

  /**
   * Gets a short from the underlying result set.
   *
   * @return - the short
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  short getShort() throws SQLException;

  /**
   * Gets a String from the underlying result set.
   *
   * @return - the String
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  String getString() throws SQLException;

  /**
   * Gets a Time from the underlying result set.
   *
   * @return - the Time
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Time getTime() throws SQLException;

  /**
   * Gets a Time from the underlying result set using a Calendar.
   *
   * @param cal
   *          - the Calendar
   *
   * @return - the Time
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Time getTime(Calendar cal) throws SQLException;

  /**
   * Gets a Timestamp from the underlying result set.
   *
   * @return - the Timestamp
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Timestamp getTimestamp() throws SQLException;

  /**
   * Gets a Timestamp from the underlying result set.
   *
   * @param cal
   *          - the Calendar
   *
   * @return - the Timestamp
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  Timestamp getTimestamp(Calendar cal) throws SQLException;

  /**
   * Gets a URL from the underlying result set.
   *
   * @return - the URL
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  URL getURL() throws SQLException;

  /**
   * Tells if the field was null.
   *
   * @return - true if it was null
   *
   * @throws SQLException
   *           - if the underlying result set throws an exception
   */
  boolean wasNull() throws SQLException;

  /**
   * Returns the underlying ResultSet...be careful!
   *
   * @return a ResultSet instance.
   */
  ResultSet getResultSet();

  /**
   * Returns the name of the column being got in the underlying ResultSet. May be <code>null</code> in which case the
   * <code>getColumnIndex</code> method should be used.
   *
   * @return the column name (may be null)
   */
  String getColumnName();

  /**
   * Returns the index of the column being got in the underlying ResultSet. Only use this method if the value returned
   * from <code>getColumnName</code> is null.
   *
   * @return the index of the column (if zero then use the column name)
   */
  int getColumnIndex();
}
