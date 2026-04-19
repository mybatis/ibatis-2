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
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMapping;
import com.ibatis.sqlmap.engine.type.TypeHandlerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MapDataExchangeTest {

  @Test
  void shouldGetAndSetDataForMapObjects() throws Exception {
    MapDataExchange exchange = new MapDataExchange(new DataExchangeFactory(new TypeHandlerFactory()));

    ParameterMap parameterMap = new ParameterMap(new SqlMapExecutorDelegate());
    ParameterMapping in = new ParameterMapping();
    in.setPropertyName("in");
    ParameterMapping out = new ParameterMapping();
    out.setPropertyName("out");
    out.setMode("OUT");
    setField(parameterMap, "parameterMappings", new ParameterMapping[] { in, out });

    Map<String, Object> input = new HashMap<>();
    input.put("in", 10);
    input.put("out", 20);
    Assertions.assertArrayEquals(new Object[] { 10, 20 }, exchange.getData(null, parameterMap, input));

    ResultMap resultMap = new ResultMap(new SqlMapExecutorDelegate());
    ResultMapping first = new ResultMapping();
    first.setPropertyName("first");
    ResultMapping second = new ResultMapping();
    second.setPropertyName("second");
    setField(resultMap, "resultMappings", new ResultMapping[] { first, second });

    Object created = exchange.setData(null, resultMap, null, new Object[] { "a", "b" });
    Assertions.assertEquals("a", ((Map<?, ?>) created).get("first"));
    Assertions.assertEquals("b", ((Map<?, ?>) created).get("second"));

    Map<String, Object> parameterTarget = new HashMap<>();
    Object updated = exchange.setData(null, parameterMap, parameterTarget, new Object[] { 1, 2 });
    Assertions.assertEquals(2, ((Map<?, ?>) updated).get("out"));
    Assertions.assertFalse(((Map<?, ?>) updated).containsKey("in"));
  }

  @Test
  void shouldRejectNonMapInputs() throws Exception {
    MapDataExchange exchange = new MapDataExchange(new DataExchangeFactory(new TypeHandlerFactory()));

    ParameterMap parameterMap = new ParameterMap(new SqlMapExecutorDelegate());
    setField(parameterMap, "parameterMappings", new ParameterMapping[] { new ParameterMapping() });

    ResultMap resultMap = new ResultMap(new SqlMapExecutorDelegate());
    ResultMapping resultMapping = new ResultMapping();
    resultMapping.setPropertyName("x");
    setField(resultMap, "resultMappings", new ResultMapping[] { resultMapping });

    Assertions.assertThrows(RuntimeException.class, () -> exchange.getData(null, parameterMap, new Object()));
    Assertions.assertThrows(RuntimeException.class,
        () -> exchange.setData(null, resultMap, new Object(), new Object[] { "x" }));
    Assertions.assertThrows(RuntimeException.class,
        () -> exchange.setData(null, parameterMap, new Object(), new Object[] { "x" }));
  }

  private static void setField(Object target, String name, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(name);
    field.setAccessible(true);
    field.set(target, value);
  }
}
