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
package com.ibatis.common.logging.jdk14;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class Jdk14LoggingImpl.
 */
public class Jdk14LoggingImpl implements com.ibatis.common.logging.Log {

  /** The log. */
  private Logger log;

  /**
   * Instantiates a new jdk 14 logging impl.
   *
   * @param clazz
   *          the clazz
   */
  public Jdk14LoggingImpl(Class clazz) {
    log = Logger.getLogger(clazz.getName());
  }

  @Override
  public boolean isDebugEnabled() {
    return log.isLoggable(Level.FINE);
  }

  @Override
  public void error(String s, Throwable e) {
    log.log(Level.SEVERE, s, e);
  }

  @Override
  public void error(String s) {
    log.log(Level.SEVERE, s);
  }

  @Override
  public void debug(String s) {
    log.log(Level.FINE, s);
  }

  @Override
  public void warn(String s) {
    log.log(Level.WARNING, s);
  }

}
