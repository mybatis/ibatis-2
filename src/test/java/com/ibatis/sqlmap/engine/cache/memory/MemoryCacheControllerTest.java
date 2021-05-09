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
package com.ibatis.sqlmap.engine.cache.memory;

import com.ibatis.sqlmap.engine.cache.CacheController;
import com.ibatis.sqlmap.engine.cache.lru.LruCacheControllerTest;

import org.junit.jupiter.api.Test;

public class MemoryCacheControllerTest extends LruCacheControllerTest {

  @Override
  protected CacheController getController() {
    return new MemoryCacheController();
  }

  @Override
  @Test
  public void testSizeOne() {
    // This is not relevant for this model
  }
}
