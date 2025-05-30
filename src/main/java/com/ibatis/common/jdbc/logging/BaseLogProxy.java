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
package com.ibatis.common.jdbc.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for proxies to do logging.
 */
public class BaseLogProxy {

  /** The next id. */
  private static int nextId = 100000;

  /** The Constant SET_METHODS. */
  protected static final Set SET_METHODS = new HashSet<>();

  /** The Constant GET_METHODS. */
  protected static final Set GET_METHODS = new HashSet<>();

  /** The Constant EXECUTE_METHODS. */
  protected static final Set EXECUTE_METHODS = new HashSet<>();

  /** The column map. */
  private Map columnMap = new HashMap<>();

  /** The column names. */
  private List columnNames = new ArrayList<>();

  /** The column values. */
  private List columnValues = new ArrayList<>();

  /** The id. */
  protected int id;

  /**
   * Default constructor.
   */
  public BaseLogProxy() {
    id = getNextId();
  }

  static {
    SET_METHODS.add("setString");
    SET_METHODS.add("setInt");
    SET_METHODS.add("setByte");
    SET_METHODS.add("setShort");
    SET_METHODS.add("setLong");
    SET_METHODS.add("setDouble");
    SET_METHODS.add("setFloat");
    SET_METHODS.add("setTimestamp");
    SET_METHODS.add("setDate");
    SET_METHODS.add("setTime");
    SET_METHODS.add("setArray");
    SET_METHODS.add("setBigDecimal");
    SET_METHODS.add("setAsciiStream");
    SET_METHODS.add("setBinaryStream");
    SET_METHODS.add("setBlob");
    SET_METHODS.add("setBoolean");
    SET_METHODS.add("setBytes");
    SET_METHODS.add("setCharacterStream");
    SET_METHODS.add("setClob");
    SET_METHODS.add("setObject");
    SET_METHODS.add("setNull");

    GET_METHODS.add("getString");
    GET_METHODS.add("getInt");
    GET_METHODS.add("getByte");
    GET_METHODS.add("getShort");
    GET_METHODS.add("getLong");
    GET_METHODS.add("getDouble");
    GET_METHODS.add("getFloat");
    GET_METHODS.add("getTimestamp");
    GET_METHODS.add("getDate");
    GET_METHODS.add("getTime");
    GET_METHODS.add("getArray");
    GET_METHODS.add("getBigDecimal");
    GET_METHODS.add("getAsciiStream");
    GET_METHODS.add("getBinaryStream");
    GET_METHODS.add("getBlob");
    GET_METHODS.add("getBoolean");
    GET_METHODS.add("getBytes");
    GET_METHODS.add("getCharacterStream");
    GET_METHODS.add("getClob");
    GET_METHODS.add("getObject");
    GET_METHODS.add("getNull");

    EXECUTE_METHODS.add("execute");
    EXECUTE_METHODS.add("executeUpdate");
    EXECUTE_METHODS.add("executeQuery");

  }

  /**
   * Sets the column.
   *
   * @param key
   *          the key
   * @param value
   *          the value
   */
  protected void setColumn(Object key, Object value) {
    columnMap.put(key, value);
    columnNames.add(key);
    columnValues.add(value);
  }

  /**
   * Gets the column.
   *
   * @param key
   *          the key
   *
   * @return the column
   */
  protected Object getColumn(Object key) {
    return columnMap.get(key);
  }

  /**
   * Gets the value string.
   *
   * @return the value string
   */
  protected String getValueString() {
    return columnValues.toString();
  }

  /**
   * Gets the type string.
   *
   * @return the type string
   */
  protected String getTypeString() {
    List typeList = new ArrayList<>(columnValues.size());
    for (int i = 0; i < columnValues.size(); i++) {
      Object value = columnValues.get(i);
      if (value == null) {
        typeList.add("null");
      } else {
        typeList.add(value.getClass().getName());
      }
    }
    return typeList.toString();
  }

  /**
   * Gets the column string.
   *
   * @return the column string
   */
  protected String getColumnString() {
    return columnNames.toString();
  }

  /**
   * Clear column info.
   */
  protected void clearColumnInfo() {
    columnMap.clear();
    columnNames.clear();
    columnValues.clear();
  }

  /**
   * Removes the breaking whitespace.
   *
   * @param original
   *          the original
   *
   * @return the string
   */
  protected String removeBreakingWhitespace(String original) {
    return original.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ');
  }

  /**
   * Gets the next id.
   *
   * @return the next id
   */
  protected synchronized static int getNextId() {
    return nextId++;
  }

}
