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
package com.ibatis.common.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The Class MethodInvoker.
 */
public class MethodInvoker implements Invoker {

  /** The method. */
  private Method method;

  /** The name. */
  private String name;

  /**
   * Instantiates a new method invoker.
   *
   * @param method
   *          the method
   */
  public MethodInvoker(Method method) {
    this.method = method;
    this.name = method.getName();
  }

  public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
    return method.invoke(target, args);
  }

  /**
   * Gets the method.
   *
   * @return the method
   */
  public Method getMethod() {
    return method;
  }

  public String getName() {
    return name;
  }
}
