/*
 * Copyright 2004-2022 the original author or authors.
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
package com.ibatis.sqlmap.engine.accessplan;

import com.ibatis.common.beans.ClassInfo;
import com.ibatis.common.beans.Invoker;

/**
 * Property access plan (for working with beans).
 */
public class PropertyAccessPlan extends BaseAccessPlan {

  /** The Constant NO_ARGUMENTS. */
  protected static final Object[] NO_ARGUMENTS = new Object[0];

  /** The setters. */
  protected Invoker[] setters;

  /** The getters. */
  protected Invoker[] getters;

  /**
   * Instantiates a new property access plan.
   *
   * @param clazz
   *          the clazz
   * @param propertyNames
   *          the property names
   */
  PropertyAccessPlan(Class clazz, String[] propertyNames) {
    super(clazz, propertyNames);
    setters = getSetters(propertyNames);
    getters = getGetters(propertyNames);
  }

  public void setProperties(Object object, Object[] values) {
    int i = 0;
    try {
      Object[] arg = new Object[1];
      for (i = 0; i < propertyNames.length; i++) {
        arg[0] = values[i];
        try {
          setters[i].invoke(object, arg);
        } catch (Throwable t) {
          throw ClassInfo.unwrapThrowable(t);
        }
      }
    } catch (Throwable t) {
      throw new RuntimeException(
          "Error setting property '" + setters[i].getName() + "' of '" + object + "'.  Cause: " + t, t);
    }
  }

  public Object[] getProperties(Object object) {
    int i = 0;
    Object[] values = new Object[propertyNames.length];
    try {
      for (i = 0; i < propertyNames.length; i++) {
        try {
          values[i] = getters[i].invoke(object, NO_ARGUMENTS);
        } catch (Throwable t) {
          throw ClassInfo.unwrapThrowable(t);
        }
      }
    } catch (Throwable t) {
      throw new RuntimeException(
          "Error getting property '" + getters[i].getName() + "' of '" + object + "'.  Cause: " + t, t);
    }
    return values;
  }

}
