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
package com.ibatis.sqlmap.engine.exchange;

import com.ibatis.sqlmap.engine.cache.CacheKey;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * Base implementation for the DataExchange interface.
 */
public abstract class BaseDataExchange implements DataExchange {

  /** The data exchange factory. */
  private DataExchangeFactory dataExchangeFactory;

  /**
   * Instantiates a new base data exchange.
   *
   * @param dataExchangeFactory
   *          the data exchange factory
   */
  protected BaseDataExchange(DataExchangeFactory dataExchangeFactory) {
    this.dataExchangeFactory = dataExchangeFactory;
  }

  @Override
  public CacheKey getCacheKey(StatementScope statementScope, ParameterMap parameterMap, Object parameterObject) {
    CacheKey key = new CacheKey();
    Object[] data = getData(statementScope, parameterMap, parameterObject);
    for (Object element : data) {
      if (element != null) {
        key.update(element);
      }
    }
    return key;
  }

  /**
   * Getter for the factory that created this object.
   *
   * @return - the factory
   */
  public DataExchangeFactory getDataExchangeFactory() {
    return dataExchangeFactory;
  }

}
