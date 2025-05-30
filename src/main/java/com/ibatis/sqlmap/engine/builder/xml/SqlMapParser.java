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
package com.ibatis.sqlmap.engine.builder.xml;

import com.ibatis.common.resources.Resources;
import com.ibatis.common.xml.Nodelet;
import com.ibatis.common.xml.NodeletException;
import com.ibatis.common.xml.NodeletParser;
import com.ibatis.common.xml.NodeletUtils;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.engine.cache.CacheController;
import com.ibatis.sqlmap.engine.config.CacheModelConfig;
import com.ibatis.sqlmap.engine.config.ParameterMapConfig;
import com.ibatis.sqlmap.engine.config.ResultMapConfig;
import com.ibatis.sqlmap.engine.mapping.statement.*;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.w3c.dom.Node;

/**
 * The Class SqlMapParser.
 */
public class SqlMapParser {

  /** The parser. */
  private final NodeletParser parser;

  /** The state. */
  private XmlParserState state;

  /** The statement parser. */
  private SqlStatementParser statementParser;

  /**
   * Instantiates a new sql map parser.
   *
   * @param state
   *          the state
   */
  public SqlMapParser(XmlParserState state) {
    this.parser = new NodeletParser();
    this.state = state;

    parser.setValidation(true);
    parser.setEntityResolver(new SqlMapClasspathEntityResolver());

    statementParser = new SqlStatementParser(this.state);

    addSqlMapNodelets();
    addSqlNodelets();
    addTypeAliasNodelets();
    addCacheModelNodelets();
    addParameterMapNodelets();
    addResultMapNodelets();
    addStatementNodelets();

  }

  /**
   * Parses the.
   *
   * @param reader
   *          the reader
   *
   * @throws NodeletException
   *           the nodelet exception
   */
  public void parse(Reader reader) throws NodeletException {
    parser.parse(reader);
  }

  /**
   * Parses the.
   *
   * @param inputStream
   *          the input stream
   *
   * @throws NodeletException
   *           the nodelet exception
   */
  public void parse(InputStream inputStream) throws NodeletException {
    parser.parse(inputStream);
  }

  /**
   * Adds the sql map nodelets.
   */
  private void addSqlMapNodelets() {
    parser.addNodelet("/sqlMap", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        state.setNamespace(attributes.getProperty("namespace"));
      }
    });
  }

  /**
   * Adds the sql nodelets.
   */
  private void addSqlNodelets() {
    parser.addNodelet("/sqlMap/sql", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String id = attributes.getProperty("id");
        if (state.isUseStatementNamespaces()) {
          id = state.applyNamespace(id);
        }
        if (state.getSqlIncludes().containsKey(id)) {
          throw new SqlMapException("Duplicate <sql>-include '" + id + "' found.");
        } else {
          state.getSqlIncludes().put(id, node);
        }
      }
    });
  }

  /**
   * Adds the type alias nodelets.
   */
  private void addTypeAliasNodelets() {
    parser.addNodelet("/sqlMap/typeAlias", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties prop = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String alias = prop.getProperty("alias");
        String type = prop.getProperty("type");
        state.getConfig().getTypeHandlerFactory().putTypeAlias(alias, type);
      }
    });
  }

  /**
   * Adds the cache model nodelets.
   */
  private void addCacheModelNodelets() {
    parser.addNodelet("/sqlMap/cacheModel", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String id = state.applyNamespace(attributes.getProperty("id"));
        String type = attributes.getProperty("type");
        String readOnlyAttr = attributes.getProperty("readOnly");
        Boolean readOnly = readOnlyAttr == null || readOnlyAttr.isEmpty() ? null
            : Boolean.valueOf("true".equals(readOnlyAttr));
        String serializeAttr = attributes.getProperty("serialize");
        Boolean serialize = serializeAttr == null || serializeAttr.isEmpty() ? null
            : Boolean.valueOf("true".equals(serializeAttr));
        type = state.getConfig().getTypeHandlerFactory().resolveAlias(type);
        Class clazz = Resources.classForName(type);
        if (readOnly == null) {
          readOnly = Boolean.TRUE;
        }
        if (serialize == null) {
          serialize = Boolean.FALSE;
        }
        CacheModelConfig cacheConfig = state.getConfig().newCacheModelConfig(id,
            (CacheController) Resources.instantiate(clazz), readOnly.booleanValue(), serialize.booleanValue());
        state.setCacheConfig(cacheConfig);
      }
    });
    parser.addNodelet("/sqlMap/cacheModel/end()", new Nodelet() {
      public void process(Node node) throws Exception {
        state.getCacheConfig().setControllerProperties(state.getCacheProps());
      }
    });
    parser.addNodelet("/sqlMap/cacheModel/property", new Nodelet() {
      public void process(Node node) throws Exception {
        state.getConfig().getErrorContext().setMoreInfo("Check the cache model properties.");
        Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String name = attributes.getProperty("name");
        String value = NodeletUtils.parsePropertyTokens(attributes.getProperty("value"), state.getGlobalProps());
        state.getCacheProps().setProperty(name, value);
      }
    });
    parser.addNodelet("/sqlMap/cacheModel/flushOnExecute", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties childAttributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String statement = childAttributes.getProperty("statement");
        state.getCacheConfig().addFlushTriggerStatement(statement);
      }
    });
    parser.addNodelet("/sqlMap/cacheModel/flushInterval", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties childAttributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        try {
          int milliseconds = childAttributes.getProperty("milliseconds") == null ? 0
              : Integer.parseInt(childAttributes.getProperty("milliseconds"));
          int seconds = childAttributes.getProperty("seconds") == null ? 0
              : Integer.parseInt(childAttributes.getProperty("seconds"));
          int minutes = childAttributes.getProperty("minutes") == null ? 0
              : Integer.parseInt(childAttributes.getProperty("minutes"));
          int hours = childAttributes.getProperty("hours") == null ? 0
              : Integer.parseInt(childAttributes.getProperty("hours"));
          state.getCacheConfig().setFlushInterval(hours, minutes, seconds, milliseconds);
        } catch (NumberFormatException e) {
          throw new RuntimeException("Error building cache in '" + "resourceNAME"
              + "'.  Flush interval milliseconds must be a valid long integer value.  Cause: " + e, e);
        }
      }
    });
  }

  /**
   * Adds the parameter map nodelets.
   */
  private void addParameterMapNodelets() {
    parser.addNodelet("/sqlMap/parameterMap/end()", new Nodelet() {
      public void process(Node node) throws Exception {
        state.getConfig().getErrorContext().setMoreInfo(null);
        state.getConfig().getErrorContext().setObjectId(null);
        state.setParamConfig(null);
      }
    });
    parser.addNodelet("/sqlMap/parameterMap", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String id = state.applyNamespace(attributes.getProperty("id"));
        String parameterClassName = attributes.getProperty("class");
        parameterClassName = state.getConfig().getTypeHandlerFactory().resolveAlias(parameterClassName);
        try {
          state.getConfig().getErrorContext().setMoreInfo("Check the parameter class.");
          ParameterMapConfig paramConf = state.getConfig().newParameterMapConfig(id,
              Resources.classForName(parameterClassName));
          state.setParamConfig(paramConf);
        } catch (Exception e) {
          throw new SqlMapException("Error configuring ParameterMap.  Could not set ParameterClass.  Cause: " + e, e);
        }
      }
    });
    parser.addNodelet("/sqlMap/parameterMap/parameter", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties childAttributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String propertyName = childAttributes.getProperty("property");
        String jdbcType = childAttributes.getProperty("jdbcType");
        String type = childAttributes.getProperty("typeName");
        String javaType = childAttributes.getProperty("javaType");
        String resultMap = state.applyNamespace(childAttributes.getProperty("resultMap"));
        String nullValue = childAttributes.getProperty("nullValue");
        String mode = childAttributes.getProperty("mode");
        String callback = childAttributes.getProperty("typeHandler");
        String numericScaleProp = childAttributes.getProperty("numericScale");

        callback = state.getConfig().getTypeHandlerFactory().resolveAlias(callback);
        Object typeHandlerImpl = null;
        if (callback != null) {
          typeHandlerImpl = Resources.instantiate(callback);
        }

        javaType = state.getConfig().getTypeHandlerFactory().resolveAlias(javaType);
        Class javaClass = null;
        try {
          if (javaType != null && !javaType.isEmpty()) {
            javaClass = Resources.classForName(javaType);
          }
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("Error setting javaType on parameter mapping.  Cause: " + e);
        }

        Integer numericScale = null;
        if (numericScaleProp != null) {
          numericScale = Integer.valueOf(numericScaleProp);
        }

        state.getParamConfig().addParameterMapping(propertyName, javaClass, jdbcType, nullValue, mode, type,
            numericScale, typeHandlerImpl, resultMap);
      }
    });
  }

  /**
   * Adds the result map nodelets.
   */
  private void addResultMapNodelets() {
    parser.addNodelet("/sqlMap/resultMap/end()", new Nodelet() {
      public void process(Node node) throws Exception {
        state.getConfig().getErrorContext().setMoreInfo(null);
        state.getConfig().getErrorContext().setObjectId(null);
      }
    });
    parser.addNodelet("/sqlMap/resultMap", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String id = state.applyNamespace(attributes.getProperty("id"));
        String resultClassName = attributes.getProperty("class");
        String extended = state.applyNamespace(attributes.getProperty("extends"));
        String xmlName = attributes.getProperty("xmlName");
        String groupBy = attributes.getProperty("groupBy");

        resultClassName = state.getConfig().getTypeHandlerFactory().resolveAlias(resultClassName);
        Class resultClass;
        try {
          state.getConfig().getErrorContext().setMoreInfo("Check the result class.");
          resultClass = Resources.classForName(resultClassName);
        } catch (Exception e) {
          throw new RuntimeException("Error configuring Result.  Could not set ResultClass.  Cause: " + e, e);
        }
        ResultMapConfig resultConf = state.getConfig().newResultMapConfig(id, resultClass, groupBy, extended, xmlName);
        state.setResultConfig(resultConf);
      }
    });
    parser.addNodelet("/sqlMap/resultMap/result", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties childAttributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String propertyName = childAttributes.getProperty("property");
        String nullValue = childAttributes.getProperty("nullValue");
        String jdbcType = childAttributes.getProperty("jdbcType");
        String javaType = childAttributes.getProperty("javaType");
        String columnName = childAttributes.getProperty("column");
        String columnIndexProp = childAttributes.getProperty("columnIndex");
        String statementName = childAttributes.getProperty("select");
        String resultMapName = childAttributes.getProperty("resultMap");
        String callback = childAttributes.getProperty("typeHandler");
        String notNullColumn = childAttributes.getProperty("notNullColumn");

        state.getConfig().getErrorContext().setMoreInfo("Check the result mapping property type or name.");
        Class javaClass = null;
        try {
          javaType = state.getConfig().getTypeHandlerFactory().resolveAlias(javaType);
          if (javaType != null && !javaType.isEmpty()) {
            javaClass = Resources.classForName(javaType);
          }
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("Error setting java type on result discriminator mapping.  Cause: " + e);
        }

        state.getConfig().getErrorContext().setMoreInfo("Check the result mapping typeHandler attribute '" + callback
            + "' (must be a TypeHandler or TypeHandlerCallback implementation).");
        Object typeHandlerImpl = null;
        try {
          if (callback != null && !callback.isEmpty()) {
            callback = state.getConfig().getTypeHandlerFactory().resolveAlias(callback);
            typeHandlerImpl = Resources.instantiate(callback);
          }
        } catch (Exception e) {
          throw new RuntimeException("Error occurred during custom type handler configuration.  Cause: " + e, e);
        }

        Integer columnIndex = null;
        if (columnIndexProp != null) {
          try {
            columnIndex = Integer.valueOf(columnIndexProp);
          } catch (Exception e) {
            throw new RuntimeException("Error parsing column index.  Cause: " + e, e);
          }
        }

        state.getResultConfig().addResultMapping(propertyName, columnName, columnIndex, javaClass, jdbcType, nullValue,
            notNullColumn, statementName, resultMapName, typeHandlerImpl);
      }
    });

    parser.addNodelet("/sqlMap/resultMap/discriminator/subMap", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties childAttributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String value = childAttributes.getProperty("value");
        String resultMap = childAttributes.getProperty("resultMap");
        resultMap = state.applyNamespace(resultMap);
        state.getResultConfig().addDiscriminatorSubMap(value, resultMap);
      }
    });

    parser.addNodelet("/sqlMap/resultMap/discriminator", new Nodelet() {
      public void process(Node node) throws Exception {
        Properties childAttributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
        String nullValue = childAttributes.getProperty("nullValue");
        String jdbcType = childAttributes.getProperty("jdbcType");
        String javaType = childAttributes.getProperty("javaType");
        String columnName = childAttributes.getProperty("column");
        String columnIndexProp = childAttributes.getProperty("columnIndex");
        String callback = childAttributes.getProperty("typeHandler");

        state.getConfig().getErrorContext().setMoreInfo("Check the disriminator type or name.");
        Class javaClass = null;
        try {
          javaType = state.getConfig().getTypeHandlerFactory().resolveAlias(javaType);
          if (javaType != null && !javaType.isEmpty()) {
            javaClass = Resources.classForName(javaType);
          }
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("Error setting java type on result discriminator mapping.  Cause: " + e);
        }

        state.getConfig().getErrorContext().setMoreInfo("Check the result mapping discriminator typeHandler attribute '"
            + callback + "' (must be a TypeHandlerCallback implementation).");
        Object typeHandlerImpl = null;
        try {
          if (callback != null && !callback.isEmpty()) {
            callback = state.getConfig().getTypeHandlerFactory().resolveAlias(callback);
            typeHandlerImpl = Resources.instantiate(callback);
          }
        } catch (Exception e) {
          throw new RuntimeException("Error occurred during custom type handler configuration.  Cause: " + e, e);
        }

        Integer columnIndex = null;
        if (columnIndexProp != null) {
          try {
            columnIndex = Integer.valueOf(columnIndexProp);
          } catch (Exception e) {
            throw new RuntimeException("Error parsing column index.  Cause: " + e, e);
          }
        }

        state.getResultConfig().setDiscriminator(columnName, columnIndex, javaClass, jdbcType, nullValue,
            typeHandlerImpl);
      }
    });
  }

  /**
   * Adds the statement nodelets.
   */
  protected void addStatementNodelets() {
    parser.addNodelet("/sqlMap/statement", new Nodelet() {
      public void process(Node node) throws Exception {
        statementParser.parseGeneralStatement(node, new MappedStatement());
      }
    });
    parser.addNodelet("/sqlMap/insert", new Nodelet() {
      public void process(Node node) throws Exception {
        statementParser.parseGeneralStatement(node, new InsertStatement());
      }
    });
    parser.addNodelet("/sqlMap/update", new Nodelet() {
      public void process(Node node) throws Exception {
        statementParser.parseGeneralStatement(node, new UpdateStatement());
      }
    });
    parser.addNodelet("/sqlMap/delete", new Nodelet() {
      public void process(Node node) throws Exception {
        statementParser.parseGeneralStatement(node, new DeleteStatement());
      }
    });
    parser.addNodelet("/sqlMap/select", new Nodelet() {
      public void process(Node node) throws Exception {
        statementParser.parseGeneralStatement(node, new SelectStatement());
      }
    });
    parser.addNodelet("/sqlMap/procedure", new Nodelet() {
      public void process(Node node) throws Exception {
        statementParser.parseGeneralStatement(node, new ProcedureStatement());
      }
    });
  }

}
