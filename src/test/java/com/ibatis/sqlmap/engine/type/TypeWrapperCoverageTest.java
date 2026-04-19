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
package com.ibatis.sqlmap.engine.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TypeWrapperCoverageTest {

  @Test
  void shouldExerciseCallableStatementResultSetMethods() {
    Set<String> delegatedCalls = new HashSet<>();
    CallableStatement callableStatement = (CallableStatement) Proxy.newProxyInstance(
        CallableStatement.class.getClassLoader(), new Class[] { CallableStatement.class }, (proxy, method, args) -> {
          delegatedCalls.add(method.getName());
          return defaultValue(method.getReturnType());
        });

    CallableStatementResultSet resultSet = new CallableStatementResultSet(callableStatement);

    for (Method method : CallableStatementResultSet.class.getMethods()) {
      if (!method.getDeclaringClass().equals(CallableStatementResultSet.class)) {
        continue;
      }
      try {
        method.invoke(resultSet, buildArguments(method.getParameterTypes()));
      } catch (InvocationTargetException ex) {
        Throwable cause = ex.getCause();
        if (!(cause instanceof UnsupportedOperationException) && !(cause instanceof SQLException)) {
          throw new RuntimeException(cause);
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }

    Assertions.assertTrue(delegatedCalls.contains("getInt"));
    Assertions.assertTrue(delegatedCalls.contains("getString"));
    Assertions.assertTrue(delegatedCalls.contains("wasNull"));
  }

  @Test
  void shouldExerciseParameterSetterAndResultGetterPaths() {
    Set<String> setterCalls = new HashSet<>();
    PreparedStatement preparedStatement = (PreparedStatement) Proxy.newProxyInstance(
        PreparedStatement.class.getClassLoader(), new Class[] { PreparedStatement.class }, (proxy, method, args) -> {
          setterCalls.add(method.getName());
          return defaultValue(method.getReturnType());
        });

    ParameterSetterImpl setter = new ParameterSetterImpl(preparedStatement, 2);
    for (Method method : ParameterSetterImpl.class.getDeclaredMethods()) {
      if (method.getDeclaringClass().equals(Object.class) || method.isSynthetic()
          || Modifier.isPrivate(method.getModifiers())) {
        continue;
      }
      try {
        method.invoke(setter, buildArguments(method.getParameterTypes()));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }

    Assertions.assertSame(preparedStatement, setter.getPreparedStatement());
    Assertions.assertEquals(2, setter.getParameterIndex());
    Assertions.assertTrue(setterCalls.contains("setString"));
    Assertions.assertTrue(setterCalls.contains("setInt"));

    Map<String, Object> resultValues = new HashMap<>();
    resultValues.put("col", "value");
    Set<String> getterCalls = new HashSet<>();

    ResultSet resultSet = (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
        new Class[] { ResultSet.class }, (proxy, method, args) -> {
          getterCalls.add(method.getName());
          if (method.getReturnType().equals(String.class)) {
            return resultValues.get("col");
          }
          return defaultValue(method.getReturnType());
        });

    ResultGetterImpl getterByName = new ResultGetterImpl(resultSet, "col");
    ResultGetterImpl getterByIndex = new ResultGetterImpl(resultSet, 1);

    for (Method method : ResultGetterImpl.class.getDeclaredMethods()) {
      if (method.getName().startsWith("lambda$") || method.getDeclaringClass().equals(Object.class)
          || method.isSynthetic() || Modifier.isPrivate(method.getModifiers())) {
        continue;
      }
      try {
        method.invoke(getterByName, buildArguments(method.getParameterTypes()));
        method.invoke(getterByIndex, buildArguments(method.getParameterTypes()));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }

    Assertions.assertTrue(getterCalls.contains("getString"));
    Assertions.assertTrue(getterCalls.contains("getInt"));
    Assertions.assertTrue(getterCalls.contains("wasNull"));
  }

  private static Object[] buildArguments(Class<?>[] parameterTypes) {
    Object[] args = new Object[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      args[i] = buildArgument(parameterTypes[i]);
    }
    return args;
  }

  private static Object buildArgument(Class<?> type) {
    if (!type.isPrimitive()) {
      if (type == String.class) {
        return "col";
      }
      if (type == Calendar.class) {
        return Calendar.getInstance();
      }
      if (type == Map.class) {
        return new HashMap<>();
      }
      if (type == Class.class) {
        return Object.class;
      }
      return null;
    }
    if (type == boolean.class) {
      return true;
    }
    if (type == int.class) {
      return 1;
    }
    if (type == long.class) {
      return 1L;
    }
    if (type == double.class) {
      return 1.0D;
    }
    if (type == float.class) {
      return 1.0F;
    }
    if (type == short.class) {
      return (short) 1;
    }
    if (type == byte.class) {
      return (byte) 1;
    }
    if (type == char.class) {
      return 'a';
    }
    return null;
  }

  private static Object defaultValue(Class<?> returnType) {
    if (!returnType.isPrimitive()) {
      return null;
    }
    if (returnType == boolean.class) {
      return false;
    }
    if (returnType == int.class) {
      return 0;
    }
    if (returnType == long.class) {
      return 0L;
    }
    if (returnType == double.class) {
      return 0D;
    }
    if (returnType == float.class) {
      return 0F;
    }
    if (returnType == short.class) {
      return (short) 0;
    }
    if (returnType == byte.class) {
      return (byte) 0;
    }
    if (returnType == char.class) {
      return (char) 0;
    }
    return null;
  }
}
