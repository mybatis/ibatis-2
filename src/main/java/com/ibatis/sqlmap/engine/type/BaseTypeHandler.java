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
package com.ibatis.sqlmap.engine.type;

/**
 * Base type handler for convenience.
 */
public abstract class BaseTypeHandler implements TypeHandler {

  @Override
  public boolean equals(Object object, String string) {
    if (object == null || string == null) {
      return object == string;
    }
    Object castedObject = valueOf(string);
    return object.equals(castedObject);
  }

}
