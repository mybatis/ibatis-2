/*
 * Copyright 2004-2021 the original author or authors.
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
package com.ibatis.sqlmap.engine.builder.xml;

import com.ibatis.common.resources.*;
import com.ibatis.sqlmap.engine.config.*;

import java.util.*;

import javax.sql.DataSource;

/**
 * The Class XmlParserState.
 */
public class XmlParserState {

  /** The config. */
  private SqlMapConfiguration config = new SqlMapConfiguration();

  /** The global props. */
  private Properties globalProps = new Properties();

  /** The tx props. */
  private Properties txProps = new Properties();

  /** The ds props. */
  private Properties dsProps = new Properties();

  /** The cache props. */
  private Properties cacheProps = new Properties();

  /** The use statement namespaces. */
  private boolean useStatementNamespaces = false;

  /** The sql includes. */
  private Map sqlIncludes = new HashMap();

  /** The param config. */
  private ParameterMapConfig paramConfig;

  /** The result config. */
  private ResultMapConfig resultConfig;

  /** The cache config. */
  private CacheModelConfig cacheConfig;

  /** The namespace. */
  private String namespace;

  /** The data source. */
  private DataSource dataSource;

  /**
   * Gets the config.
   *
   * @return the config
   */
  public SqlMapConfiguration getConfig() {
    return config;
  }

  /**
   * Sets the global props.
   *
   * @param props
   *          the new global props
   */
  public void setGlobalProps(Properties props) {
    globalProps = props;
  }

  /**
   * Gets the global props.
   *
   * @return the global props
   */
  public Properties getGlobalProps() {
    return globalProps;
  }

  /**
   * Gets the tx props.
   *
   * @return the tx props
   */
  public Properties getTxProps() {
    return txProps;
  }

  /**
   * Gets the ds props.
   *
   * @return the ds props
   */
  public Properties getDsProps() {
    return dsProps;
  }

  /**
   * Gets the cache props.
   *
   * @return the cache props
   */
  public Properties getCacheProps() {
    return cacheProps;
  }

  /**
   * Sets the use statement namespaces.
   *
   * @param useStatementNamespaces
   *          the new use statement namespaces
   */
  public void setUseStatementNamespaces(boolean useStatementNamespaces) {
    this.useStatementNamespaces = useStatementNamespaces;
  }

  /**
   * Checks if is use statement namespaces.
   *
   * @return true, if is use statement namespaces
   */
  public boolean isUseStatementNamespaces() {
    return useStatementNamespaces;
  }

  /**
   * Gets the sql includes.
   *
   * @return the sql includes
   */
  public Map getSqlIncludes() {
    return sqlIncludes;
  }

  /**
   * Sets the namespace.
   *
   * @param namespace
   *          the new namespace
   */
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * Apply namespace.
   *
   * @param id
   *          the id
   * @return the string
   */
  public String applyNamespace(String id) {
    String newId = id;
    if (namespace != null && namespace.length() > 0 && id != null && id.indexOf('.') < 0) {
      newId = namespace + "." + id;
    }
    return newId;
  }

  /**
   * Gets the cache config.
   *
   * @return the cache config
   */
  public CacheModelConfig getCacheConfig() {
    return cacheConfig;
  }

  /**
   * Sets the cache config.
   *
   * @param cacheConfig
   *          the new cache config
   */
  public void setCacheConfig(CacheModelConfig cacheConfig) {
    this.cacheConfig = cacheConfig;
  }

  /**
   * Gets the param config.
   *
   * @return the param config
   */
  public ParameterMapConfig getParamConfig() {
    return paramConfig;
  }

  /**
   * Sets the param config.
   *
   * @param paramConfig
   *          the new param config
   */
  public void setParamConfig(ParameterMapConfig paramConfig) {
    this.paramConfig = paramConfig;
  }

  /**
   * Gets the result config.
   *
   * @return the result config
   */
  public ResultMapConfig getResultConfig() {
    return resultConfig;
  }

  /**
   * Sets the result config.
   *
   * @param resultConfig
   *          the new result config
   */
  public void setResultConfig(ResultMapConfig resultConfig) {
    this.resultConfig = resultConfig;
  }

  /**
   * Gets the first token.
   *
   * @param s
   *          the s
   * @return the first token
   */
  public String getFirstToken(String s) {
    return new StringTokenizer(s, ", ", false).nextToken();
  }

  /**
   * Gets the all but first token.
   *
   * @param s
   *          the s
   * @return the all but first token
   */
  public String[] getAllButFirstToken(String s) {
    List strings = new ArrayList();
    StringTokenizer parser = new StringTokenizer(s, ", ", false);
    parser.nextToken();
    while (parser.hasMoreTokens()) {
      strings.add(parser.nextToken());
    }
    return (String[]) strings.toArray(new String[strings.size()]);
  }

  /**
   * Sets the global properties.
   *
   * @param resource
   *          the resource
   * @param url
   *          the url
   */
  public void setGlobalProperties(String resource, String url) {
    config.getErrorContext().setActivity("loading global properties");
    try {
      Properties props;
      if (resource != null) {
        config.getErrorContext().setResource(resource);
        props = Resources.getResourceAsProperties(resource);
      } else if (url != null) {
        config.getErrorContext().setResource(url);
        props = Resources.getUrlAsProperties(url);
      } else {
        throw new RuntimeException("The " + "properties" + " element requires either a resource or a url attribute.");
      }

      // Merge properties with those passed in programmatically
      if (props != null) {
        props.putAll(globalProps);
        globalProps = props;
      }

      // Check for custom executors
      String customizedSQLExecutor = globalProps.getProperty("sql_executor_class");
      config.getErrorContext().setActivity("Loading SQLExecutor.");
      if (customizedSQLExecutor != null) {
        try {
          config.getClient().getDelegate().setCustomExecutor(customizedSQLExecutor);
          config.getClient().getDelegate().getSqlExecutor().init(config, globalProps);
        } catch (Exception e) {
          config.getErrorContext().setCause(e);
          config.getErrorContext()
              .setMoreInfo("Loading of customizedSQLExecutor failed. Please check Properties file.");
        }
      }

    } catch (Exception e) {
      throw new RuntimeException("Error loading properties.  Cause: " + e, e);
    }
  }

  /**
   * Gets the data source.
   *
   * @return the data source
   */
  public DataSource getDataSource() {
    return dataSource;
  }

  /**
   * Sets the data source.
   *
   * @param dataSource
   *          the new data source
   */
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }
}
