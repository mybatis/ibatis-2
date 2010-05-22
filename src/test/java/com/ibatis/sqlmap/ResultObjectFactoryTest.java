package com.ibatis.sqlmap;

import testdomain.IItem;

import java.util.List;

public class ResultObjectFactoryTest extends BaseSqlMapTest {

  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig_rof.xml", null);
    initScript("scripts/jpetstore-hsqldb-schema.sql");
    initScript("scripts/jpetstore-hsqldb-dataload.sql");
  }

  /**
   * This tests that the result object factory is working -
   * everything in the sql map is declared as an interface.
   *
   */
  public void testShouldDemonstrateThatTheObjectFactoryIsWorking() throws Exception {
    List results =
        sqlMap.queryForList("getAllItemsROF");
    assertEquals(28, results.size());
    assertEquals(new Integer(1), ((IItem)results.get(2)).getSupplier().getSupplierId());
  }

}
