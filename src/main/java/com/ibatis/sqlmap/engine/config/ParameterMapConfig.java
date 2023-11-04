/*
 * Copyright 2004-2023 the original author or authors.
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
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;
import com.ibatis.sqlmap.engine.scope.ErrorContext;
import com.ibatis.sqlmap.engine.type.CustomTypeHandler;
import com.ibatis.sqlmap.engine.type.TypeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ParameterMapConfig.
 */
public class ParameterMapConfig {

  /** The Constant MODE_IN. */
  public static final String MODE_IN = "IN";

  /** The Constant MODE_OUT. */
  public static final String MODE_OUT = "OUT";

  /** The Constant MODE_INOUT. */
  public static final String MODE_INOUT = "INUOT";

  /** The config. */
  private SqlMapConfiguration config;

  /** The error context. */
  private ErrorContext errorContext;

  /** The client. */
  private SqlMapClientImpl client;

  /** The parameter map. */
  private ParameterMap parameterMap;

  /** The parameter mapping list. */
  private List parameterMappingList;

  /**
   * Instantiates a new parameter map config.
   *
   * @param config
   *          the config
   * @param id
   *          the id
   * @param parameterClass
   *          the parameter class
   */
  ParameterMapConfig(SqlMapConfiguration config, String id, Class parameterClass) {
    this.config = config;
    this.errorContext = config.getErrorContext();
    this.client = config.getClient();
    errorContext.setActivity("building a parameter map");
    parameterMap = new ParameterMap(client.getDelegate());
    parameterMap.setId(id);
    parameterMap.setResource(errorContext.getResource());
    errorContext.setObjectId(id + " parameter map");
    parameterMap.setParameterClass(parameterClass);
    errorContext.setMoreInfo("Check the parameter mappings.");
    this.parameterMappingList = new ArrayList();
    client.getDelegate().addParameterMap(parameterMap);
  }

  /**
   * Adds the parameter mapping.
   *
   * @param propertyName
   *          the property name
   * @param javaClass
   *          the java class
   * @param jdbcType
   *          the jdbc type
   * @param nullValue
   *          the null value
   * @param mode
   *          the mode
   * @param outParamType
   *          the out param type
   * @param numericScale
   *          the numeric scale
   * @param typeHandlerImpl
   *          the type handler impl
   * @param resultMap
   *          the result map
   */
  public void addParameterMapping(String propertyName, Class javaClass, String jdbcType, String nullValue, String mode,
      String outParamType, Integer numericScale, Object typeHandlerImpl, String resultMap) {
    errorContext.setObjectId(propertyName + " mapping of the " + parameterMap.getId() + " parameter map");
    TypeHandler handler;
    if (typeHandlerImpl != null) {
      errorContext.setMoreInfo("Check the parameter mapping typeHandler attribute '" + typeHandlerImpl
          + "' (must be a TypeHandler or TypeHandlerCallback implementation).");
      if (typeHandlerImpl instanceof TypeHandlerCallback) {
        handler = new CustomTypeHandler((TypeHandlerCallback) typeHandlerImpl);
      } else if (typeHandlerImpl instanceof TypeHandler) {
        handler = (TypeHandler) typeHandlerImpl;
      } else {
        throw new RuntimeException(
            "The class '" + typeHandlerImpl + "' is not a valid implementation of TypeHandler or TypeHandlerCallback");
      }
    } else {
      errorContext.setMoreInfo("Check the parameter mapping property type or name.");
      handler = config.resolveTypeHandler(client.getDelegate().getTypeHandlerFactory(),
          parameterMap.getParameterClass(), propertyName, javaClass, jdbcType);
    }
    ParameterMapping mapping = new ParameterMapping();
    mapping.setPropertyName(propertyName);
    mapping.setJdbcTypeName(jdbcType);
    mapping.setTypeName(outParamType);
    mapping.setResultMapName(resultMap);
    mapping.setNullValue(nullValue);
    if (mode != null && mode.length() > 0) {
      mapping.setMode(mode);
    }
    mapping.setTypeHandler(handler);
    mapping.setJavaType(javaClass);
    mapping.setNumericScale(numericScale);
    parameterMappingList.add(mapping);
    parameterMap.setParameterMappingList(parameterMappingList);
  }

}
