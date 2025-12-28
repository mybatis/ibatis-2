/*
 * Copyright 2004-2025 the original author or authors.
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * The Class GetFieldInvoker.
 */
public class GetFieldInvoker implements Invoker {

  /** The field. */
  private Field field;

  /** The name. */
  private String name;

  /**
   * Instantiates a new gets the field invoker.
   *
   * @param field
   *          the field
   */
  public GetFieldInvoker(Field field) {
    this.field = field;
    this.name = "(" + field.getName() + ")";
  }

  @Override
  public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
    return field.get(target);
  }

  @Override
  public String getName() {
    return name;
  }
}
