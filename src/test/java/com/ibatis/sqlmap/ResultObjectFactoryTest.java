/**
 * Copyright 2004-2017 the original author or authors.
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
package com.ibatis.sqlmap;

import testdomain.IItem;

import java.util.List;

public class ResultObjectFactoryTest extends BaseSqlMapTest {

  @Override
  protected void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig_rof.xml", null);
    initScript("scripts/jpetstore-hsqldb-schema.sql");
    initScript("scripts/jpetstore-hsqldb-dataload.sql");
  }

  /**
   * This tests that the result object factory is working - everything in the sql map is declared as an interface.
   *
   */
  public void testShouldDemonstrateThatTheObjectFactoryIsWorking() throws Exception {
    List results = sqlMap.queryForList("getAllItemsROF");
    assertEquals(28, results.size());
    assertEquals(new Integer(1), ((IItem) results.get(2)).getSupplier().getSupplierId());
  }

}
