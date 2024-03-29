/*
 * Copyright 2004-2023 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.IItem;

class ResultObjectFactoryTest extends BaseSqlMap {

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig_rof.xml", null);
    initScript("scripts/jpetstore-hsqldb-schema.sql");
    initScript("scripts/jpetstore-hsqldb-dataload.sql");
  }

  /**
   * This tests that the result object factory is working - everything in the sql map is declared as an interface.
   */
  @Test
  void testShouldDemonstrateThatTheObjectFactoryIsWorking() throws Exception {
    List<?> results = sqlMap.queryForList("getAllItemsROF");
    assertEquals(28, results.size());
    assertEquals(Integer.valueOf(1), ((IItem) results.get(2)).getSupplier().getSupplierId());
  }

}
