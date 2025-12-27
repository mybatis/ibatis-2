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

import com.ibatis.common.beans.ClassInfo;
import com.ibatis.common.beans.Probe;
import com.ibatis.common.beans.ProbeFactory;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapException;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;
import com.ibatis.sqlmap.engine.accessplan.AccessPlanFactory;
import com.ibatis.sqlmap.engine.cache.CacheController;
import com.ibatis.sqlmap.engine.cache.CacheModel;
import com.ibatis.sqlmap.engine.cache.fifo.FifoCacheController;
import com.ibatis.sqlmap.engine.cache.lru.LruCacheController;
import com.ibatis.sqlmap.engine.cache.memory.MemoryCacheController;
import com.ibatis.sqlmap.engine.datasource.DbcpDataSourceFactory;
import com.ibatis.sqlmap.engine.datasource.JndiDataSourceFactory;
import com.ibatis.sqlmap.engine.datasource.SimpleDataSourceFactory;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.result.Discriminator;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.ErrorContext;
import com.ibatis.sqlmap.engine.transaction.TransactionManager;
import com.ibatis.sqlmap.engine.transaction.external.ExternalTransactionConfig;
import com.ibatis.sqlmap.engine.transaction.jdbc.JdbcTransactionConfig;
import com.ibatis.sqlmap.engine.transaction.jta.JtaTransactionConfig;
import com.ibatis.sqlmap.engine.type.CustomTypeHandler;
import com.ibatis.sqlmap.engine.type.DomCollectionTypeMarker;
import com.ibatis.sqlmap.engine.type.DomTypeMarker;
import com.ibatis.sqlmap.engine.type.TypeHandler;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;
import com.ibatis.sqlmap.engine.type.XmlCollectionTypeMarker;
import com.ibatis.sqlmap.engine.type.XmlTypeMarker;

import java.util.Iterator;

/**
 * The Class SqlMapConfiguration.
 */
public class SqlMapConfiguration {

  /** The Constant PROBE. */
  private static final Probe PROBE = ProbeFactory.getProbe();

  /** The error context. */
  private ErrorContext errorContext;

  /** The delegate. */
  private SqlMapExecutorDelegate delegate;

  /** The type handler factory. */
  private TypeHandlerFactory typeHandlerFactory;

  /** The client. */
  private SqlMapClientImpl client;

  /** The default statement timeout. */
  private Integer defaultStatementTimeout;

  /**
   * Instantiates a new sql map configuration.
   */
  public SqlMapConfiguration() {
    errorContext = new ErrorContext();
    delegate = new SqlMapExecutorDelegate();
    typeHandlerFactory = delegate.getTypeHandlerFactory();
    client = new SqlMapClientImpl(delegate);
    registerDefaultTypeAliases();
  }

  /**
   * Gets the type handler factory.
   *
   * @return the type handler factory
   */
  public TypeHandlerFactory getTypeHandlerFactory() {
    return typeHandlerFactory;
  }

  /**
   * Gets the error context.
   *
   * @return the error context
   */
  public ErrorContext getErrorContext() {
    return errorContext;
  }

  /**
   * Gets the client.
   *
   * @return the client
   */
  public SqlMapClientImpl getClient() {
    return client;
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
   * Sets the class info cache enabled.
   *
   * @param classInfoCacheEnabled
   *          the new class info cache enabled
   */
  public void setClassInfoCacheEnabled(boolean classInfoCacheEnabled) {
    errorContext.setActivity("setting class info cache enabled/disabled");
    ClassInfo.setCacheEnabled(classInfoCacheEnabled);
  }

  /**
   * Sets the lazy loading enabled.
   *
   * @param lazyLoadingEnabled
   *          the new lazy loading enabled
   */
  public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
    errorContext.setActivity("setting lazy loading enabled/disabled");
    client.getDelegate().setLazyLoadingEnabled(lazyLoadingEnabled);
  }

  /**
   * Sets the statement caching enabled.
   *
   * @param statementCachingEnabled
   *          the new statement caching enabled
   */
  public void setStatementCachingEnabled(boolean statementCachingEnabled) {
    errorContext.setActivity("setting statement caching enabled/disabled");
    client.getDelegate().setStatementCacheEnabled(statementCachingEnabled);
  }

  /**
   * Sets the cache models enabled.
   *
   * @param cacheModelsEnabled
   *          the new cache models enabled
   */
  public void setCacheModelsEnabled(boolean cacheModelsEnabled) {
    errorContext.setActivity("setting cache models enabled/disabled");
    client.getDelegate().setCacheModelsEnabled(cacheModelsEnabled);
  }

  /**
   * Sets the enhancement enabled.
   *
   * @param enhancementEnabled
   *          the new enhancement enabled
   */
  public void setEnhancementEnabled(boolean enhancementEnabled) {
    errorContext.setActivity("setting enhancement enabled/disabled");
    try {
      enhancementEnabled = enhancementEnabled && Resources.classForName("net.sf.cglib.proxy.InvocationHandler") != null;
    } catch (ClassNotFoundException e) {
      enhancementEnabled = false;
    }
    client.getDelegate().setEnhancementEnabled(enhancementEnabled);
    AccessPlanFactory.setBytecodeEnhancementEnabled(enhancementEnabled);
  }

  /**
   * Sets the use column label.
   *
   * @param useColumnLabel
   *          the new use column label
   */
  public void setUseColumnLabel(boolean useColumnLabel) {
    client.getDelegate().setUseColumnLabel(useColumnLabel);
  }

  /**
   * Sets the force multiple result set support.
   *
   * @param forceMultipleResultSetSupport
   *          the new force multiple result set support
   */
  public void setForceMultipleResultSetSupport(boolean forceMultipleResultSetSupport) {
    client.getDelegate().setForceMultipleResultSetSupport(forceMultipleResultSetSupport);
  }

  /**
   * Sets the default statement timeout.
   *
   * @param defaultTimeout
   *          the new default statement timeout
   */
  public void setDefaultStatementTimeout(Integer defaultTimeout) {
    errorContext.setActivity("setting default timeout");
    if (defaultTimeout != null) {
      try {
        defaultStatementTimeout = defaultTimeout;
      } catch (NumberFormatException e) {
        throw new SqlMapException("Specified defaultStatementTimeout is not a valid integer");
      }
    }
  }

  /**
   * Sets the transaction manager.
   *
   * @param txManager
   *          the new transaction manager
   */
  public void setTransactionManager(TransactionManager txManager) {
    delegate.setTxManager(txManager);
  }

  /**
   * Sets the result object factory.
   *
   * @param rof
   *          the new result object factory
   */
  public void setResultObjectFactory(ResultObjectFactory rof) {
    delegate.setResultObjectFactory(rof);
  }

  /**
   * New type handler.
   *
   * @param javaType
   *          the java type
   * @param jdbcType
   *          the jdbc type
   * @param callback
   *          the callback
   */
  public void newTypeHandler(Class javaType, String jdbcType, Object callback) {
    try {
      errorContext.setActivity("building a building custom type handler");
      TypeHandlerFactory typeHandlerFactory = client.getDelegate().getTypeHandlerFactory();
      TypeHandler typeHandler;
      if (callback instanceof TypeHandlerCallback) {
        typeHandler = new CustomTypeHandler((TypeHandlerCallback) callback);
      } else if (callback instanceof TypeHandler) {
        typeHandler = (TypeHandler) callback;
      } else {
        throw new RuntimeException(
            "The object '" + callback + "' is not a valid implementation of TypeHandler or TypeHandlerCallback");
      }
      errorContext.setMoreInfo("Check the javaType attribute '" + javaType + "' (must be a classname) or the jdbcType '"
          + jdbcType + "' (must be a JDBC type name).");
      if (jdbcType != null && jdbcType.length() > 0) {
        typeHandlerFactory.register(javaType, jdbcType, typeHandler);
      } else {
        typeHandlerFactory.register(javaType, typeHandler);
      }
    } catch (Exception e) {
      throw new SqlMapException("Error registering occurred.  Cause: " + e, e);
    }
    errorContext.setMoreInfo(null);
    errorContext.setObjectId(null);
  }

  /**
   * New cache model config.
   *
   * @param id
   *          the id
   * @param controller
   *          the controller
   * @param readOnly
   *          the read only
   * @param serialize
   *          the serialize
   *
   * @return the cache model config
   */
  public CacheModelConfig newCacheModelConfig(String id, CacheController controller, boolean readOnly,
      boolean serialize) {
    return new CacheModelConfig(this, id, controller, readOnly, serialize);
  }

  /**
   * New parameter map config.
   *
   * @param id
   *          the id
   * @param parameterClass
   *          the parameter class
   *
   * @return the parameter map config
   */
  public ParameterMapConfig newParameterMapConfig(String id, Class parameterClass) {
    return new ParameterMapConfig(this, id, parameterClass);
  }

  /**
   * New result map config.
   *
   * @param id
   *          the id
   * @param resultClass
   *          the result class
   * @param groupBy
   *          the group by
   * @param extended
   *          the extended
   * @param xmlName
   *          the xml name
   *
   * @return the result map config
   */
  public ResultMapConfig newResultMapConfig(String id, Class resultClass, String groupBy, String extended,
      String xmlName) {
    return new ResultMapConfig(this, id, resultClass, groupBy, extended, xmlName);
  }

  /**
   * New mapped statement config.
   *
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
   * @param resultSetType
   *          the result set type
   * @param fetchSize
   *          the fetch size
   * @param allowRemapping
   *          the allow remapping
   * @param timeout
   *          the timeout
   * @param cacheModelName
   *          the cache model name
   * @param xmlResultName
   *          the xml result name
   *
   * @return the mapped statement config
   */
  public MappedStatementConfig newMappedStatementConfig(String id, MappedStatement statement, SqlSource processor,
      String parameterMapName, Class parameterClass, String resultMapName, String[] additionalResultMapNames,
      Class resultClass, Class[] additionalResultClasses, String resultSetType, Integer fetchSize,
      boolean allowRemapping, Integer timeout, String cacheModelName, String xmlResultName) {
    return new MappedStatementConfig(this, id, statement, processor, parameterMapName, parameterClass, resultMapName,
        additionalResultMapNames, resultClass, additionalResultClasses, cacheModelName, resultSetType, fetchSize,
        allowRemapping, timeout, defaultStatementTimeout, xmlResultName);
  }

  /**
   * Finalize sql map config.
   */
  public void finalizeSqlMapConfig() {
    wireUpCacheModels();
    bindResultMapDiscriminators();
  }

  /**
   * Resolve type handler.
   *
   * @param typeHandlerFactory
   *          the type handler factory
   * @param clazz
   *          the clazz
   * @param propertyName
   *          the property name
   * @param javaType
   *          the java type
   * @param jdbcType
   *          the jdbc type
   *
   * @return the type handler
   */
  TypeHandler resolveTypeHandler(TypeHandlerFactory typeHandlerFactory, Class clazz, String propertyName,
      Class javaType, String jdbcType) {
    return resolveTypeHandler(typeHandlerFactory, clazz, propertyName, javaType, jdbcType, false);
  }

  /**
   * Resolve type handler.
   *
   * @param typeHandlerFactory
   *          the type handler factory
   * @param clazz
   *          the clazz
   * @param propertyName
   *          the property name
   * @param javaType
   *          the java type
   * @param jdbcType
   *          the jdbc type
   * @param useSetterToResolve
   *          the use setter to resolve
   *
   * @return the type handler
   */
  TypeHandler resolveTypeHandler(TypeHandlerFactory typeHandlerFactory, Class clazz, String propertyName,
      Class javaType, String jdbcType, boolean useSetterToResolve) {
    TypeHandler handler;
    if (clazz == null) {
      // Unknown
      handler = typeHandlerFactory.getUnkownTypeHandler();
    } else if (DomTypeMarker.class.isAssignableFrom(clazz)) {
      // DOM
      handler = typeHandlerFactory.getTypeHandler(String.class, jdbcType);
    } else if (java.util.Map.class.isAssignableFrom(clazz)) {
      // Map
      if (javaType == null) {
        handler = typeHandlerFactory.getUnkownTypeHandler();
        // BUG 1012591 - typeHandlerFactory.getTypeHandler(java.lang.Object.class, jdbcType);
      } else {
        handler = typeHandlerFactory.getTypeHandler(javaType, jdbcType);
      }
    } else if (typeHandlerFactory.getTypeHandler(clazz, jdbcType) != null) {
      // Primitive
      handler = typeHandlerFactory.getTypeHandler(clazz, jdbcType);
    } else {
      // JavaBean
      if (javaType == null) {
        if (useSetterToResolve) {
          Class type = PROBE.getPropertyTypeForSetter(clazz, propertyName);
          handler = typeHandlerFactory.getTypeHandler(type, jdbcType);
        } else {
          Class type = PROBE.getPropertyTypeForGetter(clazz, propertyName);
          handler = typeHandlerFactory.getTypeHandler(type, jdbcType);
        }
      } else {
        handler = typeHandlerFactory.getTypeHandler(javaType, jdbcType);
      }
    }
    return handler;
  }

  /**
   * Register default type aliases.
   */
  private void registerDefaultTypeAliases() {
    // TRANSACTION ALIASES
    typeHandlerFactory.putTypeAlias("JDBC", JdbcTransactionConfig.class.getName());
    typeHandlerFactory.putTypeAlias("JTA", JtaTransactionConfig.class.getName());
    typeHandlerFactory.putTypeAlias("EXTERNAL", ExternalTransactionConfig.class.getName());

    // DATA SOURCE ALIASES
    typeHandlerFactory.putTypeAlias("SIMPLE", SimpleDataSourceFactory.class.getName());
    typeHandlerFactory.putTypeAlias("DBCP", DbcpDataSourceFactory.class.getName());
    typeHandlerFactory.putTypeAlias("JNDI", JndiDataSourceFactory.class.getName());

    // CACHE ALIASES
    typeHandlerFactory.putTypeAlias("FIFO", FifoCacheController.class.getName());
    typeHandlerFactory.putTypeAlias("LRU", LruCacheController.class.getName());
    typeHandlerFactory.putTypeAlias("MEMORY", MemoryCacheController.class.getName());

    // TYPE ALIASEs
    typeHandlerFactory.putTypeAlias("dom", DomTypeMarker.class.getName());
    typeHandlerFactory.putTypeAlias("domCollection", DomCollectionTypeMarker.class.getName());
    typeHandlerFactory.putTypeAlias("xml", XmlTypeMarker.class.getName());
    typeHandlerFactory.putTypeAlias("xmlCollection", XmlCollectionTypeMarker.class.getName());
  }

  /**
   * Wire up cache models.
   */
  private void wireUpCacheModels() {
    // Wire Up Cache Models
    Iterator cacheNames = client.getDelegate().getCacheModelNames();
    while (cacheNames.hasNext()) {
      String cacheName = (String) cacheNames.next();
      CacheModel cacheModel = client.getDelegate().getCacheModel(cacheName);
      Iterator statementNames = cacheModel.getFlushTriggerStatementNames();
      while (statementNames.hasNext()) {
        String statementName = (String) statementNames.next();
        MappedStatement statement = client.getDelegate().getMappedStatement(statementName);
        if (statement == null) {
          throw new RuntimeException("Could not find statement named '" + statementName
              + "' for use as a flush trigger for the cache model named '" + cacheName + "'.");
        }
        statement.addExecuteListener(cacheModel);
      }
    }
  }

  /**
   * Bind result map discriminators.
   */
  private void bindResultMapDiscriminators() {
    // Bind discriminators
    Iterator names = delegate.getResultMapNames();
    while (names.hasNext()) {
      String name = (String) names.next();
      ResultMap rm = delegate.getResultMap(name);
      Discriminator disc = rm.getDiscriminator();
      if (disc != null) {
        disc.bindSubMaps();
      }
    }
  }

}
