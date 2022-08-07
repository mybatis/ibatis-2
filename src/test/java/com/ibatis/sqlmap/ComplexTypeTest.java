/*
 * Copyright 2004-2021 the original author or authors.
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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import testdomain.ComplexBean;

class ComplexTypeTest extends BaseSqlMap {

  // SETUP & TEARDOWN

  @BeforeEach
  void setUp() throws Exception {
    initSqlMap("com/ibatis/sqlmap/maps/SqlMapConfig.xml", null);
    initScript("scripts/account-init.sql");
    initScript("scripts/order-init.sql");
    initScript("scripts/line_item-init.sql");
  }

  @Test
  void testMapBeanMap() throws Exception {
    Map<String, ComplexBean> map = new HashMap<>();
    ComplexBean bean = new ComplexBean();
    bean.setMap(new HashMap<String, Integer>());
    bean.getMap().put("id", Integer.valueOf(1));
    map.put("bean", bean);

    Integer id = (Integer) sqlMap.queryForObject("mapBeanMap", map);

    assertEquals(id, bean.getMap().get("id"));
  }

}
