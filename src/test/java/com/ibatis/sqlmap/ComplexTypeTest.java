package com.ibatis.sqlmap;

import testdomain.ComplexBean;

import java.util.HashMap;
import java.util.Map;

public class ComplexTypeTest extends BaseSqlMapTest {

  // SETUP & TEARDOWN

  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
    initScript("scripts/order-init.sql");
    initScript("scripts/line_item-init.sql");
  }

  protected void tearDown() throws Exception {
  }

  public void testMapBeanMap() throws Exception {
    Map map = new HashMap();
    ComplexBean bean = new ComplexBean();
    bean.setMap(new HashMap());
    bean.getMap().put("id", new Integer(1));
    map.put("bean", bean);

    Integer id = (Integer) sqlMap.queryForObject("mapBeanMap", map);

    assertEquals(id, bean.getMap().get("id"));
  }


}
