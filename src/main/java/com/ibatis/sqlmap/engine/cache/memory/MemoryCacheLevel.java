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
package com.ibatis.sqlmap.engine.cache.memory;

import com.ibatis.sqlmap.client.SqlMapException;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration for the values for the memory cache levels.
 */
public final class MemoryCacheLevel {

  /** The cache level map. */
  private static Map cacheLevelMap = new HashMap<>();

  /**
   * Constant for weak caching This cache model is probably the best choice in most cases. It will increase performance
   * for popular results, but it will absolutely release the memory to be used in allocating other objects, assuming
   * that the results are not currently in use.
   */
  public final static MemoryCacheLevel WEAK;

  /**
   * Constant for soft caching. This cache model will reduce the likelihood of running out of memory in case the results
   * are not currently in use and the memory is needed for other objects. However, this is not the most aggressive
   * cache-model in that regard. Hence, memory still might be allocated and unavailable for more important objects.
   */
  public final static MemoryCacheLevel SOFT;

  /**
   * Constant for strong caching. This cache model will guarantee that the results stay in memory until the cache is
   * explicitly flushed. This is ideal for results that are:
   * <ol>
   * <li>very small</li>
   * <li>absolutely static</li>
   * <li>used very often</li>
   * </ol>
   * The advantage is that performance will be very good for this particular query. The disadvantage is that if the
   * memory used by these results is needed, then it will not be released to make room for other objects (possibly more
   * important objects).
   */
  public final static MemoryCacheLevel STRONG;

  /** The reference type. */
  private String referenceType;

  static {
    WEAK = new MemoryCacheLevel("WEAK");
    SOFT = new MemoryCacheLevel("SOFT");
    STRONG = new MemoryCacheLevel("STRONG");

    cacheLevelMap.put(WEAK.referenceType, WEAK);
    cacheLevelMap.put(SOFT.referenceType, SOFT);
    cacheLevelMap.put(STRONG.referenceType, STRONG);
  }

  /**
   * Creates a new instance of CacheLevel.
   *
   * @param type
   *          the type
   */
  private MemoryCacheLevel(String type) {
    this.referenceType = type;
  }

  /**
   * Getter for the reference type.
   *
   * @return the type of reference type used
   */
  public String getReferenceType() {
    return this.referenceType;
  }

  /**
   * Gets a MemoryCacheLevel by name.
   *
   * @param refType
   *          the name of the reference type
   *
   * @return the MemoryCacheLevel that the name indicates
   */
  public static MemoryCacheLevel getByReferenceType(String refType) {
    MemoryCacheLevel cacheLevel = (MemoryCacheLevel) cacheLevelMap.get(refType);
    if (cacheLevel == null) {
      throw new SqlMapException("Error getting CacheLevel (reference type) for name: '" + refType + "'.");
    }
    return cacheLevel;
  }
}
