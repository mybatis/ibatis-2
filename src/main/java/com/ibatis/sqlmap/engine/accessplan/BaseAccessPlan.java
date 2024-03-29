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
 * Base implementation of the AccessPlan interface.
 */
public abstract class BaseAccessPlan implements AccessPlan {

  /** The clazz. */
  protected Class clazz;

  /** The property names. */
  protected String[] propertyNames;

  /** The info. */
  protected ClassInfo info;

  /**
   * Instantiates a new base access plan.
   *
   * @param clazz
   *          the clazz
   * @param propertyNames
   *          the property names
   */
  BaseAccessPlan(Class clazz, String[] propertyNames) {
    this.clazz = clazz;
    this.propertyNames = propertyNames;
    info = ClassInfo.getInstance(clazz);
  }

  /**
   * Gets the types.
   *
   * @param propertyNames
   *          the property names
   *
   * @return the types
   */
  protected Class[] getTypes(String[] propertyNames) {
    Class[] types = new Class[propertyNames.length];
    for (int i = 0; i < propertyNames.length; i++) {
      types[i] = info.getGetterType(propertyNames[i]);
    }
    return types;
  }

  /**
   * Gets the getters.
   *
   * @param propertyNames
   *          the property names
   *
   * @return the getters
   */
  protected Invoker[] getGetters(String[] propertyNames) {
    Invoker[] methods = new Invoker[propertyNames.length];
    for (int i = 0; i < propertyNames.length; i++) {
      methods[i] = info.getGetInvoker(propertyNames[i]);
    }
    return methods;
  }

  /**
   * Gets the setters.
   *
   * @param propertyNames
   *          the property names
   *
   * @return the setters
   */
  protected Invoker[] getSetters(String[] propertyNames) {
    Invoker[] methods = new Invoker[propertyNames.length];
    for (int i = 0; i < propertyNames.length; i++) {
      methods[i] = info.getSetInvoker(propertyNames[i]);
    }
    return methods;
  }

  /**
   * Gets the getter names.
   *
   * @param propertyNames
   *          the property names
   *
   * @return the getter names
   */
  protected String[] getGetterNames(String[] propertyNames) {
    String[] names = new String[propertyNames.length];
    for (int i = 0; i < propertyNames.length; i++) {
      names[i] = info.getGetter(propertyNames[i]).getName();
    }
    return names;
  }

  /**
   * Gets the setter names.
   *
   * @param propertyNames
   *          the property names
   *
   * @return the setter names
   */
  protected String[] getSetterNames(String[] propertyNames) {
    String[] names = new String[propertyNames.length];
    for (int i = 0; i < propertyNames.length; i++) {
      names[i] = info.getSetter(propertyNames[i]).getName();
    }
    return names;
  }

}
