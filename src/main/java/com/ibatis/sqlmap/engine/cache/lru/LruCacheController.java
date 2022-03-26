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
package com.ibatis.sqlmap.engine.cache.lru;

import com.ibatis.sqlmap.engine.cache.CacheModel;
import com.ibatis.sqlmap.engine.cache.UsedCacheController;

/**
 * LRU (least recently used) cache controller implementation.
 */
public class LruCacheController extends UsedCacheController {

  /**
   * Default constructor.
   */
  public LruCacheController() {
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
    Object result = cache.get(key);
    keyList.remove(key);
    if (result != null) {
      keyList.add(key);
    }
    return result;
  }

}
