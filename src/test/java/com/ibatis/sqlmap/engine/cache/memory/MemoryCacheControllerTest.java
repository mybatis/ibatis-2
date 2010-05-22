package com.ibatis.sqlmap.engine.cache.memory;

import com.ibatis.sqlmap.engine.cache.CacheController;
import com.ibatis.sqlmap.engine.cache.lru.LruCacheControllerTest;

public class MemoryCacheControllerTest extends LruCacheControllerTest {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(MemoryCacheControllerTest.class);
  }

  protected CacheController getController() {
    return new MemoryCacheController();
  }

  public void testSizeOne() {
    // This is not relevant for this model
  }
}
