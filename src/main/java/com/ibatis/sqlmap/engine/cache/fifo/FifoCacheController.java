/*
 * Copyright 2004-2022 the original author or authors.
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
package com.ibatis.sqlmap.engine.cache.fifo;

import com.ibatis.sqlmap.engine.cache.CacheModel;
import com.ibatis.sqlmap.engine.cache.UsedCacheController;

/**
 * FIFO (first in, first out) cache controller implementation.
 */
public class FifoCacheController extends UsedCacheController {

  /**
   * Default constructor.
   */
  public FifoCacheController() {
    super();
  }

  /**
   * Get an object out of the cache.
   *
   * @param cacheModel
   *          The cache model
   * @param key
   *          The key of the object to be returned
   * @return The cached object (or null)
   */
  @Override
  public Object getObject(CacheModel cacheModel, Object key) {
    return cache.get(key);
  }

}
