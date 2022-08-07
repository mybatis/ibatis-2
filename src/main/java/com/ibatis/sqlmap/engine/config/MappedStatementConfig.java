/*
 * Copyright 2004-2022 the original author or authors.
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

import com.ibatis.common.beans.*;
import com.ibatis.common.resources.*;
import com.ibatis.sqlmap.client.*;
import com.ibatis.sqlmap.engine.cache.*;
import com.ibatis.sqlmap.engine.impl.*;
import com.ibatis.sqlmap.engine.mapping.parameter.*;
import com.ibatis.sqlmap.engine.mapping.result.*;
import com.ibatis.sqlmap.engine.mapping.sql.*;
import com.ibatis.sqlmap.engine.mapping.sql.dynamic.*;
import com.ibatis.sqlmap.engine.mapping.sql.simple.*;
import com.ibatis.sqlmap.engine.mapping.sql.stat.*;
import com.ibatis.sqlmap.engine.mapping.statement.*;
import com.ibatis.sqlmap.engine.scope.*;
import com.ibatis.sqlmap.engine.type.*;

import java.sql.ResultSet;
import java.util.*;

/**
 * The Class MappedStatementConfig.
 */
public class MappedStatementConfig {

  /** The Constant PROBE. */
  private static final Probe PROBE = ProbeFactory.getProbe();

  /** The Constant PARAM_PARSER. */
  private static final InlineParameterMapParser PARAM_PARSER = new InlineParameterMapParser();

  /** The error context. */
  private ErrorContext errorContext;

  /** The client. */
  private SqlMapClientImpl client;

  /** The type handler factory. */
  private TypeHandlerFactory typeHandlerFactory;

  /** The mapped statement. */
  private MappedStatement mappedStatement;

  /** The root statement. */
  private MappedStatement rootStatement;

  /**
   * Instantiates a new mapped statement config.
   *
   * @param config
   *          the config
   * @param id
   *          the id
   * @param statement
   *          the statement
   * @param processor
   *          the processor
   * @param parameterMapName
   *          the parameter map name
   * @param parameterClass
   *          the parameter class
   * @param resultMapName
   *          the result map name
   * @param additionalResultMapNames
   *          the additional result map names
   * @param resultClass
   *          the result class
   * @param additionalResultClasses
   *          the additional result classes
   * @param cacheModelName
   *          the cache model name
   * @param resultSetType
   *          the result set type
   * @param fetchSize
   *          the fetch size
   * @param allowRemapping
   *          the allow remapping
   * @param timeout
   *          the timeout
   * @param defaultStatementTimeout
   *          the default statement timeout
   * @param xmlResultName
   *          the xml result name
   */
  MappedStatementConfig(SqlMapConfiguration config, String id, MappedStatement statement, SqlSource processor,
      String parameterMapName, Class parameterClass, String resultMapName, String[] additionalResultMapNames,
      Class resultClass, Class[] additionalResultClasses, String cacheModelName, String resultSetType,
      Integer fetchSize, boolean allowRemapping, Integer timeout, Integer defaultStatementTimeout,
      String xmlResultName) {
    this.errorContext = config.getErrorContext();
    this.client = config.getClient();
    SqlMapExecutorDelegate delegate = client.getDelegate();
    this.typeHandlerFactory = config.getTypeHandlerFactory();
    errorContext.setActivity("parsing a mapped statement");
    errorContext.setObjectId(id + " statement");
    errorContext.setMoreInfo("Check the result map name.");
    if (resultMapName != null) {
      statement.setResultMap(client.getDelegate().getResultMap(resultMapName));
      if (additionalResultMapNames != null) {
        for (int i = 0; i < additionalResultMapNames.length; i++) {
          statement.addResultMap(client.getDelegate().getResultMap(additionalResultMapNames[i]));
        }
      }
    }
    errorContext.setMoreInfo("Check the parameter map name.");
    if (parameterMapName != null) {
      statement.setParameterMap(client.getDelegate().getParameterMap(parameterMapName));
    }
    statement.setId(id);
    statement.setResource(errorContext.getResource());
    if (resultSetType != null) {
      if ("FORWARD_ONLY".equals(resultSetType)) {
        statement.setResultSetType(Integer.valueOf(ResultSet.TYPE_FORWARD_ONLY));
      } else if ("SCROLL_INSENSITIVE".equals(resultSetType)) {
        statement.setResultSetType(Integer.valueOf(ResultSet.TYPE_SCROLL_INSENSITIVE));
      } else if ("SCROLL_SENSITIVE".equals(resultSetType)) {
        statement.setResultSetType(Integer.valueOf(ResultSet.TYPE_SCROLL_SENSITIVE));
      }
    }
    if (fetchSize != null) {
      statement.setFetchSize(fetchSize);
    }

    // set parameter class either from attribute or from map (make sure to match)
    ParameterMap parameterMap = statement.getParameterMap();
    if (parameterMap == null) {
      statement.setParameterClass(parameterClass);
    } else {
      statement.setParameterClass(parameterMap.getParameterClass());
    }

    // process SQL statement, including inline parameter maps
    errorContext.setMoreInfo("Check the SQL statement.");
    Sql sql = processor.getSql();
    setSqlForStatement(statement, sql);

    // set up either null result map or automatic result mapping
    ResultMap resultMap = (ResultMap) statement.getResultMap();
    if (resultMap == null && resultClass == null) {
      statement.setResultMap(null);
    } else if (resultMap == null) {
      resultMap = buildAutoResultMap(allowRemapping, statement, resultClass, xmlResultName);
      statement.setResultMap(resultMap);
      if (additionalResultClasses != null) {
        for (int i = 0; i < additionalResultClasses.length; i++) {
          statement
              .addResultMap(buildAutoResultMap(allowRemapping, statement, additionalResultClasses[i], xmlResultName));
        }
      }

    }
    statement.setTimeout(defaultStatementTimeout);
    if (timeout != null) {
      try {
        statement.setTimeout(timeout);
      } catch (NumberFormatException e) {
        throw new SqlMapException(
            "Specified timeout value for statement " + statement.getId() + " is not a valid integer");
      }
    }
    errorContext.setMoreInfo(null);
    errorContext.setObjectId(null);
    statement.setSqlMapClient(client);
    if (cacheModelName != null && cacheModelName.length() > 0 && client.getDelegate().isCacheModelsEnabled()) {
      CacheModel cacheModel = client.getDelegate().getCacheModel(cacheModelName);
      mappedStatement = new CachingStatement(statement, cacheModel);
    } else {
      mappedStatement = statement;
    }
    rootStatement = statement;
    delegate.addMappedStatement(mappedStatement);
  }

  /**
   * Sets the select key statement.
   *
   * @param processor
   *          the processor
   * @param resultClassName
   *          the result class name
   * @param keyPropName
   *          the key prop name
   * @param runAfterSQL
   *          the run after SQL
   * @param type
   *          the type
   */
  public void setSelectKeyStatement(SqlSource processor, String resultClassName, String keyPropName,
      boolean runAfterSQL, String type) {
    if (rootStatement instanceof InsertStatement) {
      InsertStatement insertStatement = ((InsertStatement) rootStatement);
      Class parameterClass = insertStatement.getParameterClass();
      errorContext.setActivity("parsing a select key");
      SelectKeyStatement selectKeyStatement = new SelectKeyStatement();
      resultClassName = typeHandlerFactory.resolveAlias(resultClassName);
      Class resultClass = null;

      // get parameter and result maps
      selectKeyStatement.setSqlMapClient(client);
      selectKeyStatement.setId(insertStatement.getId() + "-SelectKey");
      selectKeyStatement.setResource(errorContext.getResource());
      selectKeyStatement.setKeyProperty(keyPropName);
      selectKeyStatement.setRunAfterSQL(runAfterSQL);
      // process the type (pre or post) attribute
      if (type != null) {
        selectKeyStatement.setRunAfterSQL("post".equals(type));
      }
      try {
        if (resultClassName != null) {
          errorContext.setMoreInfo("Check the select key result class.");
          resultClass = Resources.classForName(resultClassName);
        } else {
          if (keyPropName != null && parameterClass != null) {
            resultClass = PROBE.getPropertyTypeForSetter(parameterClass, selectKeyStatement.getKeyProperty());
          }
        }
      } catch (ClassNotFoundException e) {
        throw new SqlMapException("Error.  Could not set result class.  Cause: " + e, e);
      }
      if (resultClass == null) {
        resultClass = Object.class;
      }

      // process SQL statement, including inline parameter maps
      errorContext.setMoreInfo("Check the select key SQL statement.");
      Sql sql = processor.getSql();
      setSqlForStatement(selectKeyStatement, sql);
      ResultMap resultMap;
      resultMap = new AutoResultMap(client.getDelegate(), false);
      resultMap.setId(selectKeyStatement.getId() + "-AutoResultMap");
      resultMap.setResultClass(resultClass);
      resultMap.setResource(selectKeyStatement.getResource());
      selectKeyStatement.setResultMap(resultMap);
      errorContext.setMoreInfo(null);
      insertStatement.setSelectKeyStatement(selectKeyStatement);
    } else {
      throw new SqlMapException("You cant set a select key statement on statement named " + rootStatement.getId()
          + " because it is not an InsertStatement.");
    }
  }

  /**
   * Sets the sql for statement.
   *
   * @param statement
   *          the statement
   * @param sql
   *          the sql
   */
  private void setSqlForStatement(MappedStatement statement, Sql sql) {
    if (sql instanceof DynamicSql) {
      statement.setSql(sql);
    } else {
      applyInlineParameterMap(statement, sql.getSql(null, null));
    }
  }

  /**
   * Apply inline parameter map.
   *
   * @param statement
   *          the statement
   * @param sqlStatement
   *          the sql statement
   */
  private void applyInlineParameterMap(MappedStatement statement, String sqlStatement) {
    String newSql = sqlStatement;
    errorContext.setActivity("building an inline parameter map");
    ParameterMap parameterMap = statement.getParameterMap();
    errorContext.setMoreInfo("Check the inline parameters.");
    if (parameterMap == null) {
      ParameterMap map;
      map = new ParameterMap(client.getDelegate());
      map.setId(statement.getId() + "-InlineParameterMap");
      map.setParameterClass(statement.getParameterClass());
      map.setResource(statement.getResource());
      statement.setParameterMap(map);
      SqlText sqlText = PARAM_PARSER.parseInlineParameterMap(client.getDelegate().getTypeHandlerFactory(), newSql,
          statement.getParameterClass());
      newSql = sqlText.getText();
      List mappingList = Arrays.asList(sqlText.getParameterMappings());
      map.setParameterMappingList(mappingList);
    }
    Sql sql;
    if (SimpleDynamicSql.isSimpleDynamicSql(newSql)) {
      sql = new SimpleDynamicSql(client.getDelegate(), newSql);
    } else {
      sql = new StaticSql(newSql);
    }
    statement.setSql(sql);

  }

  /**
   * Builds the auto result map.
   *
   * @param allowRemapping
   *          the allow remapping
   * @param statement
   *          the statement
   * @param firstResultClass
   *          the first result class
   * @param xmlResultName
   *          the xml result name
   *
   * @return the result map
   */
  private ResultMap buildAutoResultMap(boolean allowRemapping, MappedStatement statement, Class firstResultClass,
      String xmlResultName) {
    ResultMap resultMap;
    resultMap = new AutoResultMap(client.getDelegate(), allowRemapping);
    resultMap.setId(statement.getId() + "-AutoResultMap");
    resultMap.setResultClass(firstResultClass);
    resultMap.setXmlName(xmlResultName);
    resultMap.setResource(statement.getResource());
    return resultMap;
  }

  /**
   * Gets the mapped statement.
   *
   * @return the mapped statement
   */
  public MappedStatement getMappedStatement() {
    return mappedStatement;
  }
}
