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
