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
package com.ibatis.sqlmap.engine.mapping.result.loader;

import com.ibatis.common.beans.ClassInfo;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.type.DomTypeMarker;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.NoOp;

/**
 * Class to lazily load results into objects (uses CGLib to improve performance).
 */
public class EnhancedLazyResultLoader {

  /** The Constant SET_INTERFACES. */
  private static final Class[] SET_INTERFACES = { Set.class };

  /** The Constant LIST_INTERFACES. */
  private static final Class[] LIST_INTERFACES = { List.class };

  /** The loader. */
  private Object loader;

  /**
   * Constructor for an enhanced lazy list loader.
   *
   * @param client
   *          - the client that is creating the lazy list
   * @param statementName
   *          - the statement to be used to build the list
   * @param parameterObject
   *          - the parameter object to be used to build the list
   * @param targetType
   *          - the type we are putting data into
   */
  public EnhancedLazyResultLoader(SqlMapClientImpl client, String statementName, Object parameterObject,
      Class targetType) {
    loader = new EnhancedLazyResultLoaderImpl(client, statementName, parameterObject, targetType);
  }

  /**
   * Loads the result.
   *
   * @return the results - a list or object
   *
   * @throws SQLException
   *           if there is a problem
   */
  public Object loadResult() throws SQLException {
    return ((EnhancedLazyResultLoaderImpl) loader).loadResult();
  }

  /**
   * The Class EnhancedLazyResultLoaderImpl.
   */
  private static class EnhancedLazyResultLoaderImpl implements LazyLoader {

    /** The client. */
    protected SqlMapClientImpl client;

    /** The statement name. */
    protected String statementName;

    /** The parameter object. */
    protected Object parameterObject;

    /** The target type. */
    protected Class targetType;

    /**
     * Constructor for an enhanced lazy list loader implementation.
     *
     * @param client
     *          - the client that is creating the lazy list
     * @param statementName
     *          - the statement to be used to build the list
     * @param parameterObject
     *          - the parameter object to be used to build the list
     * @param targetType
     *          - the type we are putting data into
     */
    public EnhancedLazyResultLoaderImpl(SqlMapClientImpl client, String statementName, Object parameterObject,
        Class targetType) {
      this.client = client;
      this.statementName = statementName;
      this.parameterObject = parameterObject;
      this.targetType = targetType;
    }

    /**
     * Loads the result.
     *
     * @return the results - a list or object
     *
     * @throws SQLException
     *           if there is a problem
     */
    public Object loadResult() throws SQLException {
      if (DomTypeMarker.class.isAssignableFrom(targetType)) {
        return ResultLoader.getResult(client, statementName, parameterObject, targetType);
      } else if (Collection.class.isAssignableFrom(targetType)) {
        if (Set.class.isAssignableFrom(targetType)) {
          return Enhancer.create(Object.class, SET_INTERFACES, this);
        } else {
          return Enhancer.create(Object.class, LIST_INTERFACES, this);
        }
      } else if (targetType.isArray() || ClassInfo.isKnownType(targetType)) {
        return ResultLoader.getResult(client, statementName, parameterObject, targetType);
      } else {
        return Enhancer.create(targetType, this);
      }
    }

    @Override
    public Object loadObject() throws Exception {
      try {
        Object result = ResultLoader.getResult(client, statementName, parameterObject, targetType);
        if (result == null) {
          // if no result is available return a proxy with a default object is returned
          // because the loadObject() method must not return null
          result = Enhancer.create(targetType, NoOp.INSTANCE);
        }
        return result;
      } catch (SQLException e) {
        throw new RuntimeException("Error lazy loading result. Cause: " + e, e);
      }
    }
  }

}
