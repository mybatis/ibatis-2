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
package com.ibatis.sqlmap.engine.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Hash value generator for cache keys.
 */
public class CacheKey implements Cloneable, Serializable {

  /** The Constant DEFAULT_MULTIPLYER. */
  private static final int DEFAULT_MULTIPLYER = 37;

  /** The Constant DEFAULT_HASHCODE. */
  private static final int DEFAULT_HASHCODE = 17;

  /** The multiplier. */
  private int multiplier;

  /** The hashcode. */
  private int hashcode;

  /** The checksum. */
  private long checksum;

  /** The count. */
  private int count;

  /** The param list. */
  private List paramList = new ArrayList<>();

  /**
   * Default constructor.
   */
  public CacheKey() {
    hashcode = DEFAULT_HASHCODE;
    multiplier = DEFAULT_MULTIPLYER;
    count = 0;
  }

  /**
   * Constructor that supplies an initial hashcode.
   *
   * @param initialNonZeroOddNumber
   *          - the hashcode to use
   */
  public CacheKey(int initialNonZeroOddNumber) {
    hashcode = initialNonZeroOddNumber;
    multiplier = DEFAULT_MULTIPLYER;
    count = 0;
  }

  /**
   * Costructor that supplies an initial hashcode and multiplier.
   *
   * @param initialNonZeroOddNumber
   *          - the hashcode to use
   * @param multiplierNonZeroOddNumber
   *          - the multiplier to use
   */
  public CacheKey(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
    hashcode = initialNonZeroOddNumber;
    multiplier = multiplierNonZeroOddNumber;
    count = 0;
  }

  /**
   * Updates this object with new information based on an int value.
   *
   * @param x
   *          - the int value
   *
   * @return the cache key
   */
  public CacheKey update(int x) {
    update(Integer.valueOf(x));
    return this;
  }

  /**
   * Updates this object with new information based on an object.
   *
   * @param object
   *          - the object
   *
   * @return the cachekey
   */
  public CacheKey update(Object object) {
    int baseHashCode = object.hashCode();

    count++;
    checksum += baseHashCode;
    baseHashCode *= count;

    hashcode = multiplier * hashcode + baseHashCode;

    paramList.add(object);

    return this;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object)
      return true;
    if (!(object instanceof CacheKey))
      return false;

    final CacheKey cacheKey = (CacheKey) object;

    if (hashcode != cacheKey.hashcode)
      return false;
    if (checksum != cacheKey.checksum)
      return false;
    if (count != cacheKey.count)
      return false;

    for (int i = 0; i < paramList.size(); i++) {
      Object thisParam = paramList.get(i);
      Object thatParam = cacheKey.paramList.get(i);
      if (thisParam == null) {
        if (thatParam != null)
          return false;
      } else {
        if (!thisParam.equals(thatParam))
          return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    return hashcode;
  }

  @Override
  public String toString() {
    StringBuilder returnValue = new StringBuilder().append(hashcode).append('|').append(checksum);
    for (int i = 0; i < paramList.size(); i++) {
      returnValue.append('|').append(paramList.get(i));
    }

    return returnValue.toString();
  }

  @Override
  public CacheKey clone() throws CloneNotSupportedException {
    CacheKey clonedCacheKey = (CacheKey) super.clone();
    clonedCacheKey.paramList = new ArrayList<Object>(paramList);
    return clonedCacheKey;
  }

}
