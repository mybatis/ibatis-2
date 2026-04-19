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
package com.ibatis.sqlmap.engine.exchange;

import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ListDataExchangeTest {

  private final ListDataExchange exchange = new ListDataExchange(new DataExchangeFactory(new TypeHandlerFactory()));

  @Test
  void shouldGetDataByPropertyName() throws Exception {
    ParameterMap pm = paramMap("myProp");
    List<String> input = new ArrayList<>();
    input.add("ignored"); // index 0 isn't used here
    // parameterObject with a 'myProp' field – but ListDataExchange traverses probe
    // If the propertyName has no dots and no brackets it calls ProbeFactory on the
    // parameterObject. Use a simple Map-like bean approach.
    // Actually ListDataExchange uses ProbeFactory.getProbe().getObject() so use a Map.
    java.util.Map<String, Object> param = new java.util.HashMap<>();
    param.put("myProp", "hello");

    Object[] data = exchange.getData(null, pm, param);
    Assertions.assertEquals(1, data.length);
    Assertions.assertEquals("hello", data[0]);
  }

  @Test
  void shouldGetDataWithArrayIndexInPropertyName() throws Exception {
    ParameterMap pm = paramMap("[0]");
    List<String> list = List.of("first", "second");

    Object[] data = exchange.getData(null, pm, list);
    Assertions.assertEquals("first", data[0]);
  }

  @Test
  void shouldGetDataWithDottedPropertyPath() throws Exception {
    ParameterMap pm = paramMap("inner");

    java.util.Map<String, Object> inner = new java.util.HashMap<>();
    inner.put("value", 42);
    java.util.Map<String, Object> param = new java.util.HashMap<>();
    param.put("inner", inner);

    Object[] data = exchange.getData(null, pm, param);
    // "inner" resolves to the inner Map object
    Assertions.assertNotNull(data[0]);
    Assertions.assertTrue(data[0] instanceof java.util.Map);
  }

  @Test
  void shouldGetDataWithDeepDotPath() throws Exception {
    ParameterMap pm = paramMap("outer.key");

    java.util.Map<String, Object> inner = new java.util.HashMap<>();
    inner.put("key", "deepValue");
    java.util.Map<String, Object> param = new java.util.HashMap<>();
    param.put("outer", inner);

    Object[] data = exchange.getData(null, pm, param);
    Assertions.assertEquals("deepValue", data[0]);
  }

  @Test
  void shouldCallInitializeWithoutError() {
    exchange.initialize(new java.util.HashMap<>());
  }

  // ---- helpers ----------------------------------------------------------------

  private static ParameterMap paramMap(String propertyName) throws Exception {
    ParameterMap pm = new ParameterMap(new SqlMapExecutorDelegate());
    ParameterMapping mapping = new ParameterMapping();
    mapping.setPropertyName(propertyName);
    setField(pm, "parameterMappings", new ParameterMapping[] { mapping });
    return pm;
  }

  private static void setField(Object target, String name, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(name);
    field.setAccessible(true);
    field.set(target, value);
  }
}
