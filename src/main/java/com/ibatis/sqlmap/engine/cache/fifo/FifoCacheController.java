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
package com.ibatis.sqlmap.engine.cache.fifo;

import com.ibatis.sqlmap.engine.cache.CacheController;
import com.ibatis.sqlmap.engine.cache.CacheModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * FIFO (first in, first out) cache controller implementation.
 */
public class FifoCacheController implements CacheController {

  /** The cache size. */
  private int cacheSize;

  /** The cache. */
  private Map cache;

  /** The key list. */
  private List keyList;

  /**
   * Default constructor.
   */
  public FifoCacheController() {
    this.cacheSize = 100;
    this.cache = Collections.synchronizedMap(new HashMap<>());
    this.keyList = Collections.synchronizedList(new LinkedList());
  }

  /**
   * Gets the cache size.
   *
   * @return the cache size
   */
  public int getCacheSize() {
    return cacheSize;
  }

  /**
   * Sets the cache size.
   *
   * @param cacheSize
   *          the new cache size
   */
  public void setCacheSize(int cacheSize) {
    this.cacheSize = cacheSize;
  }

  /**
   * Configures the cache
   *
   * @param props
   *          Optionally can contain properties [reference-type=WEAK|SOFT|STRONG]
   */
  public void setProperties(Properties props) {
    String size = props.getProperty("cache-size");
    if (size == null) {
      size = props.getProperty("size");
    }
    if (size != null) {
      cacheSize = Integer.parseInt(size);
    }
  }

  /**
   * Add an object to the cache
   *
   * @param cacheModel
   *          The cacheModel
   * @param key
   *          The key of the object to be cached
   * @param value
   *          The object to be cached
   */
  public void putObject(CacheModel cacheModel, Object key, Object value) {
    cache.put(key, value);
    keyList.add(key);
    if (keyList.size() > cacheSize) {
      try {
        Object oldestKey = keyList.remove(0);
        cache.remove(oldestKey);
      } catch (IndexOutOfBoundsException e) {
        // ignore
      }
    }
  }

  /**
   * Get an object out of the cache.
   *
   * @param cacheModel
   *          The cache model
   * @param key
   *          The key of the object to be returned
   *
   * @return The cached object (or null)
   */
  public Object getObject(CacheModel cacheModel, Object key) {
    return cache.get(key);
  }

  public Object removeObject(CacheModel cacheModel, Object key) {
    keyList.remove(key);
    return cache.remove(key);
  }

  /**
   * Flushes the cache.
   *
   * @param cacheModel
   *          The cache model
   */
  public void flush(CacheModel cacheModel) {
    cache.clear();
    keyList.clear();
  }

}
