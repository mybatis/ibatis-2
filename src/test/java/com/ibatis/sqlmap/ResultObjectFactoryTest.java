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
package com.ibatis.sqlmap;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.IItem;

class ResultObjectFactoryTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    BaseSqlMap.initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig_rof.xml", null);
    BaseSqlMap.initScript("scripts/jpetstore-hsqldb-schema.sql");
    BaseSqlMap.initScript("scripts/jpetstore-hsqldb-dataload.sql");
  }

  /**
   * This tests that the result object factory is working - everything in the sql map is declared as an interface.
   */
  @Test
  void testShouldDemonstrateThatTheObjectFactoryIsWorking() throws Exception {
    final List<?> results = BaseSqlMap.sqlMap.queryForList("getAllItemsROF");
    Assertions.assertEquals(28, results.size());
    Assertions.assertEquals(Integer.valueOf(1), ((IItem) results.get(2)).getSupplier().getSupplierId());
  }

}
