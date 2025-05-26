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
package com.ibatis.sqlmap.engine.config;

import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.result.Discriminator;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMapping;
import com.ibatis.sqlmap.engine.scope.ErrorContext;
import com.ibatis.sqlmap.engine.type.CustomTypeHandler;
import com.ibatis.sqlmap.engine.type.TypeHandler;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The Class ResultMapConfig.
 */
public class ResultMapConfig {

  /** The config. */
  private SqlMapConfiguration config;

  /** The error context. */
  private ErrorContext errorContext;

  /** The client. */
  private SqlMapClientImpl client;

  /** The delegate. */
  private SqlMapExecutorDelegate delegate;

  /** The type handler factory. */
  private TypeHandlerFactory typeHandlerFactory;

  /** The result map. */
  private ResultMap resultMap;

  /** The result mapping list. */
  private List resultMappingList;

  /** The result mapping index. */
  private int resultMappingIndex;

  /** The discriminator. */
  private Discriminator discriminator;

  /**
   * Instantiates a new result map config.
   *
   * @param config
   *          the config
   * @param id
   *          the id
   * @param resultClass
   *          the result class
   * @param groupBy
   *          the group by
   * @param extendsResultMap
   *          the extends result map
   * @param xmlName
   *          the xml name
   */
  ResultMapConfig(SqlMapConfiguration config, String id, Class resultClass, String groupBy, String extendsResultMap,
      String xmlName) {
    this.config = config;
    this.errorContext = config.getErrorContext();
    this.client = config.getClient();
    this.delegate = config.getDelegate();
    this.typeHandlerFactory = config.getTypeHandlerFactory();
    this.resultMap = new ResultMap(client.getDelegate());
    this.resultMappingList = new ArrayList<>();
    errorContext.setActivity("building a result map");
    errorContext.setObjectId(id + " result map");
    resultMap.setId(id);
    resultMap.setXmlName(xmlName);
    resultMap.setResource(errorContext.getResource());
    if (groupBy != null && !groupBy.isEmpty()) {
      StringTokenizer parser = new StringTokenizer(groupBy, ", ", false);
      while (parser.hasMoreTokens()) {
        resultMap.addGroupByProperty(parser.nextToken());
      }
    }
    resultMap.setResultClass(resultClass);
    errorContext.setMoreInfo("Check the extended result map.");
    if (extendsResultMap != null) {
      ResultMap extendedResultMap = (ResultMap) client.getDelegate().getResultMap(extendsResultMap);
      ResultMapping[] resultMappings = extendedResultMap.getResultMappings();
      for (int i = 0; i < resultMappings.length; i++) {
        resultMappingList.add(resultMappings[i]);
      }
      List nestedResultMappings = extendedResultMap.getNestedResultMappings();
      if (nestedResultMappings != null) {
        Iterator iter = nestedResultMappings.iterator();
        while (iter.hasNext()) {
          resultMap.addNestedResultMappings((ResultMapping) iter.next());
        }
      }
      if (groupBy == null || groupBy.isEmpty()) {
        if (extendedResultMap.hasGroupBy()) {
          Iterator i = extendedResultMap.groupByProps();
          while (i.hasNext()) {
            resultMap.addGroupByProperty((String) i.next());
          }
        }
      }
    }
    errorContext.setMoreInfo("Check the result mappings.");
    resultMappingIndex = resultMappingList.size();
    resultMap.setResultMappingList(resultMappingList);
    client.getDelegate().addResultMap(resultMap);
  }

  /**
   * Sets the discriminator.
   *
   * @param columnName
   *          the column name
   * @param columnIndex
   *          the column index
   * @param javaClass
   *          the java class
   * @param jdbcType
   *          the jdbc type
   * @param nullValue
   *          the null value
   * @param typeHandlerImpl
   *          the type handler impl
   */
  public void setDiscriminator(String columnName, Integer columnIndex, Class javaClass, String jdbcType,
      String nullValue, Object typeHandlerImpl) {
    TypeHandler handler;
    if (typeHandlerImpl != null) {
      if (typeHandlerImpl instanceof TypeHandlerCallback) {
        handler = new CustomTypeHandler((TypeHandlerCallback) typeHandlerImpl);
      } else if (typeHandlerImpl instanceof TypeHandler) {
        handler = (TypeHandler) typeHandlerImpl;
      } else {
        throw new RuntimeException("The class '' is not a valid implementation of TypeHandler or TypeHandlerCallback");
      }
    } else {
      handler = config.resolveTypeHandler(client.getDelegate().getTypeHandlerFactory(), resultMap.getResultClass(), "",
          javaClass, jdbcType, true);
    }
    ResultMapping mapping = new ResultMapping();
    mapping.setColumnName(columnName);
    mapping.setJdbcTypeName(jdbcType);
    mapping.setTypeHandler(handler);
    mapping.setNullValue(nullValue);
    mapping.setJavaType(javaClass);
    if (columnIndex != null) {
      mapping.setColumnIndex(columnIndex.intValue());
    }
    discriminator = new Discriminator(delegate, mapping);
    resultMap.setDiscriminator(discriminator);
  }

  /**
   * Adds the discriminator sub map.
   *
   * @param value
   *          the value
   * @param resultMap
   *          the result map
   */
  public void addDiscriminatorSubMap(Object value, String resultMap) {
    if (discriminator == null) {
      throw new RuntimeException("The discriminator is null, but somehow a subMap was reached.  This is a bug.");
    }
    discriminator.addSubMap(value.toString(), resultMap);
  }

  /**
   * Adds the result mapping.
   *
   * @param propertyName
   *          the property name
   * @param columnName
   *          the column name
   * @param columnIndex
   *          the column index
   * @param javaClass
   *          the java class
   * @param jdbcType
   *          the jdbc type
   * @param nullValue
   *          the null value
   * @param notNullColumn
   *          the not null column
   * @param statementName
   *          the statement name
   * @param resultMapName
   *          the result map name
   * @param impl
   *          the impl
   */
  public void addResultMapping(String propertyName, String columnName, Integer columnIndex, Class javaClass,
      String jdbcType, String nullValue, String notNullColumn, String statementName, String resultMapName,
      Object impl) {
    errorContext.setObjectId(propertyName + " mapping of the " + resultMap.getId() + " result map");
    TypeHandler handler;
    if (impl != null) {
      if (impl instanceof TypeHandlerCallback) {
        handler = new CustomTypeHandler((TypeHandlerCallback) impl);
      } else if (impl instanceof TypeHandler) {
        handler = (TypeHandler) impl;
      } else {
        throw new RuntimeException(
            "The class '" + impl + "' is not a valid implementation of TypeHandler or TypeHandlerCallback");
      }
    } else {
      handler = config.resolveTypeHandler(client.getDelegate().getTypeHandlerFactory(), resultMap.getResultClass(),
          propertyName, javaClass, jdbcType, true);
    }
    ResultMapping mapping = new ResultMapping();
    mapping.setPropertyName(propertyName);
    mapping.setColumnName(columnName);
    mapping.setJdbcTypeName(jdbcType);
    mapping.setTypeHandler(handler);
    mapping.setNullValue(nullValue);
    mapping.setNotNullColumn(notNullColumn);
    mapping.setStatementName(statementName);
    mapping.setNestedResultMapName(resultMapName);
    if (resultMapName != null && resultMapName.length() > 0) {
      resultMap.addNestedResultMappings(mapping);
    }
    mapping.setJavaType(javaClass);
    if (columnIndex != null) {
      mapping.setColumnIndex(columnIndex.intValue());
    } else {
      resultMappingIndex++;
      mapping.setColumnIndex(resultMappingIndex);
    }
    resultMappingList.add(mapping);
    resultMap.setResultMappingList(resultMappingList);
  }

}
