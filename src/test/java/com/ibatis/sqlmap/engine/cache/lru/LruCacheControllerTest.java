/**
 * Copyright 2004-2015 the original author or authors.
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

import com.ibatis.sqlmap.engine.cache.CacheController;
import junit.framework.TestCase;

import java.util.Properties;

public class LruCacheControllerTest extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(LruCacheControllerTest.class);
  }

  protected CacheController getController() {
    return new LruCacheController();
  }

  public void testSizeOne() {
    CacheController cc = getController();
    String testKey = "testKey";
    String testVal = "testVal";
    Properties props = new Properties();
    props.setProperty("cache-size", "1");
    cc.setProperties(props);
    cc.putObject(null, testKey, testVal);
    assertEquals(testVal, cc.getObject(null, testKey));
    String testKey2 = "testKey2";
    String testVal2 = "testVal2";
    cc.putObject(null, testKey2, testVal2);
    assertEquals(testVal2, cc.getObject(null, testKey2));
    assertNull(cc.getObject(null, testKey));

  }

  public void testGetAndPutObject() {
    CacheController cc = getController();
    String testKey = "testKey";
    String testVal = "testVal";

    assertEquals(cc.getObject(null, testKey), null);

    cc.putObject(null, testKey, testVal);
    assertEquals(cc.getObject(null, testKey), testVal);

    cc.putObject(null, testKey, null);
    assertEquals(cc.getObject(null, testKey), null);

  }

  public void testRemoveObject() {
    CacheController cc = getController();
    String testKey = "testKey";
    String testVal = "testVal";

    assertEquals(cc.getObject(null, testKey), null);

    cc.putObject(null, testKey, testVal);
    assertEquals(cc.getObject(null, testKey), testVal);

    cc.removeObject(null, testKey);
    assertEquals(cc.getObject(null, testKey), null);
  }

  public void testFlush() {
    CacheController cc = getController();
    String testKey = "testKey";
    String testVal = "testVal";

    assertEquals(cc.getObject(null, testKey), null);

    cc.putObject(null, testKey, testVal);
    assertEquals(cc.getObject(null, testKey), testVal);

    cc.flush(null);
    assertEquals(cc.getObject(null, testKey), null);
  }

}
