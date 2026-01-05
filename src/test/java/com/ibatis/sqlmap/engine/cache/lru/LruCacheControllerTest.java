/*
 * Copyright 2004-2026 the original author or authors.
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
package com.ibatis.sqlmap.engine.cache.lru;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ibatis.sqlmap.engine.cache.CacheController;

import java.util.Properties;

import org.junit.jupiter.api.Test;

public class LruCacheControllerTest {

  protected CacheController getController() {
    return new LruCacheController();
  }

  @Test
  public void testSizeOne() {
    final CacheController cc = getController();
    final String testKey = "testKey";
    final String testVal = "testVal";
    final Properties props = new Properties();
    props.setProperty("cache-size", "1");
    cc.setProperties(props);
    cc.putObject(null, testKey, testVal);
    assertEquals(testVal, cc.getObject(null, testKey));
    final String testKey2 = "testKey2";
    final String testVal2 = "testVal2";
    cc.putObject(null, testKey2, testVal2);
    assertEquals(testVal2, cc.getObject(null, testKey2));
    assertNull(cc.getObject(null, testKey));

  }

  @Test
  public void testGetAndPutObject() {
    final CacheController cc = getController();
    final String testKey = "testKey";
    final String testVal = "testVal";

    assertEquals(cc.getObject(null, testKey), null);

    cc.putObject(null, testKey, testVal);
    assertEquals(cc.getObject(null, testKey), testVal);

    cc.putObject(null, testKey, null);
    assertEquals(cc.getObject(null, testKey), null);

  }

  @Test
  public void testRemoveObject() {
    final CacheController cc = getController();
    final String testKey = "testKey";
    final String testVal = "testVal";

    assertEquals(cc.getObject(null, testKey), null);

    cc.putObject(null, testKey, testVal);
    assertEquals(cc.getObject(null, testKey), testVal);

    cc.removeObject(null, testKey);
    assertEquals(cc.getObject(null, testKey), null);
  }

  @Test
  public void testFlush() {
    final CacheController cc = getController();
    final String testKey = "testKey";
    final String testVal = "testVal";

    assertEquals(cc.getObject(null, testKey), null);

    cc.putObject(null, testKey, testVal);
    assertEquals(cc.getObject(null, testKey), testVal);

    cc.flush(null);
    assertEquals(cc.getObject(null, testKey), null);
  }

}
