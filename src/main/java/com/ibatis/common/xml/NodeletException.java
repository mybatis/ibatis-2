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
package com.ibatis.common.xml;

/**
 * The Class NodeletException.
 */
public class NodeletException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new nodelet exception.
   */
  public NodeletException() {
    super();
  }

  /**
   * Instantiates a new nodelet exception.
   *
   * @param msg
   *          the msg
   */
  public NodeletException(String msg) {
    super(msg);
  }

  /**
   * Instantiates a new nodelet exception.
   *
   * @param cause
   *          the cause
   */
  public NodeletException(Throwable cause) {
    super(cause);
  }

  /**
   * Instantiates a new nodelet exception.
   *
   * @param msg
   *          the msg
   * @param cause
   *          the cause
   */
  public NodeletException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
