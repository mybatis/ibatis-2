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
package com.ibatis.sqlmap.engine.mapping.parameter;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.engine.type.JdbcTypeRegistry;
import com.ibatis.sqlmap.engine.type.TypeHandler;

/**
 * The Class ParameterMapping.
 */
public class ParameterMapping {

  /** The Constant MODE_INOUT. */
  private static final String MODE_INOUT = "INOUT";

  /** The Constant MODE_OUT. */
  private static final String MODE_OUT = "OUT";

  /** The Constant MODE_IN. */
  private static final String MODE_IN = "IN";

  /** The property name. */
  private String propertyName;

  /** The type handler. */
  private TypeHandler typeHandler;

  /** The type name. */
  private String typeName; // this is used for REF types or user-defined types

  /** The jdbc type. */
  private int jdbcType;

  /** The jdbc type name. */
  private String jdbcTypeName;

  /** The null value. */
  private String nullValue;

  /** The mode. */
  private String mode;

  /** The input allowed. */
  private boolean inputAllowed;

  /** The output allowed. */
  private boolean outputAllowed;

  /** The java type. */
  private Class javaType;

  /** The result map name. */
  private String resultMapName;

  /** The numeric scale. */
  private Integer numericScale;

  /** The error string. */
  private String errorString;

  /**
   * Instantiates a new parameter mapping.
   */
  public ParameterMapping() {
    mode = "IN";
    inputAllowed = true;
    outputAllowed = false;
    // Default JDBC type if UNKNOWN_TYPE
    jdbcType = JdbcTypeRegistry.UNKNOWN_TYPE;
  }

  /**
   * Gets the null value.
   *
   * @return the null value
   */
  public String getNullValue() {
    return nullValue;
  }

  /**
   * Sets the null value.
   *
   * @param nullValue
   *          the new null value
   */
  public void setNullValue(String nullValue) {
    this.nullValue = nullValue;
  }

  /**
   * Gets the property name.
   *
   * @return the property name
   */
  public String getPropertyName() {
    return propertyName;
  }

  /**
   * Sets the property name.
   *
   * @param propertyName
   *          the new property name
   */
  public void setPropertyName(String propertyName) {
    this.errorString = "Check the parameter mapping for the '" + propertyName + "' property.";
    this.propertyName = propertyName;
  }

  /**
   * Gets the error string.
   *
   * @return the error string
   */
  public String getErrorString() {
    return errorString;
  }

  /**
   * Gets the type handler.
   *
   * @return the type handler
   */
  public TypeHandler getTypeHandler() {
    return typeHandler;
  }

  /**
   * Sets the type handler.
   *
   * @param typeHandler
   *          the new type handler
   */
  public void setTypeHandler(TypeHandler typeHandler) {
    this.typeHandler = typeHandler;
  }

  /**
   * Gets the java type.
   *
   * @return the java type
   */
  public Class getJavaType() {
    return javaType;
  }

  /**
   * Sets the java type.
   *
   * @param javaType
   *          the new java type
   */
  public void setJavaType(Class javaType) {
    this.javaType = javaType;
  }

  /**
   * Gets the java type name.
   *
   * @return the java type name
   */
  public String getJavaTypeName() {
    if (javaType == null) {
      return null;
    }
    return javaType.getName();
  }

  /**
   * Sets the java type name.
   *
   * @param javaTypeName
   *          the new java type name
   */
  public void setJavaTypeName(String javaTypeName) {
    try {
      if (javaTypeName == null) {
        this.javaType = null;
      } else {
        this.javaType = Resources.classForName(javaTypeName);
      }
    } catch (ClassNotFoundException e) {
      throw new SqlMapException("Error setting javaType property of ParameterMap.  Cause: " + e, e);
    }
  }

  /**
   * Gets the jdbc type.
   *
   * @return the jdbc type
   */
  public int getJdbcType() {
    return jdbcType;
  }

  /**
   * Gets the jdbc type name.
   *
   * @return the jdbc type name
   */
  public String getJdbcTypeName() {
    return jdbcTypeName;
  }

  /**
   * Sets the jdbc type name.
   *
   * @param jdbcTypeName
   *          the new jdbc type name
   */
  public void setJdbcTypeName(String jdbcTypeName) {
    this.jdbcTypeName = jdbcTypeName;
    this.jdbcType = JdbcTypeRegistry.getType(jdbcTypeName);
  }

  /**
   * Gets the mode.
   *
   * @return the mode
   */
  public String getMode() {
    return mode;
  }

  /**
   * Sets the mode.
   *
   * @param mode
   *          the new mode
   */
  public void setMode(String mode) {
    this.mode = mode;
    inputAllowed = MODE_IN.equals(mode) || MODE_INOUT.equals(mode);
    outputAllowed = MODE_OUT.equals(mode) || MODE_INOUT.equals(mode);
  }

  /**
   * Checks if is input allowed.
   *
   * @return true, if is input allowed
   */
  public boolean isInputAllowed() {
    return inputAllowed;
  }

  /**
   * Checks if is output allowed.
   *
   * @return true, if is output allowed
   */
  public boolean isOutputAllowed() {
    return outputAllowed;
  }

  /**
   * user-defined or REF types.
   *
   * @return typeName
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * for user-defined or REF types.
   *
   * @param typeName
   *          the new type name
   */
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * Gets the result map name.
   *
   * @return the result map name
   */
  public String getResultMapName() {
    return resultMapName;
  }

  /**
   * Sets the result map name.
   *
   * @param resultMapName
   *          the new result map name
   */
  public void setResultMapName(String resultMapName) {
    this.resultMapName = resultMapName;
  }

  /**
   * Gets the numeric scale.
   *
   * @return the numeric scale
   */
  public Integer getNumericScale() {
    return numericScale;
  }

  /**
   * Sets the numeric scale.
   *
   * @param numericScale
   *          the new numeric scale
   */
  public void setNumericScale(Integer numericScale) {
    if (numericScale != null && numericScale.intValue() < 0) {
      throw new RuntimeException(
          "Error setting numericScale on parameter mapping.  Cause: scale must be greater than or equal to zero");
    }
    this.numericScale = numericScale;
  }

}
