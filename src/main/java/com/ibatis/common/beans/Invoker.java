/*
 * Copyright 2004-2020 the original author or authors.
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
package com.ibatis.common.beans;

import java.lang.reflect.InvocationTargetException;

/**
 * The Interface Invoker.
 */
public interface Invoker {

  /**
   * Gets the name.
   *
   * @return the name
   */
  String getName();

  /**
   * Invoke.
   *
   * @param target
   *          the target
   * @param args
   *          the args
   * @return the object
   * @throws IllegalAccessException
   *           the illegal access exception
   * @throws InvocationTargetException
   *           the invocation target exception
   */
  Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;
}
