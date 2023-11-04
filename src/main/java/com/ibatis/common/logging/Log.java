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
package com.ibatis.common.logging;

/**
 * The Interface Log.
 */
public interface Log {

  /**
   * Checks if is debug enabled.
   *
   * @return true, if is debug enabled
   */
  boolean isDebugEnabled();

  /**
   * Error.
   *
   * @param s
   *          the s
   * @param e
   *          the e
   */
  void error(String s, Throwable e);

  /**
   * Error.
   *
   * @param s
   *          the s
   */
  void error(String s);

  /**
   * Debug.
   *
   * @param s
   *          the s
   */
  public void debug(String s);

  /**
   * Warn.
   *
   * @param s
   *          the s
   */
  public void warn(String s);

}
