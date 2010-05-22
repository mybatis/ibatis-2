package com.ibatis.sqlmap.engine.cache.fifo;

import com.ibatis.sqlmap.engine.cache.CacheController;
import com.ibatis.sqlmap.engine.cache.lru.LruCacheControllerTest;

public class FifoCacheControllerTest extends LruCacheControllerTest {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(FifoCacheControllerTest.class);
  }

  protected CacheController getController(){
    return new FifoCacheController();
  }

}
