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
package com.ibatis.common.logging.nologging;

import com.ibatis.common.logging.Log;

/**
 * The Class NoLoggingImpl.
 */
public class NoLoggingImpl implements Log {

  /**
   * Instantiates a new no logging impl.
   *
   * @param clazz
   *          the clazz
   */
  public NoLoggingImpl(Class clazz) {
  }

  @Override
  public boolean isDebugEnabled() {
    return false;
  }

  @Override
  public void error(String s, Throwable e) {
  }

  @Override
  public void error(String s) {
  }

  @Override
  public void debug(String s) {
  }

  @Override
  public void warn(String s) {
  }

}
