/*
 * Copyright 2004-2022 the original author or authors.
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
package com.ibatis.common.jdbc.logging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// pull up method refactoring - ASDC - Assignment3
public class InvokeResultSetLog extends BaseLogProxy {

  /** The statement. */
  protected PreparedStatement statement;

  // pull up method refactoring - ASDC - Assignment3
  protected Object getResultSet(Method method, Object[] params)
      throws IllegalAccessException, InvocationTargetException {
    if ("executeQuery".equals(method.getName())) {
      ResultSet rs = (ResultSet) method.invoke(statement, params);
      if (rs != null) {
        return ResultSetLogProxy.newInstance(rs);
      } else {
        return null;
      }
    } else {
      return method.invoke(statement, params);
    }
  }
}
