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

import com.ibatis.sqlmap.engine.cache.*;
import com.ibatis.sqlmap.engine.impl.*;
import com.ibatis.sqlmap.engine.scope.*;

import java.util.Properties;

/**
 * The Class CacheModelConfig.
 */
public class CacheModelConfig {

  /** The error context. */
  private ErrorContext errorContext;

  /** The cache model. */
  private CacheModel cacheModel;

  /**
   * Instantiates a new cache model config.
   *
   * @param config
   *          the config
   * @param id
   *          the id
   * @param controller
   *          the controller
   * @param readOnly
   *          the read only
   * @param serialize
   *          the serialize
   */
  CacheModelConfig(SqlMapConfiguration config, String id, CacheController controller, boolean readOnly,
      boolean serialize) {
    this.errorContext = config.getErrorContext();
    this.cacheModel = new CacheModel();
    SqlMapClientImpl client = config.getClient();
    errorContext.setActivity("building a cache model");
    cacheModel.setReadOnly(readOnly);
    cacheModel.setSerialize(serialize);
    errorContext.setObjectId(id + " cache model");
    errorContext.setMoreInfo("Check the cache model type.");
    cacheModel.setId(id);
    cacheModel.setResource(errorContext.getResource());
    try {
      cacheModel.setCacheController(controller);
    } catch (Exception e) {
      throw new RuntimeException("Error setting Cache Controller Class.  Cause: " + e, e);
    }
    errorContext.setMoreInfo("Check the cache model configuration.");
    if (client.getDelegate().isCacheModelsEnabled()) {
      client.getDelegate().addCacheModel(cacheModel);
    }
    errorContext.setMoreInfo(null);
    errorContext.setObjectId(null);
  }

  /**
   * Sets the flush interval.
   *
   * @param hours
   *          the hours
   * @param minutes
   *          the minutes
   * @param seconds
   *          the seconds
   * @param milliseconds
   *          the milliseconds
   */
  public void setFlushInterval(long hours, long minutes, long seconds, long milliseconds) {
    errorContext.setMoreInfo("Check the cache model flush interval.");
    long t = 0L;
    t += milliseconds;
    t += seconds * 1000L;
    t += minutes * 60L * 1000L;
    t += hours * 60L * 60L * 1000L;
    if (t < 1L)
      throw new RuntimeException(
          "A flush interval must specify one or more of milliseconds, seconds, minutes or hours.");
    cacheModel.setFlushInterval(t);
  }

  /**
   * Adds the flush trigger statement.
   *
   * @param statement
   *          the statement
   */
  public void addFlushTriggerStatement(String statement) {
    errorContext.setMoreInfo("Check the cache model flush on statement elements.");
    cacheModel.addFlushTriggerStatement(statement);
  }

  /**
   * Gets the cache model.
   *
   * @return the cache model
   */
  public CacheModel getCacheModel() {
    return cacheModel;
  }

  /**
   * Sets the controller properties.
   *
   * @param cacheProps
   *          the new controller properties
   */
  public void setControllerProperties(Properties cacheProps) {
    cacheModel.setControllerProperties(cacheProps);
  }
}
