/**
 * Copyright 2004-2020 the original author or authors.
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
package com.ibatis.sqlmap.engine.mapping.parameter;

import com.ibatis.common.logging.Log;
import com.ibatis.common.logging.LogFactory;
import com.ibatis.sqlmap.engine.cache.CacheKey;
import com.ibatis.sqlmap.engine.exchange.DataExchange;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.scope.ErrorContext;
import com.ibatis.sqlmap.engine.scope.StatementScope;
import com.ibatis.sqlmap.engine.type.CustomTypeHandler;
import com.ibatis.sqlmap.engine.type.JdbcTypeRegistry;
import com.ibatis.sqlmap.engine.type.TypeHandler;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class ParameterMap.
 */
public class ParameterMap {

  /** The Constant log. */
  private static final Log log = LogFactory.getLog(ParameterMap.class);

  /** The id. */
  private String id;

  /** The parameter class. */
  private Class parameterClass;

  /** The parameter mappings. */
  private ParameterMapping[] parameterMappings;

  /** The use set object for null value. */
  private Boolean useSetObjectForNullValue;

  /** The sql type to use for null value. */
  private int sqlTypeToUseForNullValue;

  /** The data exchange. */
  private DataExchange dataExchange;

  /** The resource. */
  private String resource;

  /** The parameter mapping index. */
  private Map parameterMappingIndex = new HashMap();

  /** The delegate. */
  private SqlMapExecutorDelegate delegate;

  /**
   * Instantiates a new parameter map.
   *
   * @param delegate
   *          the delegate
   */
  public ParameterMap(SqlMapExecutorDelegate delegate) {
    this.delegate = delegate;
  }

  /**
   * Gets the delegate.
   *
   * @return the delegate
   */
  public SqlMapExecutorDelegate getDelegate() {
    return delegate;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id
   *          the new id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the parameter class.
   *
   * @return the parameter class
   */
  public Class getParameterClass() {
    return parameterClass;
  }

  /**
   * Sets the parameter class.
   *
   * @param parameterClass
   *          the new parameter class
   */
  public void setParameterClass(Class parameterClass) {
    this.parameterClass = parameterClass;
  }

  /**
   * Gets the data exchange.
   *
   * @return the data exchange
   */
  public DataExchange getDataExchange() {
    return dataExchange;
  }

  /**
   * Sets the data exchange.
   *
   * @param dataExchange
   *          the new data exchange
   */
  public void setDataExchange(DataExchange dataExchange) {
    this.dataExchange = dataExchange;
  }

  /**
   * Gets the parameter mappings.
   *
   * @return the parameter mappings
   */
  public ParameterMapping[] getParameterMappings() {
    return parameterMappings;
  }

  /**
   * Sets the parameter mapping list.
   *
   * @param parameterMappingList
   *          the new parameter mapping list
   */
  public void setParameterMappingList(List parameterMappingList) {
    this.parameterMappings = (ParameterMapping[]) parameterMappingList
        .toArray(new ParameterMapping[parameterMappingList.size()]);
    parameterMappingIndex.clear();
    for (int i = 0; i < parameterMappings.length; i++) {
      parameterMappingIndex.put(parameterMappings[i].getPropertyName(), Integer.valueOf(i));
    }
    Map props = new HashMap();
    props.put("map", this);

    dataExchange = delegate.getDataExchangeFactory().getDataExchangeForClass(parameterClass);
    dataExchange.initialize(props);
  }

  /**
   * Gets the parameter index.
   *
   * @param propertyName
   *          the property name
   * @return the parameter index
   */
  public int getParameterIndex(String propertyName) {
    Integer idx = null;
    idx = (Integer) parameterMappingIndex.get(propertyName);
    return idx == null ? -1 : idx.intValue();
  }

  /**
   * Gets the parameter count.
   *
   * @return the parameter count
   */
  public int getParameterCount() {
    return this.parameterMappings.length;
  }

  /**
   * Sets the parameters.
   *
   * @param statementScope
   *          the statement scope
   * @param ps
   *          the ps
   * @param parameters
   *          the parameters
   * @throws SQLException
   *           the SQL exception
   */
  public void setParameters(StatementScope statementScope, PreparedStatement ps, Object[] parameters)
      throws SQLException {

    ErrorContext errorContext = statementScope.getErrorContext();
    errorContext.setActivity("applying a parameter map");
    errorContext.setObjectId(this.getId());
    errorContext.setResource(this.getResource());
    errorContext.setMoreInfo("Check the parameter map.");

    if (parameterMappings != null) {
      for (int i = 0; i < parameterMappings.length; i++) {
        ParameterMapping mapping = parameterMappings[i];
        errorContext.setMoreInfo(mapping.getErrorString());
        if (mapping.isInputAllowed()) {
          setParameter(ps, mapping, parameters, i);
        }
      }
    }
  }

  /**
   * Gets the parameter object values.
   *
   * @param statementScope
   *          the statement scope
   * @param parameterObject
   *          the parameter object
   * @return the parameter object values
   */
  public Object[] getParameterObjectValues(StatementScope statementScope, Object parameterObject) {
    return dataExchange.getData(statementScope, this, parameterObject);
  }

  /**
   * Gets the cache key.
   *
   * @param statementScope
   *          the statement scope
   * @param parameterObject
   *          the parameter object
   * @return the cache key
   */
  public CacheKey getCacheKey(StatementScope statementScope, Object parameterObject) {
    return dataExchange.getCacheKey(statementScope, this, parameterObject);
  }

  /**
   * Refresh parameter object values.
   *
   * @param statementScope
   *          the statement scope
   * @param parameterObject
   *          the parameter object
   * @param values
   *          the values
   */
  public void refreshParameterObjectValues(StatementScope statementScope, Object parameterObject, Object[] values) {
    dataExchange.setData(statementScope, this, parameterObject, values);
  }

  /**
   * Gets the resource.
   *
   * @return the resource
   */
  public String getResource() {
    return resource;
  }

  /**
   * Sets the resource.
   *
   * @param resource
   *          the new resource
   */
  public void setResource(String resource) {
    this.resource = resource;
  }

  /**
   * Sets the parameter.
   *
   * @param ps
   *          the ps
   * @param mapping
   *          the mapping
   * @param parameters
   *          the parameters
   * @param i
   *          the i
   * @throws SQLException
   *           the SQL exception
   */
  protected void setParameter(PreparedStatement ps, ParameterMapping mapping, Object[] parameters, int i)
      throws SQLException {
    Object value = parameters[i];
    // Apply Null Value
    String nullValueString = mapping.getNullValue();
    if (nullValueString != null) {
      TypeHandler handler = mapping.getTypeHandler();
      if (handler.equals(value, nullValueString)) {
        value = null;
      }
    }

    // Set Parameter
    TypeHandler typeHandler = mapping.getTypeHandler();
    if (value != null) {
      typeHandler.setParameter(ps, i + 1, value, mapping.getJdbcTypeName());
    } else if (typeHandler instanceof CustomTypeHandler) {
      typeHandler.setParameter(ps, i + 1, value, mapping.getJdbcTypeName());
    } else {
      int jdbcType = mapping.getJdbcType();
      if (jdbcType != JdbcTypeRegistry.UNKNOWN_TYPE) {
        ps.setNull(i + 1, jdbcType);
      } else {
        // Cloned from Spring StatementCreatorUtils.java (IBATIS-536)
        if (useSetObjectForNullValue == null) {
          // Keep current JDBC connection preferences for limiting introspections
          useSetObjectForNullValue = Boolean.FALSE;
          sqlTypeToUseForNullValue = Types.NULL;
          try {
            DatabaseMetaData dbmd = ps.getConnection().getMetaData();
            String databaseProductName = dbmd.getDatabaseProductName();
            String jdbcDriverName = dbmd.getDriverName();
            if (databaseProductName.startsWith("Informix") || databaseProductName.startsWith("Microsoft SQL Server")) {
              useSetObjectForNullValue = Boolean.TRUE;
            } else if (databaseProductName.startsWith("DB2") || jdbcDriverName.startsWith("jConnect")
                || jdbcDriverName.startsWith("SQLServer") || jdbcDriverName.startsWith("Apache Derby Embedded")) {
              sqlTypeToUseForNullValue = Types.VARCHAR;
            }
          } catch (Throwable ex) {
            log.debug("Could not check database or driver name: " + ex.getMessage());
          }
        }
        if (useSetObjectForNullValue.booleanValue()) {
          ps.setObject(i + 1, null);
        } else {
          ps.setNull(i + 1, sqlTypeToUseForNullValue);
        }
      }
    }
  }

}
